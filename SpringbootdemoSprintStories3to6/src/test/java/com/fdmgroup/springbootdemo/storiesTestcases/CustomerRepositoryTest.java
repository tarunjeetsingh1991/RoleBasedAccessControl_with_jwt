package com.fdmgroup.springbootdemo.storiesTestcases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.fdmgroup.springbootdemo.model.Address;
import com.fdmgroup.springbootdemo.model.Customer;
import com.fdmgroup.springbootdemo.model.Person;
import com.fdmgroup.springbootdemo.repository.CustomerRepository;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repo;

    private Address createAddress() {
        Address address = new Address();
        address.setStreetNumber("123");
        address.setPostalCode("L6P1A1");
        address.setCity("Brampton");
        address.setProvince("Ontario");
        return address;
    }

    private Person createPerson() {
        Person p = new Person();
        p.setCustomerName("John");
        p.setAddress(createAddress());
        return p;
    }

    @Test
    void testRepoSave_generatesId() {
        Person p = createPerson();

        repo.save(p);

        assertNotNull(p.getCustomerId());
    }

    @Test
    void testRepoExistsById_returnsTrue() {
        Person p = createPerson();

        repo.save(p);

        assertTrue(repo.existsById(p.getCustomerId()));
    }

    @Test
    void testRepoFindById_returnsCustomer() {
        Person p = createPerson();

        repo.save(p);

        Customer found = repo.findById(p.getCustomerId()).orElse(null);

        assertNotNull(found);
        assertEquals("John", found.getCustomerName());
    }

    @Test
    void testRepoFindByAddressCity_returnsCustomer() {
        Person p = createPerson();

        repo.save(p);

        var list = repo.findByAddressCity("Brampton");

        assertEquals(1, list.size());
    }
}