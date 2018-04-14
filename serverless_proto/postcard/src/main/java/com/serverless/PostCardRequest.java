package com.serverless;

import com.google.common.base.MoreObjects;
import com.lob.model.Address;

public class PostCardRequest {

  private String stripe;
  private String name;
  private String message;
  private String base64image;
  private Address toAddress;
  private Address fromAddress;
  private String code;

  /**
   * Constructs a post card request.
   *
   * @param name the recipients name
   * @param message the postcard's message
   * @param stripeToken the payment token (proof of purchase)
   * @param toAddress the recipients address
   * @param fromAddress the senders address
   * @param base64image the base64 encoded image
   */
  public PostCardRequest(String name, String message, String stripeToken, Address toAddress,
      Address fromAddress, String base64image, String code) {
    this.name = name;
    this.message = message;
    this.stripe = stripeToken;
    this.toAddress = toAddress;
    this.fromAddress = fromAddress;
    this.base64image = base64image;
    this.code = code;
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

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("stripe", stripe)
        .add("name", name)
        .add("message", message)
        .add("base64image", base64image)
        .add("toAddress", toAddress)
        .add("fromAddress", fromAddress)
        .add("code", code)
        .toString();
  }
}
