package com.chidicivok.authservice.clients;


import com.chidicivok.authservice.clients.dto.AdminAuthenticateRequest;
import com.chidicivok.authservice.clients.dto.AdminAuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AdminClient {

    private final RestClient restClient;

    public AdminClient( @Value("${internal.admin.base}")  String adminInternalBaseUrl  ) {
        this.restClient = RestClient.create(adminInternalBaseUrl);
    }


    public AdminAuthenticationResponse authenticateAdmin(AdminAuthenticateRequest request) {
        return restClient.post()
                .uri("/authenticate-admin")
                .body(request)
                .retrieve()
                .body(AdminAuthenticationResponse.class);
    }


}
