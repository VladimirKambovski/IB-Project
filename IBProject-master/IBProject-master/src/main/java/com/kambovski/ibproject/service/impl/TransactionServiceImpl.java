package com.kambovski.ibproject.service.impl;

import com.kambovski.ibproject.model.Transaction;
import com.kambovski.ibproject.model.Account;
import com.kambovski.ibproject.repository.TransactionRepository;
import com.kambovski.ibproject.repository.AccountRepository;
import com.kambovski.ibproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccount(Long accountId) {
        // Наоѓаме една сметка според ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Преземање на сите трансакции за таа сметка
        return transactionRepository.findByAccount(account);
    }

}
