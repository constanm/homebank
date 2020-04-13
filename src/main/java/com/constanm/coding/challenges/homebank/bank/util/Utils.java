package com.constanm.coding.challenges.homebank.bank.util;

import com.constanm.coding.challenges.homebank.bank.exception.UnknownCurrencyException;
import com.constanm.coding.challenges.homebank.bank.rates.ExchangeRate;

public class Utils {

  public static void validateCurrency(String currency, ExchangeRate exchangeRates) {
    if (!exchangeRates.getRates().containsKey(currency)) {
      throw new UnknownCurrencyException("Unknown currency: " + currency);
    }
  }
}
