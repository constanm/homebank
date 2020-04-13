package com.constanm.coding.challenges.homebank.bank.rates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate implements Serializable {

  private Map<String, BigDecimal> rates;
  private String base;
  private String date;

}
