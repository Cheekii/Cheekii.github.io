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
