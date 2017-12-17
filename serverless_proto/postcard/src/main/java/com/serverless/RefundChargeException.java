package com.serverless;

class RefundChargeException extends Exception {

  RefundChargeException(Exception e)
  {
    super(e);
  }
}
