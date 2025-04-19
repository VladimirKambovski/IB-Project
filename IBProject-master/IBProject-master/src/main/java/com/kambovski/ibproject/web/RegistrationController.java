package com.kambovski.ibproject.web;

import com.kambovski.ibproject.model.User;
import com.kambovski.ibproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String registerUser(@ModelAttribute User user) {
        System.out.println("RegistrationController: POST /register called");  // Логирај
        userService.createUser(user);
        return "redirect:/login";
    }
}
