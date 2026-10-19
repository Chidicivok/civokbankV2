package com.chidicivok.authservice.clients.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAuthenticationResponse {
    private Long customerId;
    private String customerEmail;
}
