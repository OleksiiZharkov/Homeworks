package com.example.client_bank.controller;

import com.example.client_bank.dto.CustomerRequestDto;
import com.example.client_bank.dto.CustomerResponseDto;
import com.example.client_bank.facade.CustomerFacade;
import com.example.client_bank.service.CustomerService;
import com.example.client_bank.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerFacade customerFacade;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser
    void getCustomerById_Success() throws Exception {
        CustomerResponseDto responseDto = new CustomerResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Test");

        when(customerFacade.getById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    @WithMockUser
    void getAllCustomers_ReturnsPage() throws Exception {
        CustomerResponseDto responseDto = new CustomerResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Test");
        Page<CustomerResponseDto> customerPage = new PageImpl<>(List.of(responseDto), PageRequest.of(0, 10), 1);

        when(customerFacade.getAll(any(Pageable.class))).thenReturn(customerPage);
        mockMvc.perform(get("/api/customers?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test"));
    }

    @Test
    @WithMockUser
    void createCustomer_WithValidDto_ReturnsCreated() throws Exception {
        CustomerRequestDto requestDto = new CustomerRequestDto();
        requestDto.setName("Test");
        requestDto.setEmail("test@test.com");
        requestDto.setAge(20);
        requestDto.setPhoneNumber("+1234567890");
        requestDto.setPassword("password123");

        CustomerResponseDto responseDto = new CustomerResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Test");

        when(customerFacade.create(any(CustomerRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void createCustomer_WithInvalidDto_ReturnsBadRequest() throws Exception {
        CustomerRequestDto requestDto = new CustomerRequestDto();
        requestDto.setName("T");
        requestDto.setEmail("test.com");
        requestDto.setAge(17);

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Ім'я має складатися щонайменше з 2 символів"))
                .andExpect(jsonPath("$.email").value("Некоректний формат email"))
                .andExpect(jsonPath("$.age").value("Клієнт повинен бути не молодше 18 років"));
    }
}