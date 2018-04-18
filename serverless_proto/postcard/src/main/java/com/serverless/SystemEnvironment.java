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
  public Long getPostcardPrice() {
    return Long.valueOf(System.getenv().get("POSTCARD_PRICE"));
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

  @Override
  public String getLobFrontTemplateId() {
    return System.getenv().get("LOB_FRONT_TEMPLATE_ID");
  }

  @Override
  public String getLobBackTemplateId() {
    return System.getenv().get("LOB_BACK_TEMPLATE_ID");
  }
}
