package com.constanm.coding.challenges.homebank.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknownCurrencyException extends RuntimeException {

  public UnknownCurrencyException(String message) {
    super(message);
  }
}
