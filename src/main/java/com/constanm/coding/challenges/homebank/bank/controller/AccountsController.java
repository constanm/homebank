package com.constanm.coding.challenges.homebank.bank.controller;

import com.constanm.coding.challenges.homebank.bank.model.EuroAccountDto;
import com.constanm.coding.challenges.homebank.bank.service.AccountsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/accounts")
@Api(value = "Accounts Operations Controller", description = "Operations pertaining to accounts")
public class AccountsController {

  private AccountsService accountsService;

  @Autowired
  public AccountsController(AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @GetMapping("/{iban}")
  @ApiOperation(value = "View the user's account balance converted in EUR", response = EuroAccountDto.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully retrieved euro balance"),
      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
      @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  public EuroAccountDto retrieveBalanceInEuroFor(@PathVariable String iban) {
    log.info("Received request of account with IBAN {}", iban);
    return accountsService.toEuroAccount(iban);
  }
}