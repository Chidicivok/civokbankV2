package com.chidicivok.customerservice.controllers;

import com.chidicivok.customerservice.dto.requests.*;
import com.chidicivok.customerservice.dto.responses.CustomerResponse;
import com.chidicivok.customerservice.services.interfaces.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private Long getCustomerId(Jwt jwt) {
        return ((Number) jwt.getClaim("customerId")).longValue();
    }

    @GetMapping("/profile")
    public ResponseEntity<CustomerResponse> profile(@AuthenticationPrincipal Jwt jwt) {
        Long customerId = getCustomerId(jwt);
        return ResponseEntity.ok(customerService.getCustomerById(customerId, customerId));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<CustomerResponse> changePassword(@Valid @RequestBody ChangePasswordCustomerRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long customerId = getCustomerId(jwt);
        return ResponseEntity.ok(customerService.changeCustomerPassword(customerId, request, customerId));
    }

    @PatchMapping("/change-email")
    public ResponseEntity<CustomerResponse> changeEmail(@Valid @RequestBody ChangeEmailCustomerRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long customerId = getCustomerId(jwt);
        return ResponseEntity.ok(customerService.changeCustomerEmail(customerId, request, customerId));
    }

    @PatchMapping("/change-phone")
    public ResponseEntity<CustomerResponse> changePhone(@Valid @RequestBody ChangePhoneCustomerRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long customerId = getCustomerId(jwt);
        return ResponseEntity.ok(customerService.changeCustomerPhone(customerId, request, customerId));
    }

    @PatchMapping("/change-city")
    public ResponseEntity<CustomerResponse> changeCity(@Valid @RequestBody ChangeCityCustomerRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long customerId = getCustomerId(jwt);
        return ResponseEntity.ok(customerService.changeCustomerCity(customerId, request, customerId));
    }
}
