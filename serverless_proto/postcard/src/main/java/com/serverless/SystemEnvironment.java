package com.serverless;

public class SystemEnvironment implements Environment {
  @Override
  public String getStripeSecretKey() {
    return System.getenv().get("STRIPE_SECRET_KEY");
  }

  @Override
  public String getLobSecretKey() {
    return System.getenv().get("LOB_SECRET_KEY");
  }

  @Override
  public String getLobApiVersion() {
    return System.getenv().get("LOB_API_VERSION");
  }

  @Override
  public Integer getPostcardPrice() {
    return Integer.valueOf(System.getenv().get("POSTCARD_PRICE"));
  }

  @Override
  public String getPostcardCurrancy() {
    return System.getenv().get("POSTCARD_CURRANCY");
  }

  @Override
  public String getDiscountCode() {
    return System.getenv().get("DISCOUNT_CODE");
  }

  @Override
  public String getBucketName() {
    return System.getenv().get("BUCKET");
  }
}
