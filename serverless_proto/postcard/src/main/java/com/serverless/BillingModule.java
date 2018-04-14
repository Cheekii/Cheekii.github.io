package com.serverless;

import com.google.inject.AbstractModule;

public class BillingModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CreditCardProcessor.class).to(StripeCreditCardProcessor.class);
  }
}
