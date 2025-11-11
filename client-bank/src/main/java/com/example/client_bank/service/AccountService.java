package com.example.client_bank.service;

import com.example.client_bank.model.Account;
import com.example.client_bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account deposit(String accountNumber, Double amount) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(String accountNumber, Double amount) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(String fromAccountNum, String toAccountNum, Double amount) {
        withdraw(fromAccountNum, amount);
        deposit(toAccountNum, amount);
    }
}