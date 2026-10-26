package com.fdmgroup.springbootdemo.storiesTestcases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fdmgroup.springbootdemo.controller.CustomerController;
import com.fdmgroup.springbootdemo.model.Address;
import com.fdmgroup.springbootdemo.model.Customer;
import com.fdmgroup.springbootdemo.model.Person;
import com.fdmgroup.springbootdemo.service.CustomerService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService service;

    private Person createPerson(Long id, String name, String postalCode, String city, String province) {
        Person person = new Person();
        person.setCustomerId(id);
        person.setCustomerName(name);

        Address address = new Address();
        address.setStreetNumber("123");
        address.setPostalCode(postalCode);
        address.setCity(city);
        address.setProvince(province);

        person.setAddress(address);
        return person;
    }

    @Test
    void test_getAllCustomers_returnsStatusOk() throws Exception {
        List<Customer> customers = List.of(
                createPerson(1L, "John", "L6P1A1", "Brampton", "Ontario"),
                createPerson(2L, "Tarun", "M1M1M1", "Toronto", "Ontario")
        );

        when(service.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void test_getCustomer_returnsStatusOk() throws Exception {
        Person customer = createPerson(1L, "John", "L6P1A1", "Brampton", "Ontario");
        String customerJson = objectMapper.writeValueAsString(customer);

        when(service.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(customerJson)
                );
    }

    @Test
    void test_createCustomer_returnsStatusCreated() throws Exception {
        Person requestCustomer = createPerson(null, "John", "L6P1A1", "Brampton", "Ontario");
        Person createdCustomer = createPerson(1L, "John", "L6P1A1", "Brampton", "Ontario");

        String requestJson = objectMapper.writeValueAsString(requestCustomer);
        String responseJson = objectMapper.writeValueAsString(createdCustomer);

        when(service.createCustomer(any(Customer.class))).thenReturn(createdCustomer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "http://localhost/api/v1/customers/1"),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(responseJson)
                );
    }

    @Test
    void test_updateCustomer_returnsStatusOk() throws Exception {
        Person requestCustomer = createPerson(null, "John", "M1M1M1", "Toronto", "Ontario");
        Person updatedCustomer = createPerson(1L, "John", "M1M1M1", "Toronto", "Ontario");

        String requestJson = objectMapper.writeValueAsString(requestCustomer);
        String responseJson = objectMapper.writeValueAsString(updatedCustomer);

        when(service.updateCustomer(any(Long.class), any(Customer.class))).thenReturn(updatedCustomer);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/1")
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
    void test_deleteCustomer_returnsStatusNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_createCustomerWithProvince_returnsStatusCreated() throws Exception {
        Person requestCustomer = createPerson(null, "John", "M1M1M1", "", "");
        Person enrichedCustomer = createPerson(1L, "John", "M1M1M1", "Toronto", "Ontario");

        String requestJson = objectMapper.writeValueAsString(requestCustomer);
        String responseJson = objectMapper.writeValueAsString(enrichedCustomer);

        when(service.enrichCustomerWithCityProvince(any(Customer.class))).thenReturn(enrichedCustomer);
        when(service.createCustomer(any(Customer.class))).thenReturn(enrichedCustomer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/province")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "http://localhost/api/v1/customers/province/1"),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(responseJson)
                );
    }

    @Test
    void test_createCustomerWithProvinceUsingJson_returnsStatusCreated() throws Exception {
        Person requestCustomer = createPerson(null, "John", "M1M1M1", "", "");
        Person enrichedCustomer = createPerson(1L, "John", "M1M1M1", "Toronto", "Ontario");

        String requestJson = objectMapper.writeValueAsString(requestCustomer);
        String responseJson = objectMapper.writeValueAsString(enrichedCustomer);

        when(service.enrichCustomerWithCityProvinceUsingJson(any(Customer.class))).thenReturn(enrichedCustomer);
        when(service.createCustomer(any(Customer.class))).thenReturn(enrichedCustomer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/province/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "http://localhost/api/v1/customers/province/json/1"),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(responseJson)
                );
    }

    @Test
    void test_getCustomersByCity_returnsStatusOk() throws Exception {
        List<Customer> customers = List.of(
                createPerson(1L, "John", "M1M1M1", "Toronto", "Ontario"),
                createPerson(2L, "Alice", "M2M2M2", "Toronto", "Ontario")
        );

        when(service.getCustomersByCity("Toronto")).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/city/Toronto")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void test_getCustomersWithAtLeastOneSavingsAccount_returnsStatusOk() throws Exception {
        List<Customer> customers = List.of(
                createPerson(1L, "John", "M1M1M1", "Toronto", "Ontario")
        );

        when(service.getCustomersWithAtLeastOneSavingsAccount()).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/savings")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }
}