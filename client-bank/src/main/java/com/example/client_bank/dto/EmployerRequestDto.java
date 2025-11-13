package com.example.client_bank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployerRequestDto {
    @NotNull
    @Size(min = 3, message = "Назва компанії має містити щонайменше 3 символи")
    private String name;

    @NotNull
    @Size(min = 3, message = "Адреса компанії повинна містити щонайменше 3 символи")
    private String address;
}