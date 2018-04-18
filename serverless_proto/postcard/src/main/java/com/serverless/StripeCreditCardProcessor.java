package com.serverless;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.serverless.exceptions.ChargeProcessingException;
import com.serverless.exceptions.RefundChargeException;
import com.serverless.exceptions.UpdateChargeException;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.CardException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;

public class StripeCreditCardProcessor implements CreditCardProcessor {

  @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
  @Inject
  public StripeCreditCardProcessor(Environment environment) {
    Stripe.apiKey = environment.getStripeSecretKey();
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

  @Override
  public void updateWithJob(Order order) throws UpdateChargeException {
    try {
      com.stripe.model.Charge.retrieve(order.getCharge().getId())
          .update(ImmutableMap.<String, Object>builder()
          .put("metadata", ImmutableMap.<String, Object>builder()
              .put("order_id", order.getJob().getId())
              .put("order_url", order.getJob().getUrl())
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

  @Override
  public void refund(Order order) throws RefundChargeException {
    try {
      com.stripe.model.Charge.retrieve(order.getCharge().getId()).refund();
    } catch (com.stripe.exception.AuthenticationException
        | com.stripe.exception.InvalidRequestException
        | APIConnectionException
        | CardException
        | com.stripe.exception.APIException e) {
      throw new RefundChargeException(e);
    }
  }

  private Order getUpdatedOrderFromCharge(Order order, com.stripe.model.Charge charge) {
    return new Order(
        order.getPostCard(),
        order.getPayment(),
        order.getOrderId(),
        new Charge(
            charge.getId(), charge.getAmount(), charge.getCurrency(), charge.getDescription()),
        null
    );
  }
}
