package com.serverless;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Address {

  @SerializedName("name")
  private final String name;
  @SerializedName("phone")
  private final String phone;
  @SerializedName("email")
  private final String email;
  @SerializedName("line1")
  private final String line1;
  @SerializedName("line2")
  private final String line2;
  @SerializedName("city")
  private final String city;
  @SerializedName("state")
  private final String state;
  @SerializedName("zip")
  private final String zip;
  @SerializedName("country")
  private final String country;

  /**
   * Constructs an Address instance.
   * @param name the name of the person belonging to the address.
   * @param phone the phone number.
   * @param email the email.
   * @param line1 the 1st address line.
   * @param line2 the 2nd address line.
   * @param city the city.
   * @param state the state.
   * @param zip the zip.
   * @param country the country.
   */
  public Address(final String name, final String phone, final String email,
      final String line1, final String line2, final String city, final String state,
      final String zip, final String country) {
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.line1 = line1;
    this.line2 = line2;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.country = country;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }

  public String getEmail() {
    return email;
  }

  public String getLine1() {
    return line1;
  }

  public String getLine2() {
    return line2;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getZip() {
    return zip;
  }

  public String getCountry() {
    return country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Address address = (Address) o;
    return Objects.equals(name, address.name)
        && Objects.equals(phone, address.phone)
        && Objects.equals(email, address.email)
        && Objects.equals(line1, address.line1)
        && Objects.equals(line2, address.line2)
        && Objects.equals(city, address.city)
        && Objects.equals(state, address.state)
        && Objects.equals(zip, address.zip)
        && Objects.equals(country, address.country);
  }

  @Override
  public int hashCode() {

    return Objects.hash(name, phone, email, line1, line2, city, state, zip, country);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("phone", phone)
        .add("email", email)
        .add("line1", line1)
        .add("line2", line2)
        .add("city", city)
        .add("state", state)
        .add("zip", zip)
        .add("country", country)
        .toString();
  }
}
