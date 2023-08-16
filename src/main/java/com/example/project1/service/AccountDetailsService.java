package com.example.project1.service;

import com.example.project1.model.Account;
import com.example.project1.repository.AccountRepository;
import com.example.project1.security.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername(s);

        if (account.isEmpty())
            throw new UsernameNotFoundException("User not found");

        System.out.println(account.get().getUsername());
        System.out.println(account.get().getPassword());

        return new AccountDetails(account.get());
    }


}
