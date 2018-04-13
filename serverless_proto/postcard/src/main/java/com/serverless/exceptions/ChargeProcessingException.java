package com.serverless.exceptions;

public class ChargeProcessingException extends Exception {

  public ChargeProcessingException(Exception e) {
    super(e);
  }
}
