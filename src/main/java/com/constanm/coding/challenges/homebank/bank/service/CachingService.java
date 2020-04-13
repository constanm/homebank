package com.constanm.coding.challenges.homebank.bank.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.cache.interceptor.SimpleKey.EMPTY;

import com.constanm.coding.challenges.homebank.bank.exception.ExchangeRatesNotCachedException;
import com.constanm.coding.challenges.homebank.bank.rates.ExchangeRate;
import com.google.common.base.Preconditions;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CachingService {

  private ExchangeRatesService exchangeRatesService;
  private CacheManager cacheManager;

  @Autowired
  public CachingService(ExchangeRatesService exchangeRatesService,
      CacheManager cacheManager) {
    this.exchangeRatesService = exchangeRatesService;
    this.cacheManager = cacheManager;
  }

  @HystrixCommand(fallbackMethod = "callExchangeRatesApiFallback")
  public ExchangeRate retrieveCachedExchangeRates() {
    ExchangeRate exchangeRate = readCachedExchangeRates();
    Preconditions.checkNotNull(exchangeRate, new ExchangeRatesNotCachedException());
    return exchangeRate;
  }

  @SuppressWarnings("unused")
  ExchangeRate callExchangeRatesApiFallback() {
    log.info("Missing from cache, need to call Exchange Rates API..");
    return exchangeRatesService.retrieveLatestExchangeRate();
  }

  private ExchangeRate readCachedExchangeRates() {
    return cacheManager.getCache("exchangeRates").get(EMPTY, ExchangeRate.class);
  }
}
