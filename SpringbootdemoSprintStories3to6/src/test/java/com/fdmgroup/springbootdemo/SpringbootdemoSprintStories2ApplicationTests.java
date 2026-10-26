package com.fdmgroup.springbootdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(properties = {
        "geocoder.api.key=dummy-key"
})
class SpringbootdemoSprintStories2ApplicationTests {

    @MockitoBean
    private WebClient geoWebClient;

    @Test
    void contextLoads() {
    }
}