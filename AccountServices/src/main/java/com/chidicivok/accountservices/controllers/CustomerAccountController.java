package com.chidicivok.accountservices.controllers;

import com.chidicivok.accountservices.dto.responses.AccountResponse;
import com.chidicivok.accountservices.services.interfaces.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/account")
public class CustomerAccountController {

    private final AccountService accountService;

    public CustomerAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private Long getCustomerId(Jwt jwt) {
        return ((Number) jwt.getClaim("customerId")).longValue();
    }


    @GetMapping("/check-balance/{accountNumber}")
    public ResponseEntity<AccountResponse> ViewBalance (@PathVariable Long customerId , @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(accountService.balanceEnquiry(customerId));
    }


}
