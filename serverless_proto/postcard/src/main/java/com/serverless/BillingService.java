package com.serverless;

import com.google.inject.Inject;
import com.serverless.exceptions.ChargeProcessingException;
import org.apache.log4j.Logger;

public class BillingService {
  private final CreditCardProcessor processor;
  private static final Logger LOG = Logger.getLogger(BillingService.class);

  @Inject
  public BillingService(CreditCardProcessor processor) {
    this.processor = processor;
  }

  void authorizeOrder(Order order) {
    LOG.info("Authorizing payment for order: " + order);
  }

  Order chargeOrder(Order order) throws ChargeProcessingException {
    LOG.info("Charging payment for order: " + order.getOrderId());
    return processor.charge(order);
  }
}
