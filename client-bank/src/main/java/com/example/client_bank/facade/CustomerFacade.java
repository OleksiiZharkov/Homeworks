package com.example.client_bank.facade;

import com.example.client_bank.dto.CustomerRequestDto;
import com.example.client_bank.dto.CustomerResponseDto;
import com.example.client_bank.model.Customer;
import com.example.client_bank.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerFacade {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerFacade(CustomerService customerService,
                          ModelMapper modelMapper,
                          PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerResponseDto create(CustomerRequestDto requestDto) {
        log.info("Creating customer with email: {}", requestDto.getEmail());
        Customer customer = modelMapper.map(requestDto, Customer.class);
        customer.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Customer savedCustomer = customerService.createCustomer(customer);
        log.info("Customer created with id: {}", savedCustomer.getId());
        return modelMapper.map(savedCustomer, CustomerResponseDto.class);
    }

    public CustomerResponseDto update(Long id, CustomerRequestDto requestDto) {
        log.info("Updating customer with id: {}", id);
        Customer customer = modelMapper.map(requestDto, Customer.class);
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return modelMapper.map(updatedCustomer, CustomerResponseDto.class);
    }

    public CustomerResponseDto getById(Long id) {
        log.info("Fetching customer with id: {}", id);
        Customer customer = customerService.getCustomerById(id);
        return modelMapper.map(customer, CustomerResponseDto.class);
    }

    public void deleteById(Long id) {
        log.info("Deleting customer with id: {}", id);
        customerService.deleteCustomer(id);
    }

    public Page<CustomerResponseDto> getAll(Pageable pageable) {
        log.info("Fetching customer page: {} (size: {})", pageable.getPageNumber(), pageable.getPageSize());
        Page<Customer> customerPage = customerService.getAllCustomers(pageable);
        return customerPage.map(customer -> modelMapper.map(customer, CustomerResponseDto.class));
    }

    private Customer convertToEntity(CustomerRequestDto requestDto) {
        return modelMapper.map(requestDto, Customer.class);
    }
    private CustomerResponseDto convertToDto(Customer customer) {
        return modelMapper.map(customer, CustomerResponseDto.class);
    }
}