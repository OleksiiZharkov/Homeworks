package com.example.client_bank.service;

import com.example.client_bank.model.Account;
import com.example.client_bank.model.Currency;
import com.example.client_bank.model.Customer;
import com.example.client_bank.model.Employer;
import com.example.client_bank.repository.AccountRepository;
import com.example.client_bank.repository.CustomerRepository;
import com.example.client_bank.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final EmployerRepository employerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           AccountRepository accountRepository,
                           EmployerRepository employerRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.employerRepository = employerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer with id " + id + " not found"));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer customer = getCustomerById(id);
        customer.setName(updatedCustomer.getName());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setAge(updatedCustomer.getAge());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public Account createAccount(Long customerId, Currency currency) {
        Customer customer = getCustomerById(customerId);
        Account account = new Account(currency, customer);
        return accountRepository.save(account);
    }

    public void deleteAccount(Long customerId, Long accountId) {
        Customer customer = getCustomerById(customerId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account with id " + accountId + " not found"));

        if (!account.getCustomer().getId().equals(customer.getId())) {
            throw new SecurityException("This account does not belong to the specified customer");
        }

        accountRepository.deleteById(accountId);
    }

    public Customer addEmployerToCustomer(Long customerId, Long employerId) {
        Customer customer = getCustomerById(customerId);
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        customer.addEmployer(employer);
        return customerRepository.save(customer);
    }

    public Customer removeEmployerFromCustomer(Long customerId, Long employerId) {
        Customer customer = getCustomerById(customerId);
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        customer.getEmployers().remove(employer);
        return customerRepository.save(customer);
    }
}