package com.music.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }
    @RequestMapping("/register")
    public String register(Model model) {
        return "register";
    }

}
