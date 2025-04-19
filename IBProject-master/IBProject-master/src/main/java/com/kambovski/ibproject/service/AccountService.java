package com.kambovski.ibproject.service;

import com.kambovski.ibproject.model.Account;
import com.kambovski.ibproject.model.User;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account);

    // Метод за преземање на сите сметки на одреден корисник
    List<Account> findAllByUser(User user);

    // Метод за преземање на сите сметки во базата
    List<Account> findAll();

    Account getAccount(Long accountId);
}
