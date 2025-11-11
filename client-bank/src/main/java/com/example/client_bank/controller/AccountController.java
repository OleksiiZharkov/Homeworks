package com.example.client_bank.controller;

import com.example.client_bank.dto.AmountRequest;
import com.example.client_bank.dto.TransferRequest;
import com.example.client_bank.model.Account;
import com.example.client_bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit/{accountNumber}")
    public ResponseEntity<Account> deposit(@PathVariable String accountNumber, @RequestBody AmountRequest request) {
        try {
            Account account = accountService.deposit(accountNumber, request.getAmount());
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/withdraw/{accountNumber}")
    public ResponseEntity<Account> withdraw(@PathVariable String accountNumber, @RequestBody AmountRequest request) {
        try {
            Account account = accountService.withdraw(accountNumber, request.getAmount());
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        try {
            accountService.transfer(
                    request.getFromAccountNumber(),
                    request.getToAccountNumber(),
                    request.getAmount()
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}