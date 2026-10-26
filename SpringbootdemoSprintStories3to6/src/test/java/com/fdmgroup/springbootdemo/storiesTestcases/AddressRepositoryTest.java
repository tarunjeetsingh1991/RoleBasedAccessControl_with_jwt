package com.fdmgroup.springbootdemo.storiesTestcases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.fdmgroup.springbootdemo.model.Address;
import com.fdmgroup.springbootdemo.repository.AddressRepository;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository repo;

    private Address createAddress() {
        Address address = new Address();
        address.setStreetNumber("123");
        address.setPostalCode("L6P1A1");
        address.setCity("Brampton");
        address.setProvince("Ontario");
        return address;
    }

    @Test
    void testRepoSave_generatesId() {
        Address address = createAddress();

        repo.save(address);

        assertNotNull(address.getAddressId());
    }

    @Test
    void testRepoExistsById_returnsTrue() {
        Address address = createAddress();

        repo.save(address);

        assertTrue(repo.existsById(address.getAddressId()));
    }

    @Test
    void testRepoFindById_returnsAddress() {
        Address address = createAddress();

        repo.save(address);

        Address found = repo.findById(address.getAddressId()).orElse(null);

        assertNotNull(found);
        assertEquals("123", found.getStreetNumber());
        assertEquals("L6P1A1", found.getPostalCode());
        assertEquals("Brampton", found.getCity());
        assertEquals("Ontario", found.getProvince());
    }

    @Test
    void testRepoFindAll_returnsAddresses() {
        Address address1 = createAddress();

        Address address2 = new Address();
        address2.setStreetNumber("456");
        address2.setPostalCode("M1M1M1");
        address2.setCity("Toronto");
        address2.setProvince("Ontario");

        repo.save(address1);
        repo.save(address2);

        assertEquals(2, repo.findAll().size());
    }

    @Test
    void testRepoDeleteById_removesAddress() {
        Address address = createAddress();

        repo.save(address);
        Long id = address.getAddressId();

        repo.deleteById(id);

        assertFalse(repo.existsById(id));
    }
}