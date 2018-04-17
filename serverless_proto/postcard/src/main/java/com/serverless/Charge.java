package com.serverless;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.Objects;

public class Charge {

  private final String id;
  private final Long price;
  private final String currency;
  private final String description;

  private static final String EXCEPTION_MESSAGE_TEMPLATE =
      "Argument %s is expected to be not null.";

  /**
   * Construct a charge instance.
   *
   * @param id the charge id
   * @param price the price
   * @param currency the currency
   * @param description the description of the charge
   */
  public Charge(String id, Long price, String currency, String description) {
    this.id = Preconditions.checkNotNull(id, EXCEPTION_MESSAGE_TEMPLATE, "id");
    this.price = Preconditions.checkNotNull(price, EXCEPTION_MESSAGE_TEMPLATE, "price");
    this.currency = Preconditions.checkNotNull(currency, EXCEPTION_MESSAGE_TEMPLATE, "currency");
    this.description = Preconditions.checkNotNull(description, EXCEPTION_MESSAGE_TEMPLATE,
        "description");
  }

  /**
   * Construct a charge instance given id is not known.
   *
   * @param price the price
   * @param currency the currency
   * @param description the description of the charge
   */
  public Charge(Long price, String currency, String description) {
    this("", price, currency, description);
  }

  public String getId() {
    return id;
  }

  public Long getPrice() {
    return price;
  }

  public String getCurrency() {
    return currency;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Charge charge = (Charge) o;
    return Objects.equals(id, charge.id)
        && Objects.equals(price, charge.price)
        && Objects.equals(currency, charge.currency)
        && Objects.equals(description, charge.description);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, price, currency, description);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .toString();
  }
}
