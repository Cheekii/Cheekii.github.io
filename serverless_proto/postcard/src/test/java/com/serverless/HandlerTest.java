package com.serverless;

import static org.mockito.Mockito.when;

import com.junitextensions.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class HandlerTest {

  @BeforeEach
  void setUp(@Mock Environment environment, @Mock BillingService billingService) {
    when(environment.getBucketName()).thenReturn("postcard-bucket");
    when(environment.getDiscountCode()).thenReturn("DISCOUNT-CODE");
    when(environment.getLobApiVersion()).thenReturn("2017-11-08");
    when(environment.getLobSecretKey()).thenReturn("LOB-SECRET-KEY");
    when(environment.getPostcardCurrancy()).thenReturn("CAD");
    when(environment.getPostcardPrice()).thenReturn(1000L);
    when(environment.getStripeSecretKey()).thenReturn("STRIPE-SECRET-KEY");
  }

}