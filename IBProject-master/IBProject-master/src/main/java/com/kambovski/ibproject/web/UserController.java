package com.kambovski.ibproject.web;

import com.kambovski.ibproject.model.User;
import com.kambovski.ibproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{embg}")
    public User getUserByEmbg(@PathVariable String embg) {
        return userService.findUserByEmbg(embg);
    }

    
}
