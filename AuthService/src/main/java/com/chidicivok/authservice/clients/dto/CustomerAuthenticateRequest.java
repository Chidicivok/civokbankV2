package com.chidicivok.authservice.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerAuthenticateRequest {
    private String email;
    private String password;
}
