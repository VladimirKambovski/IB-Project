package com.kambovski.ibproject.web;

import com.kambovski.ibproject.model.User;
import com.kambovski.ibproject.model.UserType;
import com.kambovski.ibproject.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String Index(Model model) throws Exception {
        // Преземање на EMBG од сертификатот
        String embg = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByEmbg(embg);

        // Додавање на улогата на корисникот во моделот
        model.addAttribute("userType", user.getUserType());

        // Прикажување на "home" за сите корисници со различни линкови според улогата
        return "home";
    }
}
