package com.example.client_bank.dto;

import com.example.client_bank.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateNotification {
    private String accountNumber;
    private Double newBalance;
    private Currency currency;
    private Long customerId;
}