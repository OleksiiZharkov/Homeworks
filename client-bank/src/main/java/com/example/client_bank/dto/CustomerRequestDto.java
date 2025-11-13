package com.example.client_bank.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequestDto {

    @NotNull
    @Size(min = 2, message = "Ім'я має складатися щонайменше з 2 символів")
    private String name;

    @NotNull
    @Email(message = "Некоректний формат email")
    private String email;

    @NotNull
    @Min(value = 18, message = "Клієнт повинен бути не молодше 18 років")
    private Integer age;

    @NotNull
    @Pattern(regexp = "^\\+?[0-9\\-\\s()]{10,15}$", message = "Некоректний формат телефону")
    private String phoneNumber;

    @NotNull
    @Size(min = 8, message = "Пароль має бути не менше 8 символів")
    private String password;
}