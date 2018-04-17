package com.serverless;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;

class ChargeTest {

  @SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
  @Test
  void testConstructorThrowsNullException() {
    Throwable exception = assertThrows(NullPointerException.class, () -> {
      Charge charge = new Charge(null,100L, "CAD", "Description");
    });
    assertEquals("Argument id is expected to be not null.", exception.getMessage());
  }

  @Test
  void testConstructorWithoutId() {
    Charge charge = new Charge(100L, "CAD", "Description");
    assertAll("Charge constructor without id",
        () -> assertEquals("", charge.getId()),
        () -> assertEquals(Long.valueOf(100), charge.getPrice()),
        () -> assertEquals("CAD", charge.getCurrency()),
        () -> assertEquals("Description", charge.getDescription())
    );
  }
}