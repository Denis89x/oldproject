package com.example.project1.controller;

import com.example.project1.model.Account;
import com.example.project1.service.AccountDetailsService;
import com.example.project1.service.RegistrationService;
import com.example.project1.util.AccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;

    private final AccountValidator accountValidator;
    private final AccountDetailsService accountDetailsService;

    @Autowired
    public AuthController(RegistrationService registrationService, AccountValidator accountValidator, AccountDetailsService accountDetailsService) {
        this.registrationService = registrationService;
        this.accountValidator = accountValidator;
        this.accountDetailsService = accountDetailsService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Account account) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Account account, BindingResult bindingResult) {
        System.out.println("Password:" + account.getPassword() + '|' + "login: " + account.getUsername());
        accountValidator.validate(account, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        registrationService.register(account);

        return "redirect:/auth/login";
    }
}
