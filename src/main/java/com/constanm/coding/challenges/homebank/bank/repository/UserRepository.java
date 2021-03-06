package com.constanm.coding.challenges.homebank.bank.repository;

import com.constanm.coding.challenges.homebank.bank.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  List<User> findByName(String name);
}