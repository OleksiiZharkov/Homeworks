package com.example.client_bank.controller;

import com.example.client_bank.dto.CreateAccountRequest;
import com.example.client_bank.dto.CreateCustomerRequest;
import com.example.client_bank.model.Account;
import com.example.client_bank.model.Customer;
import com.example.client_bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Customer createCustomer(@RequestBody CreateCustomerRequest request) {
        Customer customer = new Customer(request.getName(), request.getEmail(), request.getAge());
        return customerService.createCustomer(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody CreateCustomerRequest request) {
        try {
            Customer customerStub = new Customer(request.getName(), request.getEmail(), request.getAge());
            Customer updatedCustomer = customerService.updateCustomer(id, customerStub);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{customerId}/accounts")
    public ResponseEntity<Account> createAccount(@PathVariable Long customerId, @RequestBody CreateAccountRequest request) {
        try {
            Account account = customerService.createAccount(customerId, request.getCurrency());
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{customerId}/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long customerId, @PathVariable Long accountId) {
        try {
            customerService.deleteAccount(customerId, accountId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/{customerId}/employers/{employerId}")
    public ResponseEntity<Customer> addEmployerToCustomer(@PathVariable Long customerId,
                                                          @PathVariable Long employerId) {
        try {
            Customer customer = customerService.addEmployerToCustomer(customerId, employerId);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{customerId}/employers/{employerId}")
    public ResponseEntity<Customer> removeEmployerFromCustomer(@PathVariable Long customerId,
                                                               @PathVariable Long employerId) {
        try {
            Customer customer = customerService.removeEmployerFromCustomer(customerId, employerId);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}