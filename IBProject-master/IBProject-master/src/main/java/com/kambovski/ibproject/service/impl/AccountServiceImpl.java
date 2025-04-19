package com.kambovski.ibproject.service.impl;

import com.kambovski.ibproject.model.Account;
import com.kambovski.ibproject.model.User;
import com.kambovski.ibproject.repository.AccountRepository;
import com.kambovski.ibproject.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // Преземање на сите сметки на одреден корисник
    @Override
    public List<Account> findAllByUser(User user) {
        return accountRepository.findByUser(user);
    }

    // Преземање на сите сметки во базата
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
