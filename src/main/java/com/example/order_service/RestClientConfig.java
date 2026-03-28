package com.example.order_service;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient inventoryRestClient(RestClient.Builder builder) {
        // Points to the exact spring.application.name registered in Eureka
        return builder.baseUrl("http://INVENTORY-SERVICE").build();
    }
}