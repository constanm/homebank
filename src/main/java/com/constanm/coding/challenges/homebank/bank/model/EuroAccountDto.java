package com.constanm.coding.challenges.homebank.bank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
public class EuroAccountDto {

  private String iban;
  private BigDecimal balance;
  private String currency;
  @JsonFormat(pattern = "dd-MM-YYYY HH:mm:ss")
  private Date lastModified;
}
