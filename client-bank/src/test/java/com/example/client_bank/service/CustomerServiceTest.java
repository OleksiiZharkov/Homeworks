package com.example.client_bank.service;

import com.example.client_bank.exception.ResourceNotFoundException;
import com.example.client_bank.model.Customer;
import com.example.client_bank.repository.AccountRepository;
import com.example.client_bank.repository.CustomerRepository;
import com.example.client_bank.repository.EmployerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EmployerRepository employerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getCustomerById_Success() {
        Customer customer = new Customer("Test", "test@test.com", 25);
        customer.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Customer found = customerService.getCustomerById(1L);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test");
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerById(1L);
        });
    }

    @Test
    void getAllCustomers_ReturnsPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        List<Customer> customers = List.of(new Customer("Test", "test@test.com", 25));
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, 1);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<Customer> result = customerService.getAllCustomers(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
    }
}