package com.serverless;

public interface CreditCardProcessor {

  void auth();

  void charge();

  void update();

  void refund();
}
