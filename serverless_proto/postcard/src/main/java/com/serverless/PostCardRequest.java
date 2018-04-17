package com.serverless;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class PostCardRequest {

  @SerializedName("payment")
  private final Payment paymentInfo;
  @SerializedName("postcard")
  private final PostCard postCard;

  public PostCardRequest(Payment paymentInfo, PostCard postCard) {
    this.paymentInfo = paymentInfo;
    this.postCard = postCard;
  }

  public Payment getPaymentInfo() {
    return paymentInfo;
  }

  public PostCard getPostCard() {
    return postCard;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostCardRequest that = (PostCardRequest) o;
    return Objects.equals(paymentInfo, that.paymentInfo)
        && Objects.equals(postCard, that.postCard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(paymentInfo, postCard);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("paymentInfo", paymentInfo)
        .add("postCard", postCard)
        .toString();
  }
}
