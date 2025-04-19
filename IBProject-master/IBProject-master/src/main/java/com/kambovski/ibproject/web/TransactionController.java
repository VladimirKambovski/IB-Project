package com.kambovski.ibproject.web;

import com.kambovski.ibproject.model.Account;
import com.kambovski.ibproject.model.Transaction;
import com.kambovski.ibproject.model.User;
import com.kambovski.ibproject.service.TransactionService;
import com.kambovski.ibproject.service.AccountService;
import com.kambovski.ibproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String getTransactionsForUser(Model model, Principal principal) {
        String userId = principal.getName();
        User user = userService.findUserByEmbg(userId);

        // Преземи ги сметките само за тековниот корисник
        List<Account> accounts = accountService.findAllByUser(user);

        if (accounts.isEmpty()) {
            model.addAttribute("errorMessage", "No accounts found for this user.");
            return "error";  // Можеш да прикажеш грешка ако нема сметки
        }

        Account account = accounts.get(0);  // Преземи ја првата сметка
        List<Transaction> transactions = transactionService.findByAccount(account.getId());
        model.addAttribute("transactions", transactions);

        return "transactions";  // Thymeleaf страница за трансакции
    }
}
