package com.springboot.messaging.controller;

import com.springboot.messaging.Entity.User;
import com.springboot.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        userService.registerNewUser(user);
        return "redirect:/login";
    }

}
