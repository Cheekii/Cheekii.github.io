package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.lob.Lob;
import com.stripe.Stripe;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;

public class Handler2 implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

  private static final String STRIPE_SECRET_KEY = System.getenv().get("STRIPE_SECRET_KEY");
  private static final String LOB_SECRET_KEY = System.getenv().get("LOB_SECRET_KEY");
  private static final String LOB_API_VERSION = System.getenv().get("LOB_API_VERSION");
  private static final Integer POSTCARD_PRICE = Integer.valueOf(System.getenv().get("POSTCARD_PRICE"));
  private static final String POSTCARD_CURRANCY = System.getenv().get("POSTCARD_CURRANCY");
  private static final String BUCKET_NAME = System.getenv().get("BUCKET");

  private static final Gson GSON = new Gson();
  private static final Logger LOG = Logger.getLogger(Handler2.class);

  public Handler2() {
    Lob.init(LOB_SECRET_KEY, LOB_API_VERSION);
    Stripe.apiKey = STRIPE_SECRET_KEY;
  }

  @Override
  public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    LOG.info("received: " + input);

    PostCardRequest request = GSON.fromJson(String.valueOf(input.get("body")), PostCardRequest.class);
    LOG.info("request:" + request);
    UUID orderGuid = UUID.randomUUID();

    List<Object> stuff = Arrays.asList(
        request.getStripe(),
        request.getMessage(),
        request.getName(),
        request.getToAddress(),
        request.getFromAddress(),
        orderGuid);

    InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(request.getBase64image()));

    AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    String key = orderGuid.toString() + ".jpg";
    PutObjectRequest putObjectRequest = new PutObjectRequest(
        BUCKET_NAME,
        key,
        inputStream, null);
    PutObjectResult result  = s3.putObject(putObjectRequest);
    URL url = s3.getUrl(BUCKET_NAME, key);
    return ApiGatewayResponse.builder()
        .setStatusCode(200)
        .setObjectBody(new Gson().toJson(url))
        .setHeaders(ImmutableMap.<String,String>builder()
            .put("X-Powered-By","AWS Lambda & serverless")
            .put("Content-Type", "application/json")
            .build())
        .build();
  }
}
