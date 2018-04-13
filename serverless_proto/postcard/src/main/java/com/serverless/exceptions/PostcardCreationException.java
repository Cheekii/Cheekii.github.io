package com.serverless.exceptions;

public class PostcardCreationException extends Exception {

  public PostcardCreationException(Exception e) {
    super(e);
  }
}
