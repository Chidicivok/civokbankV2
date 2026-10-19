package com.chidicivok.customerservice.dto.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerAuthenticationResponse {

    private Long customerId;
    private String customerEmail;
}
