package com.example.client_bank.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "accounts")

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, exclude = {"customer"})
@ToString(exclude = {"customer"})
@NoArgsConstructor
public class Account extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    private Double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    public Account(Currency currency, Customer customer) {
        this.currency = currency;
        this.customer = customer;
        this.balance = 0.0;
        this.number = UUID.randomUUID().toString();
    }
}