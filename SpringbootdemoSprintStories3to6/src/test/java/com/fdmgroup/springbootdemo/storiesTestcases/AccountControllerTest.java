package com.fdmgroup.springbootdemo.storiesTestcases;


import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fdmgroup.springbootdemo.controller.AccountController;
import com.fdmgroup.springbootdemo.model.Address;
import com.fdmgroup.springbootdemo.model.CheckingAccount;
import com.fdmgroup.springbootdemo.model.Person;
import com.fdmgroup.springbootdemo.model.SavingsAccount;
import com.fdmgroup.springbootdemo.service.AccountService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AccountService service;

    private Person createCustomer() {
        Address address = new Address();
        address.setStreetNumber("123");
        address.setPostalCode("L6P1A1");
        address.setCity("Brampton");
        address.setProvince("Ontario");

        Person customer = new Person();
        customer.setCustomerId(1L);
        customer.setCustomerName("John");
        customer.setAddress(address);

        return customer;
    }

    @Test
    void test_createSavingsAccount_returnsStatusOK() throws Exception {
        Person customer = createCustomer();

        SavingsAccount requestAccount = new SavingsAccount();
        requestAccount.setBalance(5000.0);
        requestAccount.setInterestRate(2.5);
        requestAccount.setCustomer(customer);

        SavingsAccount createdAccount = new SavingsAccount();
        createdAccount.setAccountId(100L);
        createdAccount.setBalance(5000.0);
        createdAccount.setInterestRate(2.5);
        createdAccount.setCustomer(customer);

        String requestJson = objectMapper.writeValueAsString(requestAccount);
        String responseJson = objectMapper.writeValueAsString(createdAccount);

        Mockito.when(service.createSavingsAccount(anyLong(), anyDouble(), anyDouble()))
                .thenReturn(createdAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts/savings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json(responseJson)
        );
    }

    @Test
    void test_createCheckingAccount_returnsStatusOK() throws Exception {
        Person customer = createCustomer();

        CheckingAccount requestAccount = new CheckingAccount();
        requestAccount.setBalance(3000.0);
        requestAccount.setNextCheckNumber(101);
        requestAccount.setCustomer(customer);

        CheckingAccount createdAccount = new CheckingAccount();
        createdAccount.setAccountId(200L);
        createdAccount.setBalance(3000.0);
        createdAccount.setNextCheckNumber(101);
        createdAccount.setCustomer(customer);

        String requestJson = objectMapper.writeValueAsString(requestAccount);
        String responseJson = objectMapper.writeValueAsString(createdAccount);

        Mockito.when(service.createCheckingAccount(anyLong(), anyDouble(), anyInt()))
                .thenReturn(createdAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts/checking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json(responseJson)
        );
    }

    @Test
    void test_getAccountsByCity_returnsStatusOK() throws Exception {
        Person customer = createCustomer();

        SavingsAccount savings = new SavingsAccount();
        savings.setAccountId(1L);
        savings.setBalance(1000.0);
        savings.setInterestRate(2.5);
        savings.setCustomer(customer);

        CheckingAccount checking = new CheckingAccount();
        checking.setAccountId(2L);
        checking.setBalance(2000.0);
        checking.setNextCheckNumber(111);
        checking.setCustomer(customer);

        Mockito.when(service.getAccountsByCity("Brampton"))
                .thenReturn(List.of(savings, checking));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/city/Brampton")
                .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
    }
}