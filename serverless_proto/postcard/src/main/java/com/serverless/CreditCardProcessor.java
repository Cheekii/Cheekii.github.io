package com.serverless;

import com.serverless.exceptions.ChargeProcessingException;
import com.serverless.exceptions.RefundChargeException;
import com.serverless.exceptions.UpdateChargeException;

public interface CreditCardProcessor {

  Order charge(Order order) throws ChargeProcessingException;

  void updateWithJob(Order order) throws UpdateChargeException;

  void refund(Order order) throws RefundChargeException;
}
