package com.constanm.coding.challenges.homebank.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;

  @Size(min = 2, message = "IBAN should have at least 2 characters")
  @ApiModelProperty(notes = "IBAN should have at least 2 characters")
  private String iban;

  @Size(min = 2, message = "Currency should have at least 2 characters")
  @ApiModelProperty(notes = "Currency should have at least 2 characters")
  private String currency;

  @PositiveOrZero(message = "Balance should be positive")
  @ApiModelProperty(notes = "Balance should be positive")
  private BigDecimal balance;

  @Past
  @ApiModelProperty(notes = "Last update date should be in the past")
  private Date lastUpdated;

  public Account(User user,
      @Size(min = 2, message = "IBAN should have at least 2 characters") String iban,
      @Size(min = 2, message = "Currency should have at least 2 characters") String currency,
      @PositiveOrZero(message = "Balance should be positive") BigDecimal balance,
      @Past Date lastUpdated) {
    super();
    this.user = user;
    this.iban = iban;
    this.currency = currency;
    this.balance = balance;
    this.lastUpdated = lastUpdated;
  }

}
