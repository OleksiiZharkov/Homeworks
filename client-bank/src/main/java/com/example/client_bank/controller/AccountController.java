package com.example.client_bank.controller;

import com.example.client_bank.dto.AmountRequest;
import com.example.client_bank.dto.TransferRequest;
import com.example.client_bank.model.Account;
import com.example.client_bank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Account deposit(@PathVariable String accountNumber,
                           @Valid @RequestBody AmountRequest request) {
        return accountService.deposit(accountNumber, request.getAmount());
    }

    @PostMapping("/withdraw/{accountNumber}")
    public Account withdraw(@PathVariable String accountNumber,
                            @Valid @RequestBody AmountRequest request) {
        return accountService.withdraw(accountNumber, request.getAmount());
    }

    @PostMapping("/transfer")
    public void transfer(@Valid @RequestBody TransferRequest request) {
        accountService.transfer(
                request.getFromAccountNumber(),
                request.getToAccountNumber(),
                request.getAmount()
        );
    }
}