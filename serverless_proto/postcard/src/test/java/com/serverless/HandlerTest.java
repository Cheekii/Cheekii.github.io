package com.serverless;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.junitextensions.MockitoExtension;
import com.lob.Lob;
import com.stripe.Stripe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    when(environment.getPostcardPrice()).thenReturn(1000);
    when(environment.getStripeSecretKey()).thenReturn("STRIPE-SECRET-KEY");
  }

  @Test
  void testConstructorInitializesKeys(@Mock Environment environment,
      @Mock BillingService billingService) {
    Handler handler = new Handler(environment, billingService);
    assertEquals(Lob.apiKey, "LOB-SECRET-KEY");
    assertEquals(Stripe.apiKey, "STRIPE-SECRET-KEY");
  }

}