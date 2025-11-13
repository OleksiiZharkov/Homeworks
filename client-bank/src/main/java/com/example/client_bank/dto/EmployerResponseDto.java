package com.example.client_bank.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmployerResponseDto {
    private Long id;
    private String name;
    private String address;
    private LocalDateTime createdDate;
}