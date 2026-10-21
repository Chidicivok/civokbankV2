package com.chidicivok.authservice.controllers;

import com.chidicivok.authservice.dtos.requests.LoginRequest;
import com.chidicivok.authservice.dtos.responses.LoginResponse;
import com.chidicivok.authservice.services.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponse> loginAdmin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginAdmin(request));
    }

    @PostMapping("/customer/login")
    public ResponseEntity<LoginResponse> loginCustomer(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginCustomer(request));
    }

}
