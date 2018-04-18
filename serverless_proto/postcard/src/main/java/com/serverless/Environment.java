package com.serverless;

public interface Environment {

  String getStripeSecretKey();

  String getLobSecretKey();

  String getLobApiVersion();

  Long getPostcardPrice();

  String getPostcardCurrancy();

  String getDiscountCode();

  String getBucketName();

  String getLobFrontTemplateId();

  String getLobBackTemplateId();
}
