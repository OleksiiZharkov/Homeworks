package com.example.client_bank.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employers")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employer extends AbstractEntity {

    private String name;
    private String address;

    @ManyToMany(mappedBy = "employers")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Customer> customers = new HashSet<>();
}