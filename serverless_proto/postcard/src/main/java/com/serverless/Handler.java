package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.lob.Lob;
import com.lob.exception.APIException;
import com.lob.exception.AuthenticationException;
import com.lob.exception.InvalidRequestException;
import com.lob.exception.RateLimitException;
import com.lob.model.Address;
import com.lob.model.Postcard;
import com.lob.net.LobResponse;
import com.serverless.exceptions.ChargeProcessingException;
import com.serverless.exceptions.PostcardCreationException;
import com.serverless.exceptions.RefundChargeException;
import com.serverless.exceptions.UpdateChargeException;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.CardException;
import com.stripe.model.Charge;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

  private static final String STRIPE_SECRET_KEY = System.getenv().get("STRIPE_SECRET_KEY");
  private static final String LOB_SECRET_KEY = System.getenv().get("LOB_SECRET_KEY");
  private static final String LOB_API_VERSION = System.getenv().get("LOB_API_VERSION");
  private static final Integer POSTCARD_PRICE = Integer
      .valueOf(System.getenv().get("POSTCARD_PRICE"));
  private static final String POSTCARD_CURRANCY = System.getenv().get("POSTCARD_CURRANCY");
  private static final String DISCOUNT_CODE = System.getenv().get("DISCOUNT_CODE");
  private static final String BUCKET_NAME = System.getenv().get("BUCKET");


  private static final Gson GSON = new Gson();
  private static final Logger LOG = Logger.getLogger(Handler.class);

  @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
  public Handler() {
    Lob.init(LOB_SECRET_KEY, LOB_API_VERSION);
    Stripe.apiKey = STRIPE_SECRET_KEY;
  }

  @Override
  public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    LOG.info("received: " + input);

    PostCardRequest request = GSON
        .fromJson(String.valueOf(input.get("body")), PostCardRequest.class);

    UUID orderGuid = UUID.randomUUID();

    final boolean discountApplied = request.getCode().equals(DISCOUNT_CODE);

    Charge charge = null;
    if (!discountApplied) {
      try {
        charge = chargeCard(request.getStripe(), orderGuid);
      } catch (ChargeProcessingException e) {
        LOG.error("Failed to charge card", e);
        return ApiGatewayResponse.builder()
            .setStatusCode(500)
            .setObjectBody("failed to charge postcard")
            .setHeaders(ImmutableMap.<String, String>builder()
                .put("X-Powered-By", "AWS Lambda & serverless")
                .put("Content-Type", "text/plain")
                .build())
            .build();
      }
    }
    byte[] decodedImage = Base64.getDecoder().decode(request.getBase64image().split(",")[1]);
    InputStream inputStream = new ByteArrayInputStream(decodedImage);
    AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(decodedImage.length);
    String key = orderGuid.toString() + ".jpg";
    PutObjectRequest putObjectRequest = new PutObjectRequest(
        BUCKET_NAME,
        key,
        inputStream, metadata);
    PutObjectResult result = s3.putObject(putObjectRequest);
    LOG.info("PutObjectResult:" + result);
    URL url = s3.getUrl(BUCKET_NAME, key);

    Postcard postcard;
    try {
      postcard = createPostCard(request, url, orderGuid);

    } catch (PostcardCreationException e) {
      LOG.error("Failed to create postcard", e);
      String failureMessage = String
          .format("Postcard could not be created for order: %s", orderGuid);
      if (!discountApplied) {
        try {
          refundCharge(charge);
        } catch (RefundChargeException e1) {
          LOG.error(String.format("Failed to refund charge for order: %s", orderGuid), e1);
          failureMessage = String
              .format("Postcard could not be created for order: %s Failed to refund.", orderGuid);
        }
      }
      return ApiGatewayResponse.builder()
          .setStatusCode(500)
          .setObjectBody(failureMessage)
          .setHeaders(ImmutableMap.<String, String>builder()
              .put("X-Powered-By", "AWS Lambda & serverless")
              .put("Content-Type", "text/plain")
              .build())
          .build();
    }

    if (!discountApplied) {
      try {
        updateChargeWithOrderId(charge, postcard);
      } catch (UpdateChargeException e) {
        LOG.error("Failed to update charge with metadata", e);
      }
    }

    String jsonPostcard = GSON.toJson(postcard);
    return ApiGatewayResponse.builder()
        .setStatusCode(200)
        .setObjectBody(jsonPostcard)
        .setHeaders(ImmutableMap.<String, String>builder()
            .put("X-Powered-By", "AWS Lambda & serverless")
            .put("Content-Type", "application/json")
            .put("Access-Control-Allow-Origin", "*")
            .build())
        .build();
  }

  private Charge refundCharge(Charge charge) throws RefundChargeException {
    try {
      return charge.refund();
    } catch (com.stripe.exception.AuthenticationException
        | com.stripe.exception.InvalidRequestException
        | APIConnectionException
        | CardException
        | com.stripe.exception.APIException e) {
      throw new RefundChargeException(e);
    }
  }

  private void updateChargeWithOrderId(Charge charge, Postcard postcard)
      throws UpdateChargeException {
    try {
      charge.update(ImmutableMap.<String, Object>builder()
          .put("metadata", ImmutableMap.<String, Object>builder()
              .put("order_id", postcard.getId())
              .put("order_url", postcard.getUrl())
              .build())
          .build());
    } catch (com.stripe.exception.AuthenticationException
        | com.stripe.exception.InvalidRequestException
        | APIConnectionException
        | CardException
        | com.stripe.exception.APIException e) {
      throw new UpdateChargeException(e);
    }
  }

  private Charge chargeCard(String stripeToken, UUID orderGuid) throws ChargeProcessingException {
    try {
      // Charge the user's card:
      Map<String, Object> params = ImmutableMap.<String, Object>builder()
          .put("amount", POSTCARD_PRICE)
          .put("currency", POSTCARD_CURRANCY)
          .put("description", "Cheekii gram")
          .put("metadata", ImmutableMap.<String, Object>builder()
              .put("order_guid", orderGuid)
              .build())
          .put("statement_descriptor", "Cheekii.co - Postcard")
          .put("source", stripeToken)
          .build();
      return Charge.create(params);
    } catch (com.stripe.exception.AuthenticationException
        | com.stripe.exception.InvalidRequestException
        | APIConnectionException
        | CardException
        | com.stripe.exception.APIException e) {
      throw new ChargeProcessingException(e);
    }
  }

  private Postcard createPostCard(
      PostCardRequest request,
      URL image,
      UUID orderGuid)
      throws PostcardCreationException {
    Map<String, String> mergeVariables = new HashMap<>();
    mergeVariables.put("name", request.getName());
    mergeVariables.put("message", request.getMessage());
    mergeVariables.put("img", image.toString());

    Address toAddress = request.getToAddress();
    Address fromAddress = request.getFromAddress();
    LobResponse<Postcard> response = null;
    try {
      response = new Postcard.RequestBuilder()
          .setDescription("Demo Postcard")
          .setTo(
              new Address.RequestBuilder()
                  .setName(Strings.nullToEmpty(toAddress.getName()))
                  .setLine1(Strings.nullToEmpty(toAddress.getLine1()))
                  .setLine2(Strings.nullToEmpty(toAddress.getLine2()))
                  .setCity(Strings.nullToEmpty(toAddress.getCity()))
                  .setState(Strings.nullToEmpty(toAddress.getState()))
                  .setZip(Strings.nullToEmpty(toAddress.getZip()))
                  .setCountry(Strings.nullToEmpty(toAddress.getCountry()))
                  .setPhone(Strings.nullToEmpty(toAddress.getPhone()))
                  .setEmail(Strings.nullToEmpty(toAddress.getEmail()))
          )
          .setFrom(
              new Address.RequestBuilder()
                  .setName(Strings.nullToEmpty(fromAddress.getName()))
                  .setLine1(Strings.nullToEmpty(fromAddress.getLine1()))
                  .setLine2(Strings.nullToEmpty(fromAddress.getLine2()))
                  .setCity(Strings.nullToEmpty(fromAddress.getCity()))
                  .setState(Strings.nullToEmpty(fromAddress.getState()))
                  .setZip(Strings.nullToEmpty(fromAddress.getZip()))
                  .setCountry(Strings.nullToEmpty(fromAddress.getCountry()))
                  .setPhone(Strings.nullToEmpty(fromAddress.getPhone()))
                  .setEmail(Strings.nullToEmpty(fromAddress.getEmail()))
          )
          .setFront("tmpl_5635fab87f724c5")
          .setBack("tmpl_87d4ff5f4dc694d")
          .setMergeVariables(mergeVariables)
          .setMetadata(Collections.singletonMap("order_guid", orderGuid.toString()))
          .create();
    } catch (APIException
        | IOException
        | AuthenticationException
        | InvalidRequestException
        | RateLimitException e) {
      throw new PostcardCreationException(e);
    }

    return response.getResponseBody();
  }

}
