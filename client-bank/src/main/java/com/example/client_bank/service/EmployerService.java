package com.example.client_bank.service;

import com.example.client_bank.model.Employer;
import com.example.client_bank.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerService {

    private final EmployerRepository employerRepository;

    @Autowired
    public EmployerService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    public Employer save(Employer employer) {
        return employerRepository.save(employer);
    }

    public Employer findById(Long id) {
        return employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
    }

    public List<Employer> findAll() {
        return employerRepository.findAll();
    }

    public void deleteById(Long id) {
        employerRepository.deleteById(id);
    }
}