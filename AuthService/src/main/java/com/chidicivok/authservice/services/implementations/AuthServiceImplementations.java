package com.chidicivok.authservice.services.implementations;

import com.chidicivok.authservice.clients.AdminClient;
import com.chidicivok.authservice.clients.CustomerClient;
import com.chidicivok.authservice.clients.dto.AdminAuthenticateRequest;
import com.chidicivok.authservice.clients.dto.AdminAuthenticationResponse;
import com.chidicivok.authservice.clients.dto.CustomerAuthenticateRequest;
import com.chidicivok.authservice.clients.dto.CustomerAuthenticationResponse;
import com.chidicivok.authservice.dtos.requests.LoginRequest;
import com.chidicivok.authservice.dtos.responses.LoginResponse;
import com.chidicivok.authservice.services.JwtService;
import com.chidicivok.authservice.services.interfaces.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementations implements AuthService {

    private final AdminClient adminClient;
    private final JwtService jwtService;
    private final CustomerClient customerClient;

    public AuthServiceImplementations(AdminClient adminClient, JwtService jwtService, CustomerClient customerClient) {
        this.adminClient = adminClient;
        this.jwtService = jwtService;
        this.customerClient = customerClient;
    }

    @Override
    public LoginResponse loginAdmin(LoginRequest request) {

        AdminAuthenticationResponse admin = adminClient.authenticateAdmin(
                new AdminAuthenticateRequest(
                        request.getEmail(), request.getPassword()
                )
        );

        JwtService.TokenResult result = jwtService.generateAdminToken(admin);

        return new LoginResponse(result.token(), "Bearer", result.expiresAt());
    }

    @Override
    public LoginResponse loginCustomer(LoginRequest request) {

        CustomerAuthenticationResponse customer = customerClient.authenticateCustomer(
                new CustomerAuthenticateRequest(
                        request.getEmail(), request.getPassword()
                )
        );

        JwtService.TokenResult result = jwtService.generateCustomerToken(customer);

        return new LoginResponse(result.token(), "Bearer", result.expiresAt());
    }
}
