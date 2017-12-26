package com.serverless;

import com.lob.model.Address;
import java.util.Base64;

public class PostCardRequest {
  private String stripe;
  private String name;
  private String message;
  private String base64image;
  private Address toAddress;
  private Address fromAddress;

  public PostCardRequest(String name, String message, String stripeToken, Address toAddress, Address fromAddress, String base64image) {
    this.name = name;
    this.message = message;
    this.stripe = stripeToken;
    this.toAddress = toAddress;
    this.fromAddress = fromAddress;
    this.base64image = base64image;
  }

  public String getName() {
    return name;
  }

  public String getMessage() {
    return message;
  }

  public String getStripe() {
    return stripe;
  }

  public Address getToAddress() {
    return toAddress;
  }

  public Address getFromAddress() {
    return fromAddress;
  }

  public String getBase64image() {
    return base64image;
  }
}
