package com.serverless;

import com.google.common.collect.ImmutableMap;
import com.serverless.exceptions.ChargeProcessingException;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;

import com.stripe.exception.InvalidRequestException;
import java.util.Map;

public class StripeCreditCardProcessor implements CreditCardProcessor {

  @Override
  public void auth() {

  }

  @Override
  public Order charge(Order order) throws ChargeProcessingException {
    try {
      // Charge the user's card:
      Map<String, Object> params = ImmutableMap.<String, Object>builder()
          .put("amount", order.getCharge().getPrice())
          .put("currency", order.getCharge().getCurrency())
          .put("description", order.getCharge().getDescription())
          .put("metadata", ImmutableMap.<String, Object>builder()
              .put("order_guid", order.getOrderId())
              .build())
          .put("statement_descriptor", "Cheekii.co")
          .put("source", order.getPayment().getPaymentToken())
          .build();
      return getUpdatedOrderFromCharge(order, com.stripe.model.Charge.create(params));
    } catch (com.stripe.exception.AuthenticationException
        | com.stripe.exception.InvalidRequestException
        | APIConnectionException
        | CardException
        | com.stripe.exception.APIException e) {
      throw new ChargeProcessingException(e);
    }
  }

  private Order getUpdatedOrderFromCharge(Order order, com.stripe.model.Charge charge) {
    return new Order(
        order.getPostCard(),
        order.getPayment(),
        order.getOrderId(),
        new Charge(
            charge.getId(), charge.getAmount(), charge.getCurrency(), charge.getDescription())
    );
  }

  @Override
  public void update() {

  }

  @Override
  public void refund() {

  }
}
