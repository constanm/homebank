package com.constanm.coding.challenges.homebank.bank.service;

import static com.constanm.coding.challenges.homebank.bank.util.Utils.validateCurrency;
import static java.math.BigDecimal.ROUND_HALF_UP;

import com.constanm.coding.challenges.homebank.bank.entity.Account;
import com.constanm.coding.challenges.homebank.bank.exception.AccountNotFoundException;
import com.constanm.coding.challenges.homebank.bank.model.EuroAccountDto;
import com.constanm.coding.challenges.homebank.bank.rates.ExchangeRate;
import com.constanm.coding.challenges.homebank.bank.repository.AccountRepository;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountsService {

  private AccountRepository accountRepository;
  private CachingService cachingService;

  @Autowired
  public AccountsService(
      AccountRepository accountRepository,
      CachingService cachingService) {
    this.accountRepository = accountRepository;
    this.cachingService = cachingService;
  }

  public EuroAccountDto toEuroAccount(String iban) {
    Account account = findAccountByIban(iban);
    ExchangeRate exchangeRates = cachingService.retrieveCachedExchangeRates();
    validateCurrency(account.getCurrency(), exchangeRates);
    BigDecimal conversionRate = new BigDecimal(exchangeRates.getRates().get(account.getCurrency()).toString());
    BigDecimal balanceInEuro =  account.getBalance().divide(conversionRate, ROUND_HALF_UP);
    log.info("Calculated balance for IBAN {} to {} EUR", account.getIban(), balanceInEuro);
    return new EuroAccountDto(account.getIban(), balanceInEuro, "EUR", account.getLastUpdated());
  }

  private Account findAccountByIban(String iban) {
    Account account = accountRepository.findByIban(iban);
    if (null == account) {
      throw new AccountNotFoundException("Cannot find account with IBAN=" + iban);
    }
    log.info("Found account with IBAN {}", iban);
    return account;
  }

}
