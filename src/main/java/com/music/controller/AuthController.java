package com.music.controller;

import com.music.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {model.addAttribute("error", "Invalid username or password!");}
        return "login";
    }
    @RequestMapping("/register")
    public String register(@RequestParam(value = "error", required = false) String error,Model model) {
        if (error != null) {model.addAttribute("error", "Couldn't register a user!");}
        model.addAttribute("user", new UserDto());
        return "register";
    }
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {new SecurityContextLogoutHandler().logout(request, response, auth);}
        return "redirect:/login?logout=true";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto userDto) {
        UserDetails user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles("USER")
                .build();

        try {
            userDetailsManager.createUser(user);
            return "redirect:/login?registration=success";
        } catch (Exception e) {
            return "redirect:/register?error=true";
        }
    }
}
