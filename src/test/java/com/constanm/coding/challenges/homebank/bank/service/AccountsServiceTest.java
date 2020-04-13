package com.constanm.coding.challenges.homebank.bank.service;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.constanm.coding.challenges.homebank.bank.entity.Account;
import com.constanm.coding.challenges.homebank.bank.entity.User;
import com.constanm.coding.challenges.homebank.bank.exception.AccountNotFoundException;
import com.constanm.coding.challenges.homebank.bank.model.EuroAccountDto;
import com.constanm.coding.challenges.homebank.bank.rates.ExchangeRate;
import com.constanm.coding.challenges.homebank.bank.repository.AccountRepository;
import com.constanm.coding.challenges.homebank.bank.service.AccountsService;
import com.constanm.coding.challenges.homebank.bank.service.CachingService;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class AccountsServiceTest {

  @Mock
  private AccountRepository mockAccountRepository;
  @Mock
  private CachingService mockCachingService;

  private AccountsService accountsServiceUnderTest;

  @BeforeEach
  void setUp() {
    initMocks(this);
    accountsServiceUnderTest = new AccountsService(mockAccountRepository, mockCachingService);
  }

  @Test
  void accountNotFound() {
    // Setup
    when(mockAccountRepository.findByIban("iban")).thenReturn(null);

    // Run the test
    Exception exception = assertThrows(
        AccountNotFoundException.class,
        () -> accountsServiceUnderTest.toEuroAccount("iban"));

    // Verify the results
    verify(mockAccountRepository).findByIban("iban");
    assertTrue(exception.getMessage().contains("Cannot find account with IBAN=iban"));
  }

  @Test
  void accountFound() {
    // Setup
    final EuroAccountDto expectedResult = new EuroAccountDto("iban", BigDecimal.ONE, "EUR", createDate());
    when(mockAccountRepository.findByIban("iban")).thenReturn(createAccount());
    final ExchangeRate exchangeRate = createExchangeRates();
    when(mockCachingService.retrieveCachedExchangeRates()).thenReturn(exchangeRate);

    // Run the test
    final EuroAccountDto result = accountsServiceUnderTest.toEuroAccount("iban");

    // Verify the results
    verify(mockAccountRepository).findByIban("iban");
    verify(mockCachingService).retrieveCachedExchangeRates();
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

  private Date createDate() {
    return new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime();
  }

  private Account createAccount(User user) {
    return new Account(user, "iban", "currency", new BigDecimal("2"),
                       createDate());
  }

  private User createUser() {
    final User user = new User();
    user.setId(0L);
    user.setName("name");
    user.setBirthDate(createDate());
    user.setAccounts(new HashSet<>(Collections.singletonList(createAccount())));
    return user;
  }

  private Account createAccount() {
    return createAccount(new User());
  }
}
