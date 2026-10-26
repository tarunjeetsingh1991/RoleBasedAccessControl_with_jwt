package com.fdmgroup.springbootdemo.storiesTestcases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.fdmgroup.springbootdemo.exception.ResourceNotFoundException;
import com.fdmgroup.springbootdemo.model.Address;
import com.fdmgroup.springbootdemo.model.Customer;
import com.fdmgroup.springbootdemo.model.Person;
import com.fdmgroup.springbootdemo.model.SavingsAccount;
import com.fdmgroup.springbootdemo.repository.CustomerRepository;
import com.fdmgroup.springbootdemo.service.CustomerService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository repo;

    @Mock
    private WebClient geoWebClient;

    private CustomerService service;

    @BeforeEach
    void setUp() {
        service = new CustomerService(repo, geoWebClient);
    }

    private Address createAddress(String postalCode, String city, String province) {
        Address address = new Address();
        address.setStreetNumber("123");
        address.setPostalCode(postalCode);
        address.setCity(city);
        address.setProvince(province);
        return address;
    }

    private Person createPerson(Long id, String name, String postalCode, String city, String province) {
        Person person = new Person();
        person.setCustomerId(id);
        person.setCustomerName(name);
        person.setAddress(createAddress(postalCode, city, province));
        return person;
    }

    @Test
    void test_getAllCustomers_returnsAllCustomers() {
        List<Customer> customers = List.of(
                createPerson(1L, "John", "L6P1A1", "Brampton", "Ontario"),
                createPerson(2L, "Tarun", "M1M1M1", "Toronto", "Ontario")
        );

        when(repo.findAll()).thenReturn(customers);

        List<Customer> result = service.getAllCustomers();

        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    void test_getCustomerById_returnsCustomer_whenFound() {
        Customer customer = createPerson(1L, "John", "L6P1A1", "Brampton", "Ontario");

        when(repo.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = service.getCustomerById(1L);

        assertEquals("John", result.getCustomerName());
        verify(repo).findById(1L);
    }

    @Test
    void test_getCustomerById_throwsException_whenNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, () -> service.getCustomerById(1L));

        assertEquals("Customer not found with id: 1", ex.getMessage());
        verify(repo).findById(1L);
    }

    @Test
    void test_createCustomer_savesCustomer() {
        Customer customer = createPerson(null, "John", "L6P1A1", "Brampton", "Ontario");
        Customer saved = createPerson(1L, "John", "L6P1A1", "Brampton", "Ontario");

        when(repo.save(customer)).thenReturn(saved);

        Customer result = service.createCustomer(customer);

        assertNotNull(result.getCustomerId());
        assertEquals("John", result.getCustomerName());
        verify(repo).save(customer);
    }

    @Test
    void test_deleteCustomer_deletesCustomer_whenExists() {
        when(repo.existsById(1L)).thenReturn(true);

        service.deleteCustomer(1L);

        verify(repo).existsById(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void test_deleteCustomer_throwsException_whenNotFound() {
        when(repo.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, () -> service.deleteCustomer(1L));

        assertEquals("Customer not found with id: 1", ex.getMessage());
        verify(repo).existsById(1L);
        verify(repo, never()).deleteById(anyLong());
    }

    @Test
    void test_updateCustomer_updatesOnlyAddressFields() {
        Person existing = createPerson(1L, "John", "L6P1A1", "Brampton", "Ontario");

        Person updated = new Person();
        Address updatedAddress = new Address();
        updatedAddress.setPostalCode("M1M1M1");
        updatedAddress.setCity("Toronto");
        updatedAddress.setProvince("Ontario");
        updated.setAddress(updatedAddress);
        updated.setCustomerName("Should Not Replace");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Customer result = service.updateCustomer(1L, updated);

        assertEquals("John", result.getCustomerName());
        assertEquals("M1M1M1", result.getAddress().getPostalCode());
        assertEquals("Toronto", result.getAddress().getCity());
        verify(repo).findById(1L);
        verify(repo).save(existing);
    }

    @Test
    void test_getCustomersByCity_returnsMatchingCustomers() {
        List<Customer> customers = List.of(
                createPerson(1L, "John", "M1M1M1", "Toronto", "Ontario"),
                createPerson(2L, "Alice", "M2M2M2", "Toronto", "Ontario")
        );

        when(repo.findByAddressCity("Toronto")).thenReturn(customers);

        List<Customer> result = service.getCustomersByCity("Toronto");

        assertEquals(2, result.size());
        verify(repo).findByAddressCity("Toronto");
    }

    @Test
    void test_getCustomersWithAtLeastOneSavingsAccount_returnsOnlyMatchingCustomers() {
        Person customer1 = createPerson(1L, "John", "L6P1A1", "Brampton", "Ontario");
        customer1.setAccounts(new ArrayList<>(List.of(new SavingsAccount())));

        Person customer2 = createPerson(2L, "Alice", "M1M1M1", "Toronto", "Ontario");
        customer2.setAccounts(new ArrayList<>());

        when(repo.findAll()).thenReturn(List.of(customer1, customer2));

        List<Customer> result = service.getCustomersWithAtLeastOneSavingsAccount();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getCustomerName());
        verify(repo).findAll();
    }

    @Test
    void test_getCustomersWithAtLeastOneSavingsAccountWithoutStreams_returnsOnlyMatchingCustomers() {
        Person customer1 = createPerson(1L, "John", "L6P1A1", "Brampton", "Ontario");
        customer1.setAccounts(new ArrayList<>(List.of(new SavingsAccount())));

        Person customer2 = createPerson(2L, "Alice", "M1M1M1", "Toronto", "Ontario");
        customer2.setAccounts(new ArrayList<>());

        when(repo.findAll()).thenReturn(List.of(customer1, customer2));

        List<Customer> result = service.getCustomersWithAtLeastOneSavingsAccountWithoutStreams();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getCustomerName());
        verify(repo).findAll();
    }

    @Test
    void test_enrichCustomerWithCityProvince_setsCityAndProvince_fromXmlResponse() {
        ExchangeFunction exchangeFunction = request -> Mono.just(
                ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/xml")
                        .body("<geodata><city>Toronto</city><prov>Ontario</prov></geodata>")
                        .build()
        );

        WebClient realWebClient = WebClient.builder().exchangeFunction(exchangeFunction).build();
        CustomerService realService = new CustomerService(repo, realWebClient);
        ReflectionTestUtils.setField(realService, "apiKey", "dummy-key");

        Person customer = createPerson(null, "John", "M1M1M1", "", "");

        Customer result = realService.enrichCustomerWithCityProvince(customer);

        assertEquals("Toronto", result.getAddress().getCity());
        assertEquals("Ontario", result.getAddress().getProvince());
    }

    @Test
    void test_enrichCustomerWithCityProvinceUsingJson_setsUnknown_whenErrorOccurs() {
        ExchangeFunction exchangeFunction = request -> Mono.error(new RuntimeException("API failure"));

        WebClient realWebClient = WebClient.builder().exchangeFunction(exchangeFunction).build();
        CustomerService realService = new CustomerService(repo, realWebClient);
        ReflectionTestUtils.setField(realService, "apiKey", "dummy-key");

        Person customer = createPerson(null, "John", "M1M1M1", "", "");

        Customer result = realService.enrichCustomerWithCityProvinceUsingJson(customer);

        assertEquals("UNKNOWN", result.getAddress().getCity());
        assertEquals("UNKNOWN", result.getAddress().getProvince());
    }
}