package com.serverless;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import java.util.UUID;

public class Order {
  private final PostCard postCard;
  private final Payment payment;
  private final UUID orderId;
  private final Charge charge;
  private final Job job;

  /**
   * Constructs an order instance.
   * @param postCard the postcard.
   * @param payment the payment info.
   * @param orderId the orderId.
   * @param charge the charge.
   */
  public Order(PostCard postCard, Payment payment, UUID orderId, Charge charge, Job job) {
    this.postCard = postCard;
    this.payment = payment;
    this.orderId = orderId;
    this.charge = charge;
    this.job = job;
  }

  public PostCard getPostCard() {
    return postCard;
  }

  public Payment getPayment() {
    return payment;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public Charge getCharge() {
    return charge;
  }

  public Job getJob() {
    return job;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(postCard, order.postCard)
        && Objects.equals(payment, order.payment)
        && Objects.equals(orderId, order.orderId)
        && Objects.equals(charge, order.charge)
        && Objects.equals(job, order.job);
  }

  @Override
  public int hashCode() {

    return Objects.hash(postCard, payment, orderId, charge, job);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("postCard", postCard)
        .add("payment", payment)
        .add("orderId", orderId)
        .add("charge", charge)
        .add("job", job)
        .toString();
  }
}
