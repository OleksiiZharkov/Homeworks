package com.example.client_bank.service;
import com.example.client_bank.exception.ResourceNotFoundException;
import com.example.client_bank.model.Account;
import com.example.client_bank.model.Currency;
import com.example.client_bank.model.Customer;
import com.example.client_bank.model.Employer;
import com.example.client_bank.repository.AccountRepository;
import com.example.client_bank.repository.CustomerRepository;
import com.example.client_bank.repository.EmployerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer customer = getCustomerById(id);
        customer.setName(updatedCustomer.getName());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setAge(updatedCustomer.getAge());
        customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer with id " + id + " not found");
        }
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
                .orElseThrow(() -> new ResourceNotFoundException("Account with id " + accountId + " not found"));

        if (!account.getCustomer().getId().equals(customer.getId())) {
            throw new SecurityException("This account does not belong to the specified customer");
        }

        accountRepository.deleteById(accountId);
    }

    public Customer addEmployerToCustomer(Long customerId, Long employerId) {
        Customer customer = getCustomerById(customerId);

        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer with id " + employerId + " not found"));

        customer.addEmployer(employer);
        return customerRepository.save(customer);
    }

    public Customer removeEmployerFromCustomer(Long customerId, Long employerId) {
        Customer customer = getCustomerById(customerId);

        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer with id " + employerId + " not found"));

        customer.getEmployers().remove(employer);
        return customerRepository.save(customer);
    }
}