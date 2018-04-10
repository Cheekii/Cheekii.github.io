package com.serverless;

public class Address {

  private String name;
  private String line1;
  private String city;
  private String state;
  private String zip;
  private String country;
  private String phone;
  private String email;

  /**
   * Constructs and address object.
   * @param name the name of the person
   * @param line1 line 1 of the address
   * @param city the city
   * @param state the state/province
   * @param zip the postal/zip code
   * @param country the country
   * @param phone the phone number
   * @param email the email
   */
  public Address(String name, String line1, String city, String state, String zip,
      String country, String phone, String email) {
    this.name = name;
    this.line1 = line1;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.country = country;
    this.phone = phone;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getLine1() {
    return line1;
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

  public String getPhone() {
    return phone;
  }

  public String getEmail() {
    return email;
  }
}
