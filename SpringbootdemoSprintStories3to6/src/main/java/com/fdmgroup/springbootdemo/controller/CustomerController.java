package com.fdmgroup.springbootdemo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.springbootdemo.model.Customer;
import com.fdmgroup.springbootdemo.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @Operation(summary = "Get all customers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @Operation(summary = "Get customer by specified id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer found",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                            @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                    },
                    headers = {
                            @Header(name = "location", description = "URL to particular id")
                    }),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable long id) {
        return ResponseEntity.ok(service.getCustomerById(id));
    }

    @Operation(summary = "Create customer")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {

        Customer created = service.createCustomer(customer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getCustomerId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Update customer")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable long id,
                                                   @RequestBody Customer customer) {

        Customer updated = service.updateCustomer(id, customer);

        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete customer")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {

        service.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create customer with automatic city/province using geocoder XML")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/province")
    public ResponseEntity<Customer> createCustomerWithProvince(@Valid @RequestBody Customer customer) {

        Customer enriched = service.enrichCustomerWithCityProvince(customer);
        Customer created = service.createCustomer(enriched);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getCustomerId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Create customer with automatic city/province using geocoder JSON")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/province/json")
    public ResponseEntity<Customer> createCustomerWithProvinceUsingJson(@Valid @RequestBody Customer customer) {

        Customer enriched = service.enrichCustomerWithCityProvinceUsingJson(customer);
        Customer created = service.createCustomer(enriched);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getCustomerId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Find customers by city")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Customer>> getCustomersByCity(@PathVariable String city) {

        List<Customer> customers = service.getCustomersByCity(city);

        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Find all customers having at least one savings account")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/savings")
    public ResponseEntity<List<Customer>> getCustomersWithAtLeastOneSavingsAccount() {
        return ResponseEntity.ok(service.getCustomersWithAtLeastOneSavingsAccount());
    }
}