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
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
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
  private static final Gson GSON = new Gson();
  private static final Logger LOG = Logger.getLogger(Handler.class);
  private static final Injector INJECTOR = Guice.createInjector(
      new EnvironmentModule(),
      new BillingModule(),
      new ImageStorageModule(),
      new PostCardModule());

  @Inject
  private BillingService billingService;

  @Inject
  private Environment environment;

  @Inject
  private ImageStorageService imageStorageService;

  @Inject
  private PostCardService postCardService;

  public Handler() {
    INJECTOR.injectMembers(this);
    initialize();
  }

  /**
   * Creates a handler instance with a given environment and billing service.
   * @param environment the environment
   * @param billingService the service
   */
  public Handler(
      Environment environment,
      BillingService billingService,
      ImageStorageService imageStorageService) {
    this.billingService = billingService;
    this.environment = environment;
    this.imageStorageService = imageStorageService;
    initialize();
  }

  @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
  private void initialize() {
    Lob.init(environment.getLobSecretKey(), environment.getLobApiVersion());
    Stripe.apiKey = environment.getStripeSecretKey();
  }

  @Override
  public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    LOG.info("received: " + input);

    PostCardRequest request = GSON
        .fromJson(String.valueOf(input.get("body")), PostCardRequest.class);

    Order order = new Order(
        request.getPostCard(),
        request.getPaymentInfo(),
        UUID.randomUUID(),
        new Charge(
            environment.getPostcardPrice(),
            environment.getPostcardCurrancy(),
            "Cheekii Gram"),
        null
    );

    // start by charging the card
    try {
      order = billingService.chargeOrder(order);
    } catch (ChargeProcessingException e) {
      LOG.error("Failed to charge card", e);
      return getFailureResponse("failed to charge postcard");
    }

    // Upload image to S3
    order = imageStorageService.upload(order, environment.getBucketName());

    try {
      order = postCardService.submit(order);
    } catch (PostcardCreationException e) {
      String failureMessage = String
          .format("Postcard could not be created for order: %s", order.getOrderId());
      LOG.error(failureMessage, e);
      try {
        billingService.refundOrder(order);
      } catch (RefundChargeException e1) {
        failureMessage = String
            .format("Postcard could not be created for order: %s Failed to refund.",
                order.getOrderId());
        LOG.error(failureMessage, e1);
      }
      return getFailureResponse(failureMessage);
    }

    try {
      billingService.updateWithJob(order);
    } catch (UpdateChargeException e) {
      String failureMessage = String
          .format("Failed to update charge with metadata for order: %s", order.getOrderId());
      LOG.error(failureMessage, e);
    }

    ImmutableMap map = ImmutableMap.of(
        "orderId", order.getOrderId(),
        "stripeId", order.getCharge().getId(),
        "imageUrl", order.getPostCard().getImageUrl()
    );
    return ApiGatewayResponse.builder()
        .setStatusCode(200)
        .setObjectBody(GSON.toJson(map))
        .setHeaders(ImmutableMap.<String, String>builder()
            .put("X-Powered-By", "AWS Lambda & serverless")
            .put("Content-Type", "application/json")
            .put("Access-Control-Allow-Origin", "*")
            .build())
        .build();
  }

  private ApiGatewayResponse getFailureResponse(String failureMessage) {
    return ApiGatewayResponse.builder()
        .setStatusCode(500)
        .setObjectBody(failureMessage)
        .setHeaders(ImmutableMap.<String, String>builder()
            .put("X-Powered-By", "AWS Lambda & serverless")
            .put("Content-Type", "text/plain")
            .build())
        .build();
  }
}
