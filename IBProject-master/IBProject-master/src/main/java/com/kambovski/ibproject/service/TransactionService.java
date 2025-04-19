package com.kambovski.ibproject.service;

import com.kambovski.ibproject.model.Transaction;
import com.kambovski.ibproject.model.Account;

import java.util.List;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    List<Transaction> findByAccount(Long accountId);

    
}
