package com.constanm.coding.challenges.homebank.bank.service;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.constanm.coding.challenges.homebank.bank.rates.ExchangeRate;
import com.constanm.coding.challenges.homebank.bank.service.CachingService;
import com.constanm.coding.challenges.homebank.bank.service.ExchangeRatesService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.SimpleKey;

class CachingServiceTest {

  @Mock
  private ExchangeRatesService mockExchangeRatesService;
  @Mock
  private CacheManager mockCacheManager;

  private CachingService cachingServiceUnderTest;

  @BeforeEach
  void setUp() {
    initMocks(this);
    cachingServiceUnderTest = new CachingService(mockExchangeRatesService, mockCacheManager);
  }

  @Test
  void testRetrieveCachedExchangeRates_missingCache() {
    // Setup
    ConcurrentMapCache emptyConcurrentMapCache = new ConcurrentMapCache("exchangeRates");

    when(mockCacheManager.getCache("exchangeRates")).thenReturn(emptyConcurrentMapCache);

    // Run the test
    Exception exception = assertThrows(
        NullPointerException.class,
        () -> cachingServiceUnderTest.retrieveCachedExchangeRates());

    // Verify the results
    verify(mockCacheManager).getCache("exchangeRates");
    assertTrue(exception.getMessage().contains("ExchangeRatesNotCachedException"));
  }

  @Test
  void testRetrieveCachedExchangeRates_presentInCache() {
    // Setup
    final ExchangeRate expectedResult = createExchangeRates();
    final ExchangeRate exchangeRateInCache = createExchangeRates();
    ConcurrentMapCache concurrentMapCache = new ConcurrentMapCache("exchangeRates");
    concurrentMapCache.put(SimpleKey.EMPTY, exchangeRateInCache);

    when(mockCacheManager.getCache("exchangeRates")).thenReturn(concurrentMapCache);

    // Run the test
    final ExchangeRate result = cachingServiceUnderTest.retrieveCachedExchangeRates();

    // Verify the results
    verify(mockCacheManager).getCache("exchangeRates");
    assertEquals(expectedResult, result);
  }

  private ExchangeRate createExchangeRates() {
    final ExchangeRate exchangeRate = new ExchangeRate();
    Map<String, BigDecimal> rates = new HashMap<>();
    rates.put("currency", new BigDecimal("2"));
    exchangeRate.setRates(rates);
    exchangeRate.setBase("base");
    exchangeRate.setDate("date");
    return exchangeRate;
  }

}
