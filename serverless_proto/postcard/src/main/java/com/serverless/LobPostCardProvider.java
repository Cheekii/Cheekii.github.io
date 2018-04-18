package com.serverless;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.lob.Lob;
import com.lob.exception.APIException;
import com.lob.exception.AuthenticationException;
import com.lob.exception.InvalidRequestException;
import com.lob.exception.RateLimitException;
import com.lob.model.Postcard;
import com.lob.net.LobResponse;
import com.serverless.exceptions.PostcardCreationException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LobPostCardProvider implements PostCardProvider {

  private final String frontTemplateId;
  private final String backTemplateId;

  /**
   * Constructs a Lob post card provider.
   * @param environment the environment
   */
  @Inject
  public LobPostCardProvider(Environment environment) {
    Lob.init(environment.getLobSecretKey(), environment.getLobApiVersion());
    frontTemplateId = environment.getLobFrontTemplateId();
    backTemplateId = environment.getLobBackTemplateId();
  }

  @Override
  public Order submit(Order order) throws PostcardCreationException {
    Map<String, String> mergeVariables = new HashMap<>();
    mergeVariables.put("name", order.getPostCard().getName());
    mergeVariables.put("message", order.getPostCard().getMessage());
    mergeVariables.put("img", order.getPostCard().getImageUrl().toString());

    Address toAddress = order.getPostCard().getToAddress();
    Address fromAddress = order.getPostCard().getFromAddress();
    LobResponse<Postcard> response;
    try {
      response = new Postcard.RequestBuilder()
          .setDescription("Demo Postcard")
          .setTo(
              new com.lob.model.Address.RequestBuilder()
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
              new com.lob.model.Address.RequestBuilder()
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
          .setFront(frontTemplateId)
          .setBack(backTemplateId)
          .setMergeVariables(mergeVariables)
          .setMetadata(Collections.singletonMap("order_guid", order.getOrderId().toString()))
          .create();
    } catch (APIException
        | IOException
        | AuthenticationException
        | InvalidRequestException
        | RateLimitException e) {
      throw new PostcardCreationException(e);
    }
    return getOrderWithUpdatedJob(order, response.getResponseBody());
  }

  private Order getOrderWithUpdatedJob(Order order, Postcard postCard) {
    return new Order(
        order.getPostCard(),
        order.getPayment(),
        order.getOrderId(),
        order.getCharge(),
        new Job(postCard.getId(), postCard.getUrl())
    );
  }
}
