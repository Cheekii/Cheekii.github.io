package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
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
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.CardException;
import com.stripe.model.Charge;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

  private static final String STRIPE_SECRET_KEY = System.getenv().get("STRIPE_SECRET_KEY");
  private static final String LOB_SECRET_KEY = System.getenv().get("LOB_SECRET_KEY");
  private static final String LOB_API_VERSION = System.getenv().get("LOB_API_VERSION");
  private static final Integer POSTCARD_PRICE = Integer.valueOf(System.getenv().get("POSTCARD_PRICE"));
  private static final String POSTCARD_CURRANCY = System.getenv().get("POSTCARD_CURRANCY");

  private static final Logger LOG = Logger.getLogger(Handler.class);

  public Handler() {
    Lob.init(LOB_SECRET_KEY, LOB_API_VERSION);
    Stripe.apiKey = STRIPE_SECRET_KEY;
  }

  @Override
  public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    LOG.info("received: " + input);
    Response responseBody = new Response("Go Serverless v1.x! Your function executed successfully!",
        input);

    String stripeToken = String.valueOf(input.get("stripe"));
    String name = String.valueOf(input.get("name"));
    String message = String.valueOf(input.get("message"));
    UUID orderGuid = UUID.randomUUID();

    Charge charge;
    try {
      charge = chargeCard(stripeToken, orderGuid);
    } catch (ChargeProcessingException e) {
      LOG.error("Failed to charge card", e);
      return ApiGatewayResponse.builder()
          .setStatusCode(500)
          .setObjectBody("failed to charge postcard")
          .setHeaders(ImmutableMap.<String,String>builder()
              .put("X-Powered-By","AWS Lambda & serverless")
              .put("Content-Type", "text/plain")
              .build())
          .build();
    }

    Postcard postcard;
    try {
      postcard = createPostCard(name, message, orderGuid);

    } catch (PostcardCreationException e) {
      LOG.error("Failed to create postcard", e);
      String failureMessage = String.format("Postcard could not be created for order: %s", orderGuid);
      try {
        refundCharge(charge);
      } catch (RefundChargeException e1) {
        LOG.error(String.format("Failed to refund charge for order: %s", orderGuid), e1);
        failureMessage = String.format("Postcard could not be created for order: %s Failed to refund.", orderGuid);
      }
      return ApiGatewayResponse.builder()
          .setStatusCode(500)
          .setObjectBody(failureMessage)
          .setHeaders(ImmutableMap.<String,String>builder()
              .put("X-Powered-By","AWS Lambda & serverless")
              .put("Content-Type", "text/plain")
              .build())
          .build();
    }

    try {
      updateChargeWithOrderId(charge, postcard);
    } catch (UpdateChargeException e) {
      LOG.error("Failed to update charge with metadata", e);
    }

    Gson gson = new Gson();
    String jsonPostcard = gson.toJson(postcard);
    return ApiGatewayResponse.builder()
        .setStatusCode(200)
        .setObjectBody(jsonPostcard)
        .setHeaders(ImmutableMap.<String,String>builder()
            .put("X-Powered-By","AWS Lambda & serverless")
            .put("Content-Type", "application/json")
            .build())
        .build();
  }

  private Charge refundCharge(Charge charge) throws RefundChargeException {
    try {
      return charge.refund();
    } catch (com.stripe.exception.AuthenticationException |
        com.stripe.exception.InvalidRequestException |
        APIConnectionException |
        CardException |
        com.stripe.exception.APIException e) {
      throw new RefundChargeException(e);
    }
  }

  private void updateChargeWithOrderId(Charge charge, Postcard postcard) throws UpdateChargeException {
    try {
      charge.update(ImmutableMap.<String, Object>builder()
          .put("metadata", ImmutableMap.<String, Object>builder()
              .put("order_id", postcard.getId())
              .put("order_url", postcard.getUrl())
              .build())
          .build());
    } catch (com.stripe.exception.AuthenticationException |
        com.stripe.exception.InvalidRequestException |
        APIConnectionException |
        CardException |
        com.stripe.exception.APIException e) {
      throw new UpdateChargeException(e);
    }
  }

  private Charge chargeCard(String stripeToken, UUID orderGuid) throws ChargeProcessingException{
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
    } catch (com.stripe.exception.AuthenticationException |
        com.stripe.exception.InvalidRequestException |
        APIConnectionException |
        CardException |
        com.stripe.exception.APIException e) {
      throw new ChargeProcessingException(e);
    }
  }

  private Postcard createPostCard(String name, String message, UUID orderGuid)
      throws PostcardCreationException{
    Map<String, String> mergeVariables = new HashMap<>();
    mergeVariables.put("name", name);
    mergeVariables.put("message", message);

    LobResponse<Postcard> response = null;
    try {
      response = new Postcard.RequestBuilder()
          .setDescription("Demo Postcard")
          .setTo(
              new Address.RequestBuilder()
                  .setName("Harry Zhang")
                  .setLine1("521-35 Inglewood Park")
                  .setLine2("")
                  .setCity("Calgary")
                  .setState("AB")
                  .setZip("T2G1B5")
                  .setCountry("CA")
          )
          .setFrom(
              new Address.RequestBuilder()
                  .setName("Leore Avidar")
                  .setLine1("185 Berry St")
                  .setLine2("# 6100")
                  .setCity("San Francisco")
                  .setState("CA")
                  .setZip("94107")
                  .setCountry("US")
          )
          .setFront("<html style='padding: 1in; font-size: 50;'>Dear {{name}}, {{message}}</html>")
          .setBack("<html style='padding: 1in; font-size: 20;'>Back HTML for {{name}}</html>")
          .setMergeVariables(mergeVariables)
          .setMetadata(Collections.singletonMap("order_guid", orderGuid.toString()))
          .create();
    } catch (APIException |
        IOException |
        AuthenticationException |
        InvalidRequestException |
        RateLimitException e) {
      throw new PostcardCreationException(e);
    }

    return response.getResponseBody();
  }

}
