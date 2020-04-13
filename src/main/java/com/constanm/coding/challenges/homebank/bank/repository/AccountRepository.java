package com.constanm.coding.challenges.homebank.bank.repository;

import com.constanm.coding.challenges.homebank.bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

  Account findByIban(String iban);
}
