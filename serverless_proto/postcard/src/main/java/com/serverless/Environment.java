package com.serverless;

public interface Environment {

  String getStripeSecretKey();

  String getLobSecretKey();

  String getLobApiVersion();

  Integer getPostcardPrice();

  String getPostcardCurrancy();

  String getDiscountCode();

  String getBucketName();
}
