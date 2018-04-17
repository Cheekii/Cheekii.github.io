package com.serverless;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

import com.junitextensions.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {
  private BillingService billingService;

  @BeforeEach
  void setUp(@Mock CreditCardProcessor mockCrediCardProcessor) {
    billingService = new BillingService(mockCrediCardProcessor);
  }

  @Test
  void testChargeOrder() {

  }
}