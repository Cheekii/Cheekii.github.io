package com.serverless;

import com.google.inject.Inject;
import org.apache.log4j.Logger;

public class BillingService {
  private final CreditCardProcessor processor;
  private static final Logger LOG = Logger.getLogger(BillingService.class);

  @Inject
  public BillingService(CreditCardProcessor processor) {
    this.processor = processor;
  }

  void authorizeOrder(PostCardRequest order) {
    LOG.info("Authorizing payment for order: " + order);
  }

}
