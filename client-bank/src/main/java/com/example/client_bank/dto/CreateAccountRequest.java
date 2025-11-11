package com.example.client_bank.dto;

import com.example.client_bank.model.Currency;

public class CreateAccountRequest {
    private Currency currency;

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }
}