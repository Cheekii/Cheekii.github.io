package com.serverless;

import com.google.inject.Inject;
import com.serverless.exceptions.ChargeProcessingException;
import com.serverless.exceptions.RefundChargeException;
import com.serverless.exceptions.UpdateChargeException;
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

  void refundOrder(Order order) throws RefundChargeException {
    processor.refund(order);
  }

  void updateWithJob(Order order) throws UpdateChargeException {
    processor.updateWithJob(order);
  }
}
