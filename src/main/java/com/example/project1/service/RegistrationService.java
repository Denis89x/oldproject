package com.example.project1.service;

import com.example.project1.model.Account;
import com.example.project1.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public RegistrationService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole("ROLE_USER");

        System.out.println(account.getUsername() + "| " + account.getPassword());
        accountRepository.save(account);
    }
}
