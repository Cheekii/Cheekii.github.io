package com.serverless;

import com.serverless.exceptions.ChargeProcessingException;

public interface CreditCardProcessor {

  void auth();

  Order charge(Order order) throws ChargeProcessingException;

  void update();

  void refund();
}
