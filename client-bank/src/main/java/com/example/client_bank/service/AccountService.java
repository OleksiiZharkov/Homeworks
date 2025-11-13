package com.example.client_bank.service;

import com.example.client_bank.dto.AccountUpdateNotification;
import com.example.client_bank.model.Account;
import com.example.client_bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          SimpMessagingTemplate messagingTemplate) {
        this.accountRepository = accountRepository;
        this.messagingTemplate = messagingTemplate;
    }
    @Transactional
    public Account deposit(String accountNumber, Double amount) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        account.setBalance(account.getBalance() + amount);
        Account savedAccount = accountRepository.save(account);
        sendAccountUpdate(savedAccount);
        return savedAccount;
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
        Account savedAccount = accountRepository.save(account);
        sendAccountUpdate(savedAccount);
        return savedAccount;
    }
    @Transactional
    public void transfer(String fromAccountNum, String toAccountNum, Double amount) {
        Account fromAccount = withdraw(fromAccountNum, amount);
        try {
            deposit(toAccountNum, amount);
        } catch (Exception e) {
            fromAccount.setBalance(fromAccount.getBalance() + amount);
            Account rolledBackAccount = accountRepository.save(fromAccount);
            sendAccountUpdate(rolledBackAccount);
            throw new RuntimeException("Transfer failed. Money returned to sender.", e);
        }
    }
    private void sendAccountUpdate(Account account) {
        AccountUpdateNotification notification = new AccountUpdateNotification(
                account.getNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getCustomer().getId()
        );
        messagingTemplate.convertAndSend("/topic/accounts", notification);
    }
}