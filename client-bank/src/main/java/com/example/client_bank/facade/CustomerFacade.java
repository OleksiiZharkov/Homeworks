package com.example.client_bank.facade;

import com.example.client_bank.dto.CustomerRequestDto;
import com.example.client_bank.dto.CustomerResponseDto;
import com.example.client_bank.model.Customer;
import com.example.client_bank.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class CustomerFacade {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerFacade(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    public CustomerResponseDto create(CustomerRequestDto requestDto) {

        Customer customer = modelMapper.map(requestDto, Customer.class);

        Customer savedCustomer = customerService.createCustomer(customer);

        return modelMapper.map(savedCustomer, CustomerResponseDto.class);
    }

    public CustomerResponseDto update(Long id, CustomerRequestDto requestDto) {
        Customer customer = modelMapper.map(requestDto, Customer.class);
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return modelMapper.map(updatedCustomer, CustomerResponseDto.class);
    }

    public CustomerResponseDto getById(Long id) {
        Customer customer = customerService.getCustomerById(id);
        return modelMapper.map(customer, CustomerResponseDto.class);
    }

    public void deleteById(Long id) {
        customerService.deleteCustomer(id);
    }

    public Page<CustomerResponseDto> getAll(Pageable pageable) {
        Page<Customer> customerPage = customerService.getAllCustomers(pageable);
        return customerPage.map(customer -> modelMapper.map(customer, CustomerResponseDto.class));
    }
}