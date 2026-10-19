package com.chidicivok.customerservice.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAuthenticationRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
