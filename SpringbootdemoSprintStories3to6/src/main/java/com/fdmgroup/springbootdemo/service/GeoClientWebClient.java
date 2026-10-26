package com.fdmgroup.springbootdemo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fdmgroup.springbootdemo.model.Customer;
import reactor.core.publisher.Mono;

@Service
public class GeoClientWebClient implements IGeoClient {

    private final WebClient geoWebClient;
    private final String apiKey = "15555171732015421992x214750974";
    public GeoClientWebClient(WebClient geoWebClient) {
        this.geoWebClient = geoWebClient;
    }

    @Override
    public Customer enrichCustomerWithCityProvince(Customer customer) {
        try {
            // Remove spaces from postal code
            String postal = customer.getPostalCode().replaceAll("\\s+", "");
            
            GeoResponse response = geoWebClient.get()
            	    .uri(uriBuilder -> uriBuilder
            	        .path("/")
            	        .queryParam("locate", postal)
            	        .queryParam("geoit", "JSON")
            	        .queryParam("auth", apiKey) // MUST match your geocoder.ca key
            	        .build())
            	    .retrieve()
            	    .onStatus(status -> status.isError(), clientResponse -> {
            	        System.out.println("Geocoder API returned error status: " + clientResponse.statusCode());
            	        return Mono.error(new RuntimeException("Failed to fetch geocode info"));
            	    })
            	    .bodyToMono(GeoResponse.class)
            	    .onErrorResume(e -> {
            	        System.out.println("Failed to fetch geocode info: " + e.getMessage());
            	        // fallback response
            	        GeoResponse fallback = new GeoResponse();
            	        fallback.setCity("UNKNOWN");
            	        fallback.setProv("UNKNOWN");
            	        return Mono.just(fallback);
            	    })
            	    .block();

        } catch (Exception e) {
            System.out.println("Error enriching customer: " + e.getMessage());
        }

        return customer;
    }

    public static class GeoResponse {
        private String city;
        private String prov;

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getProv() { return prov; }
        public void setProv(String prov) { this.prov = prov; }
    }
}