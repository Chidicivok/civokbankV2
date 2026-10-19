package com.chidicivok.customerservice.controllers;


import com.chidicivok.customerservice.dto.requests.ChangePasswordCustomerRequest;
import com.chidicivok.customerservice.dto.requests.CreateCustomerRequest;
import com.chidicivok.customerservice.dto.responses.CustomerResponse;
import com.chidicivok.customerservice.services.interfaces.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customer")
public class AdminCustomerController {

    private final CustomerService customerService;


    public AdminCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private Long getAdminId(Jwt jwt) {
        return ((Number) jwt.getClaim("adminId")).longValue();
    }

    @PostMapping("/create-customer")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request, getAdminId(jwt)));
    }

    @GetMapping("/find-customer/{id}")
    public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.getCustomerById(id, getAdminId(jwt)));
    }


    @GetMapping("/find-customer-by-email/{email}")
    public ResponseEntity<CustomerResponse> findCustomerByEmail(@PathVariable String email, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email, getAdminId(jwt)));
    }

    @GetMapping("/find-customer-by-phone/{phone}")
    public ResponseEntity<CustomerResponse> findCustomerByPhone(@PathVariable String phone, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.getCustomerByPhone(phone, getAdminId(jwt)));
    }


    @GetMapping("/get-all-customers-by-first-name/{firstName}")
    public ResponseEntity<List<CustomerResponse>> getAllCustomersByFirstName(@PathVariable String firstName, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.getCustomerByFirstName(firstName, getAdminId(jwt)));
    }


    @GetMapping("/get-all-customers-by-last-name/{lastName}")
    public ResponseEntity<List<CustomerResponse>> getAllCustomersByLastName(@PathVariable String lastName, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.getCustomerByLastName(lastName, getAdminId(jwt)));
    }

    @GetMapping("/get-all-customers-by-city/{city}")
    public ResponseEntity<List<CustomerResponse>> getAllCustomersByCity(@PathVariable String city, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.getCustomerByCity(city, getAdminId(jwt)));
    }

    @GetMapping("/get-all-customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.getAllCustomers(getAdminId(jwt)));
    }

    @PatchMapping("/reset-password/{id}")
    public ResponseEntity<CustomerResponse> resetPassword(@PathVariable Long id,
                                                          @Valid @RequestBody ChangePasswordCustomerRequest request,
                                                          @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.resetCustomerPasswordByStaff(id, request, getAdminId(jwt)));
    }


}
