package com.fdmgroup.springbootdemo.storiesTestcases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.fdmgroup.springbootdemo.model.Address;
import com.fdmgroup.springbootdemo.model.Person;
import com.fdmgroup.springbootdemo.model.SavingsAccount;
import com.fdmgroup.springbootdemo.repository.AccountRepository;
import com.fdmgroup.springbootdemo.repository.CustomerRepository;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private CustomerRepository customerRepo;

    private Address createAddress() {
        Address address = new Address();
        address.setStreetNumber("123");
        address.setPostalCode("L6P1A1");
        address.setCity("Brampton");
        address.setProvince("Ontario");
        return address;
    }

    private Person createCustomer() {
        Person p = new Person();
        p.setCustomerName("John");
        p.setAddress(createAddress());
        return p;
    }

    private SavingsAccount createAccount(Person customer) {
        SavingsAccount acc = new SavingsAccount();
        acc.setBalance(1000.0);
        acc.setInterestRate(2.5);
        acc.setCustomer(customer);
        return acc;
    }

    @Test
    void testSaveAccount_generatesId() {
        Person savedCustomer = customerRepo.save(createCustomer());

        SavingsAccount acc = createAccount(savedCustomer);
        accountRepo.save(acc);

        assertNotNull(acc.getAccountId());
    }

    @Test
    void testExistsById_returnsTrue() {
        Person savedCustomer = customerRepo.save(createCustomer());

        SavingsAccount acc = createAccount(savedCustomer);
        accountRepo.save(acc);

        assertTrue(accountRepo.existsById(acc.getAccountId()));
    }

    @Test
    void testFindById_returnsAccount() {
        Person savedCustomer = customerRepo.save(createCustomer());

        SavingsAccount acc = createAccount(savedCustomer);
        accountRepo.save(acc);

        var found = accountRepo.findById(acc.getAccountId()).orElse(null);

        assertNotNull(found);
        assertEquals(1000.0, found.getBalance());
        assertEquals("John", found.getCustomer().getCustomerName());
    }

    @Test
    void testFindByCustomerAddressCity() {
        Person savedCustomer = customerRepo.save(createCustomer());

        SavingsAccount acc = createAccount(savedCustomer);
        accountRepo.save(acc);

        List<?> list = accountRepo.findByCustomerAddressCity("Brampton");

        assertEquals(1, list.size());
    }
}