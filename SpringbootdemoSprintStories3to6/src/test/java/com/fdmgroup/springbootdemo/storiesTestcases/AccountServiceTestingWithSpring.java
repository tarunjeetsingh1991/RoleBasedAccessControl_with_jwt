package com.fdmgroup.springbootdemo.storiesTestcases;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;

import com.fdmgroup.springbootdemo.exception.ResourceNotFoundException;
import com.fdmgroup.springbootdemo.model.Account;
import com.fdmgroup.springbootdemo.model.Address;
import com.fdmgroup.springbootdemo.model.CheckingAccount;
import com.fdmgroup.springbootdemo.model.Person;
import com.fdmgroup.springbootdemo.model.SavingsAccount;
import com.fdmgroup.springbootdemo.repository.AccountRepository;
import com.fdmgroup.springbootdemo.repository.CustomerRepository;
import com.fdmgroup.springbootdemo.service.AccountService;

@SpringBootTest(properties = "geocoder.api.key=dummy-key")
public class AccountServiceTestingWithSpring {

    @MockitoBean
    AccountRepository accountRepository;

    @MockitoBean
    CustomerRepository customerRepository;

    @MockitoBean
    WebClient geoWebClient;

    AccountService service;

    @BeforeEach
    public void setup() {
        service = new AccountService(accountRepository, customerRepository);
    }

    @Test
    void contextLoads() {
    }

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
        customer.setAccounts(new ArrayList<>());

        return customer;
    }

    @Test
    void test_createSavingsAccount_returnsSavedSavingsAccount() {
        Person customer = createCustomer();

        SavingsAccount savedAccount = new SavingsAccount();
        savedAccount.setAccountId(100L);
        savedAccount.setBalance(5000.0);
        savedAccount.setInterestRate(2.5);
        savedAccount.setCustomer(customer);

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Mockito.when(accountRepository.save(Mockito.any(SavingsAccount.class))).thenReturn(savedAccount);

        Account result = service.createSavingsAccount(1L, 5000.0, 2.5);

        assertEquals(savedAccount, result);
    }

    @Test
    void test_createSavingsAccount_throwsException_whenCustomerNotFound() {
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createSavingsAccount(1L, 5000.0, 2.5));
    }

    @Test
    void test_createCheckingAccount_returnsSavedCheckingAccount() {
        Person customer = createCustomer();

        CheckingAccount savedAccount = new CheckingAccount();
        savedAccount.setAccountId(200L);
        savedAccount.setBalance(3000.0);
        savedAccount.setNextCheckNumber(101);
        savedAccount.setCustomer(customer);

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Mockito.when(accountRepository.save(Mockito.any(CheckingAccount.class))).thenReturn(savedAccount);

        Account result = service.createCheckingAccount(1L, 3000.0, 101);

        assertEquals(savedAccount, result);
    }

    @Test
    void test_createCheckingAccount_throwsException_whenCustomerNotFound() {
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createCheckingAccount(1L, 3000.0, 101));
    }

    @Test
    void test_getAccountsByCity_returnsAccountsFromRepository() {
        Person customer = createCustomer();

        SavingsAccount account1 = new SavingsAccount();
        account1.setAccountId(1L);
        account1.setBalance(1000.0);
        account1.setInterestRate(2.0);
        account1.setCustomer(customer);

        CheckingAccount account2 = new CheckingAccount();
        account2.setAccountId(2L);
        account2.setBalance(2000.0);
        account2.setNextCheckNumber(111);
        account2.setCustomer(customer);

        List<Account> expected = List.of(account1, account2);

        Mockito.when(accountRepository.findByCustomerAddressCity("Brampton")).thenReturn(expected);

        List<Account> result = service.getAccountsByCity("Brampton");

        assertEquals(2, result.size());
        assertEquals(expected, result);
    }
}