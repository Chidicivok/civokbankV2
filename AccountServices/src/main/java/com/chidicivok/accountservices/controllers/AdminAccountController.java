package com.chidicivok.accountservices.controllers;

import com.chidicivok.accountservices.dto.requests.CreateAccountRequest;
import com.chidicivok.accountservices.dto.responses.AccountResponse;
import com.chidicivok.accountservices.services.interfaces.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/account")
public class AdminAccountController {

    private final AccountService accountService;

    public AdminAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private Long getAdminId(Jwt jwt) {
        return ((Number) jwt.getClaim("adminId")).longValue();
    }


    @PostMapping("/create-account")
    public ResponseEntity<AccountResponse> createAccount (@Valid  @RequestBody CreateAccountRequest request, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request, getAdminId(jwt)));
    }




}
