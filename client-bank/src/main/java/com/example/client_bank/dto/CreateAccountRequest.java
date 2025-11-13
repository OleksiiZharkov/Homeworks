package com.example.client_bank.dto;

import com.example.client_bank.model.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequest {
    @NotNull(message = "Валюта не може бути null")
    private Currency currency;
}