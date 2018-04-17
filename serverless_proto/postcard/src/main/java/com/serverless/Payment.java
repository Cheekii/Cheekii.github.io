package com.serverless;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Payment {

  @SerializedName("token")
  private final String paymentToken;
  @SerializedName("code")
  private final String discountCode;

  public Payment(String paymentToken, String discountCode) {
    this.paymentToken = paymentToken;
    this.discountCode = discountCode;
  }

  public String getPaymentToken() {
    return paymentToken;
  }

  public String getDiscountCode() {
    return discountCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Payment payment = (Payment) o;
    return Objects.equals(paymentToken, payment.paymentToken)
        && Objects.equals(discountCode, payment.discountCode);
  }

  @Override
  public int hashCode() {

    return Objects.hash(paymentToken, discountCode);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("paymentToken", paymentToken)
        .add("discountCode", discountCode)
        .toString();
  }
}
