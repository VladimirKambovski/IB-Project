package com.kambovski.ibproject.repository;

import com.kambovski.ibproject.model.Account;
import com.kambovski.ibproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user); // Find all accounts of a specific user
}
