package com.constanm.coding.challenges.homebank.bank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.constanm.coding.challenges.homebank.bank.model.EuroAccountDto;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EuroAccountDtoTest {

  private EuroAccountDto euroAccountDtoUnderTest;

  @BeforeEach
  void setUp() {
    euroAccountDtoUnderTest = new EuroAccountDto("iban", new BigDecimal("0.00"), "currency",
                                                 new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());
  }

  @Test
  void testToString() {
    // Setup

    // Run the test
    final String result = euroAccountDtoUnderTest.toString();

    // Verify the results
    assertEquals("EuroAccountDto(iban=iban, balance=0.00, currency=currency, lastModified=Tue Jan 01 00:00:00 EET 2019)",
                 result);
  }

  @Test
  void testEquals() {
    // Setup
    EuroAccountDto expected = new EuroAccountDto("iban", new BigDecimal("0.00"), "currency",
                                                 new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());

    // Run the test
    final boolean result = euroAccountDtoUnderTest.equals(expected);

    // Verify the results
    assertTrue(result);
  }

  @Test
  void testHashCode() {
    // Setup

    // Run the test
    final int result = euroAccountDtoUnderTest.hashCode();

    // Verify the results
    assertEquals(700671368, result);
  }
}
