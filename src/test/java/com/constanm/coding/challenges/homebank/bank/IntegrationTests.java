package com.constanm.coding.challenges.homebank.bank;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.constanm.coding.challenges.homebank.bank.rates.ExchangeRate;
import com.constanm.coding.challenges.homebank.bank.service.ExchangeRatesService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTests {

  private ExchangeRate exchangeRates;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CacheManager cacheManager;
  @MockBean
  private ExchangeRatesService mockExchangeRatesService;

  @BeforeAll
  void setUp() {
    exchangeRates = createExchangeRates();
  }

  @Test
  void accountNotFound() throws Exception {
    mockMvc.perform(get("/accounts/{iban}", "NOT AN IBAN"))
        .andExpect(status().isNotFound());
  }

  @Test
  void accountIsFound() throws Exception {
    when(mockExchangeRatesService.retrieveLatestExchangeRate()).thenReturn(exchangeRates);

    mockMvc.perform(get("/accounts/{iban}", "BBB222"))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  void accountIsFoundAndRatesAreNotCached() throws Exception {
    doReturn(exchangeRates).when(mockExchangeRatesService).retrieveLatestExchangeRate();
    evictAllCaches();

    mockMvc.perform(get("/accounts/{iban}", "BBB222"))
        .andExpect(status().is2xxSuccessful());

    verify(mockExchangeRatesService, times(1)).retrieveLatestExchangeRate();
  }

  @Test
  void accountIsFoundAndRatesAreCached() throws Exception {
    doReturn(exchangeRates).when(mockExchangeRatesService).retrieveLatestExchangeRate();
    evictAllCaches();
    cacheManager.getCache("exchangeRates").put(SimpleKey.EMPTY, exchangeRates);

    mockMvc.perform(get("/accounts/{iban}", "BBB222"))
        .andExpect(status().is2xxSuccessful());

    verify(mockExchangeRatesService, times(0)).retrieveLatestExchangeRate();
  }

  private void evictAllCaches() {
    cacheManager.getCacheNames().stream()
        .map(name -> cacheManager.getCache(name))
        .forEach(Cache::clear);
  }

  private ExchangeRate createExchangeRates() {
    final ExchangeRate exchangeRate = new ExchangeRate();
    Map<String, BigDecimal> rates = new HashMap<>();
    rates.put("MYR", new BigDecimal("4.7268"));
    rates.put("CAD", new BigDecimal("1.5259"));
    exchangeRate.setRates(rates);
    exchangeRate.setBase("EUR");
    exchangeRate.setDate("date");
    return exchangeRate;
  }

}
