package com.constanm.coding.challenges.homebank.bank.service;

import com.constanm.coding.challenges.homebank.bank.rates.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ExchangeRatesService {

  private static final String EXCHANGE_SERVICE_URL = "https://api.exchangeratesapi.io/latest";
  private RestTemplate restTemplate;

  @Autowired
  public ExchangeRatesService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @CachePut(value = "exchangeRates")
  public ExchangeRate retrieveLatestExchangeRate() {
    log.info("Calling Exchange Rates API directly and then adding to cache..");
    return this.restTemplate.getForObject(EXCHANGE_SERVICE_URL, ExchangeRate.class);
  }
}
