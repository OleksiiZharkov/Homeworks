package com.example.client_bank.controller;

import com.example.client_bank.dto.CreateAccountRequest;
import com.example.client_bank.dto.CustomerRequestDto;
import com.example.client_bank.dto.CustomerResponseDto;
import com.example.client_bank.facade.CustomerFacade;
import com.example.client_bank.model.Account;
import com.example.client_bank.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.client_bank.model.Customer;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerFacade customerFacade;
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerFacade customerFacade, CustomerService customerService) {
        this.customerFacade = customerFacade;
        this.customerService = customerService;
    }

    @GetMapping
    public Page<CustomerResponseDto> getAllCustomers(
            @PageableDefault(size = 10) Pageable pageable) {
        return customerFacade.getAll(pageable);
    }

    @GetMapping("/{id}")
    public CustomerResponseDto getCustomerById(@PathVariable Long id) {
        return customerFacade.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDto createCustomer(
            @Valid @RequestBody CustomerRequestDto requestDto) {
        return customerFacade.create(requestDto);
    }

    @PutMapping("/{id}")
    public CustomerResponseDto updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDto requestDto) {
        return customerFacade.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long id) {
        customerFacade.deleteById(id);
    }
    @PostMapping("/{customerId}/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(
            @PathVariable Long customerId,
            @Valid @RequestBody CreateAccountRequest request) {
        return customerService.createAccount(customerId, request.getCurrency());
    }

    @DeleteMapping("/{customerId}/accounts/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long customerId, @PathVariable Long accountId) {
        customerService.deleteAccount(customerId, accountId);
    }
    @PostMapping("/{customerId}/employers/{employerId}")
    public CustomerResponseDto addEmployerToCustomer(@PathVariable Long customerId,
                                                     @PathVariable Long employerId) {
        Customer customer = customerService.addEmployerToCustomer(customerId, employerId);
        return customerFacade.getById(customerId);
    }

    @DeleteMapping("/{customerId}/employers/{employerId}")
    public CustomerResponseDto removeEmployerFromCustomer(@PathVariable Long customerId,
                                                          @PathVariable Long employerId) {
        Customer customer = customerService.removeEmployerFromCustomer(customerId, employerId);
        return customerFacade.getById(customerId);
    }
}