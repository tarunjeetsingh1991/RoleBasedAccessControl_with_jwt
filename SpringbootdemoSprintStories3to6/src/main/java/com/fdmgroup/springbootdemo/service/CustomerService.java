

package com.fdmgroup.springbootdemo.service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.fdmgroup.springbootdemo.dto.GeocoderResponse;
import com.fdmgroup.springbootdemo.exception.ResourceNotFoundException;
import com.fdmgroup.springbootdemo.model.Account;
import com.fdmgroup.springbootdemo.model.Address;
import com.fdmgroup.springbootdemo.model.Customer;
import com.fdmgroup.springbootdemo.model.SavingsAccount;
import com.fdmgroup.springbootdemo.repository.CustomerRepository;

import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    private final WebClient geoWebClient;

    @Value("${geocoder.api.key}")
    private String apiKey;

    public CustomerService(CustomerRepository repo, WebClient geoWebClient) {
        this.repo = repo;
        this.geoWebClient = geoWebClient;
    }

    // =========================
    // STORY 1 - CRUD
    // =========================

    @Cacheable(value = "CUSTOMER")
    public List<Customer> getAllCustomers() {
    	System.out.println("DB hit......");
        return repo.findAll();
    }

    public Customer getCustomerById(long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public Customer createCustomer(Customer customer) {
        return repo.save(customer);
    }

    public void deleteCustomer(long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }

        repo.deleteById(id);
    }

    // According to old story rules:
    // only update allowed address-related fields, not read-only customerName
    public Customer updateCustomer(long id, Customer updated) {

        Customer existing = getCustomerById(id);

        if (updated.getAddress() != null) {
            Address existingAddress = existing.getAddress();
            Address updatedAddress = updated.getAddress();

            if (updatedAddress.getPostalCode() != null) {
                existingAddress.setPostalCode(updatedAddress.getPostalCode());
            }

            if (updatedAddress.getCity() != null) {
                existingAddress.setCity(updatedAddress.getCity());
            }

            if (updatedAddress.getProvince() != null) {
                existingAddress.setProvince(updatedAddress.getProvince());
            }
        }

        return repo.save(existing);
    }

    // =========================
    // STORY 2 - GEO ENRICHMENT
    // =========================

    public Customer createCustomerWithGeo(Customer customer) {
        enrichCustomerWithCityProvince(customer);
        return repo.save(customer);
    }

    // XML version
    public Customer enrichCustomerWithCityProvince(Customer customer) {
        try {
            Address address = customer.getAddress();

            String postal = address.getPostalCode().replaceAll("\\s+", "");

            String xmlResponse = geoWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/")
                            .queryParam("locate", postal)
                            .queryParam("geoit", "XML")
                            .queryParam("auth", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(e ->
                            Mono.just("<geodata><city>UNKNOWN</city><prov>UNKNOWN</prov></geodata>"))
                    .block();

            if (xmlResponse != null) {

                ByteArrayInputStream input =
                        new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8));

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document doc = builder.parse(input);

                String city = extractTag(doc, "city");
                String prov = extractTag(doc, "prov");

                address.setCity(city != null ? city : "UNKNOWN");
                address.setProvince(prov != null ? prov : "UNKNOWN");
            }

        } catch (Exception e) {
            customer.getAddress().setCity("UNKNOWN");
            customer.getAddress().setProvince("UNKNOWN");
        }

        return customer;
    }

    // JSON version
    public Customer enrichCustomerWithCityProvinceUsingJson(Customer customer) {
        try {
            String postal = customer.getAddress().getPostalCode().replaceAll("\\s+", "");

            GeocoderResponse response = geoWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/")
                            .queryParam("locate", postal)
                            .queryParam("json", "1")
                            .queryParam("auth", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(GeocoderResponse.class)
                    .block();

            if (response != null && response.getStandard() != null) {
                customer.getAddress().setCity(response.getStandard().getCity());
                customer.getAddress().setProvince(response.getStandard().getProv());
            } else {
                customer.getAddress().setCity("UNKNOWN");
                customer.getAddress().setProvince("UNKNOWN");
            }

        } catch (Exception e) {
            customer.getAddress().setCity("UNKNOWN");
            customer.getAddress().setProvince("UNKNOWN");
        }

        return customer;
    }

    // =========================
    // EXTRA - FIND CUSTOMERS BY CITY
    // =========================

    public List<Customer> getCustomersByCity(String city) {
        return repo.findByAddressCity(city);
    }

    // =========================
    // XML HELPER
    // =========================

    private String extractTag(Document doc, String tagName) {
        NodeList nodes = doc.getElementsByTagName(tagName);

        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }

        return null;
    } 
    
    public List<Customer> getCustomersWithAtLeastOneSavingsAccount() {

        return repo.findAll()
                .stream()
                .filter(customer -> customer.getAccounts() != null &&
                        customer.getAccounts().stream()
                                .anyMatch(account -> account instanceof SavingsAccount))
                .collect(Collectors.toList());
    }
    
    //without streams
    public List<Customer> getCustomersWithAtLeastOneSavingsAccountWithoutStreams() {

        List<Customer> result = new ArrayList<>();

        List<Customer> customers = repo.findAll();

        for (Customer customer : customers) {

            List<Account> accounts = customer.getAccounts();

            if (accounts != null) {

                for (Account account : accounts) {

                    if (account instanceof SavingsAccount) {

                        result.add(customer);
                        break;

                    }
                }
            }
        }

        return result;
    }
}
