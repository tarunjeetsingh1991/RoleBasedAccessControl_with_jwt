package com.fdmgroup.springbootdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SpringbootdemoSprintStories2Application implements CommandLineRunner {

    final String CUSTOMER_BASE_URL = "http://localhost:8080/api/v1/customers";

    private final ApplicationContext ctx;

    public SpringbootdemoSprintStories2Application(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootdemoSprintStories2Application.class, args);
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient geoWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://geocoder.ca")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public void run(String... args) throws Exception {
        /*
        CustomerService service = ctx.getBean(CustomerService.class);
        var customer = service.createCustomerWithGeo("John Doe", "123 Main St", "M5V3L9");
        System.out.println("Created customer: " + customer);
        */
    }
}

// Swagger :http://localhost:8080/swagger-ui/index.html