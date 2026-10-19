package com.chidicivok.authservice.clients;

import com.chidicivok.authservice.clients.dto.CustomerAuthenticateRequest;
import com.chidicivok.authservice.clients.dto.CustomerAuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CustomerClient {
    private final RestClient restClient;

    public CustomerClient(@Value("${internal.customer.base}") String customerInternalBaseUrl) {
        this.restClient = RestClient.create(customerInternalBaseUrl);
    }

    public CustomerAuthenticationResponse authenticateCustomer(CustomerAuthenticateRequest request) {
        return restClient.post()
                .uri("/authenticate-customer")
                .body(request)
                .retrieve()
                .body(CustomerAuthenticationResponse.class);
    }
}
