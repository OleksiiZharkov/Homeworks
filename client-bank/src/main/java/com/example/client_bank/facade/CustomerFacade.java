package com.example.client_bank.facade;

import com.example.client_bank.dto.CustomerRequestDto;
import com.example.client_bank.dto.CustomerResponseDto;
import com.example.client_bank.model.Customer;
import com.example.client_bank.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
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

    private Customer convertToEntity(CustomerRequestDto requestDto) {
        return modelMapper.map(requestDto, Customer.class);
    }

    private CustomerResponseDto convertToDto(Customer customer) {
        return modelMapper.map(customer, CustomerResponseDto.class);
    }

    public CustomerResponseDto create(CustomerRequestDto requestDto) {
        Customer customer = convertToEntity(requestDto);
        customer.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Customer savedCustomer = customerService.createCustomer(customer);

        return convertToDto(savedCustomer);
    }

    public CustomerResponseDto update(Long id, CustomerRequestDto requestDto) {
        Customer customer = convertToEntity(requestDto);
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return convertToDto(updatedCustomer);
    }

    public CustomerResponseDto getById(Long id) {
        Customer customer = customerService.getCustomerById(id);
        return convertToDto(customer);
    }

    public void deleteById(Long id) {
        customerService.deleteCustomer(id);
    }

    public Page<CustomerResponseDto> getAll(Pageable pageable) {
        Page<Customer> customerPage = customerService.getAllCustomers(pageable);
        return customerPage.map(this::convertToDto);
    }
}