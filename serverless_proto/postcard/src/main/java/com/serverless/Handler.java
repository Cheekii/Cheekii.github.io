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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

  private static final String STRIPE_SECRET_KEY = System.getenv().get("STRIPE_SECRET_KEY");
  private static final String LOB_SECRET_KEY = System.getenv().get("LOB_SECRET_KEY");
  private static final Logger LOG = Logger.getLogger(Handler.class);

  public Handler() {
    Lob.init("test_4af73aad4d47b5590a8f7f1dbd145d093e4");
  }

  @Override
  public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    LOG.info("received: " + input);
    Response responseBody = new Response("Go Serverless v1.x! Your function executed successfully!",
        input);

    String stripeToken = String.valueOf(input.get("stripe"));
    String name = String.valueOf(input.get("name"));
    String message = String.valueOf(input.get("message"));

    try {
      Postcard postcard = createPostCard(name, message);
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
    } catch (APIException | IOException | AuthenticationException |
        InvalidRequestException | RateLimitException e) {
      e.printStackTrace();
      return ApiGatewayResponse.builder()
          .setStatusCode(500)
          .setObjectBody("failed to produce postcard")
          .setHeaders(ImmutableMap.<String,String>builder()
              .put("X-Powered-By","AWS Lambda & serverless")
              .put("Content-Type", "text/plain")
              .build())
          .build();
    }
  }

  private Postcard createPostCard(String name, String message)
      throws APIException, IOException, AuthenticationException,
      InvalidRequestException, RateLimitException {
    Map<String, String> mergeVariables = new HashMap<>();
    mergeVariables.put("name", name);
    mergeVariables.put("message", message);

    LobResponse<Postcard> response = new Postcard.RequestBuilder()
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
        .create();

    return response.getResponseBody();
  }

}
