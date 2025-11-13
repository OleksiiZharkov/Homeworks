package com.example.client_bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, exclude = {"accounts", "employers"})
@ToString(exclude = {"accounts", "employers"})
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private Integer age;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Account> accounts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "customer_employer",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "employer_id")
    )
    @JsonManagedReference
    private Set<Employer> employers = new HashSet<>();

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
        account.setCustomer(this);
    }

    public void addEmployer(Employer employer) {
        this.employers.add(employer);
        employer.getCustomers().add(this);
    }
}