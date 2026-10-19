package com.chidicivok.customerservice.controllers;


import com.chidicivok.customerservice.dto.requests.CustomerAuthenticationRequest;
import com.chidicivok.customerservice.dto.responses.CustomerAuthenticationResponse;
import com.chidicivok.customerservice.dto.responses.CustomerResponse;
import com.chidicivok.customerservice.services.interfaces.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/customer")
public class InternalCustomerController {

    private final CustomerService customerService;


    public InternalCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/authenticate-customer")
    public ResponseEntity<CustomerAuthenticationResponse> login(@Valid @RequestBody CustomerAuthenticationRequest request) {
        return ResponseEntity.ok(customerService.authenticateCustomer(request));
    }

}
