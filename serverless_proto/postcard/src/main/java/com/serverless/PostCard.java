package com.serverless;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;
import java.net.URL;
import java.util.Objects;

public class PostCard {

  @SerializedName("name")
  private final String name;
  @SerializedName("message")
  private final String message;
  @SerializedName("base64image")
  private final String base64image;
  @SerializedName("imageUrl")
  private final URL imageUrl;
  @SerializedName("toAddress")
  private final Address toAddress;
  @SerializedName("fromAddress")
  private final Address fromAddress;
  @SerializedName("code")
  private final String code;

  /**
   * Constructs a post card request.
   *
   * @param name the recipients name
   * @param message the postcard's message
   * @param toAddress the recipients address
   * @param fromAddress the senders address
   * @param base64image the base64 encoded image
   */
  public PostCard(String name, String message, Address toAddress, Address fromAddress,
      String base64image, URL imageUrl, String code) {
    this.name = name;
    this.message = message;
    this.toAddress = toAddress;
    this.fromAddress = fromAddress;
    this.base64image = base64image;
    this.imageUrl = imageUrl;
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public String getMessage() {
    return message;
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

  public URL getImageUrl() {
    return imageUrl;
  }

  public String getCode() {
    return code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostCard postCard = (PostCard) o;
    return Objects.equals(name, postCard.name)
        && Objects.equals(message, postCard.message)
        && Objects.equals(base64image, postCard.base64image)
        && Objects.equals(imageUrl, postCard.imageUrl)
        && Objects.equals(toAddress, postCard.toAddress)
        && Objects.equals(fromAddress, postCard.fromAddress)
        && Objects.equals(code, postCard.code);
  }

  @Override
  public int hashCode() {

    return Objects.hash(name, message, base64image, imageUrl, toAddress, fromAddress, code);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("message", message)
        .add("base64image", base64image)
        .add("imageUrl", imageUrl)
        .add("toAddress", toAddress)
        .add("fromAddress", fromAddress)
        .add("code", code)
        .toString();
  }
}
