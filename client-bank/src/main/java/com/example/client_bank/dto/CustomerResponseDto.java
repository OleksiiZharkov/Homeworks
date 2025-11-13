package com.example.client_bank.dto;

import com.example.client_bank.model.Account;
import com.example.client_bank.model.Employer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class CustomerResponseDto {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    private List<Account> accounts;
    private Set<Employer> employers;
}