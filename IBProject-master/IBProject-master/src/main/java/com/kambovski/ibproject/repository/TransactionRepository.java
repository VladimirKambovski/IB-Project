package com.kambovski.ibproject.repository;

import com.kambovski.ibproject.model.Transaction;
import com.kambovski.ibproject.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(Account account); // Find all transactions for a specific account
}
