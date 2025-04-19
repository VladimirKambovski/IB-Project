package com.kambovski.ibproject.web;

import com.kambovski.ibproject.model.Account;
import com.kambovski.ibproject.model.User;
import com.kambovski.ibproject.service.AccountService;
import com.kambovski.ibproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;  // Импортирање на BigDecimal
import java.security.Principal;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    // Преглед на балансот на тековната сметка
    @GetMapping("/details")
    public String getAccountDetails(Model model, Principal principal) {
        String userId = principal.getName();  // Преземање на EMBG од сертификатот
        User user = userService.findUserByEmbg(userId);

        // Преземање на сметката на корисникот
        Account account = accountService.findAllByUser(user).get(0);  // Претпоставуваме дека има една сметка

        model.addAttribute("account", account);
        return "account_details";  // Thymeleaf страница за детали за сметката
    }

    // Обработка на Withdraw операцијата
    @PostMapping("/withdraw")
    public String withdrawFromAccount(@ModelAttribute Account account, double amount) {
        BigDecimal withdrawalAmount = BigDecimal.valueOf(amount);  // Конвертирање на double во BigDecimal
        account.setBalance(account.getBalance().subtract(withdrawalAmount));  // Намалување на балансот
        accountService.createAccount(account);  // Зачувување на промената
        return "redirect:/account/details";
    }

    // Обработка на Deposit операцијата
    @PostMapping("/deposit")
    public String depositToAccount(@ModelAttribute Account account, double amount) {
        BigDecimal depositAmount = BigDecimal.valueOf(amount);  // Конвертирање на double во BigDecimal
        account.setBalance(account.getBalance().add(depositAmount));  // Зголемување на балансот
        accountService.createAccount(account);  // Зачувување на промената
        return "redirect:/account/details";
    }
}
