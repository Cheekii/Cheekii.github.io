package com.serverless.exceptions;

public class RefundChargeException extends Exception {

  public RefundChargeException(Exception e) {
    super(e);
  }
}
