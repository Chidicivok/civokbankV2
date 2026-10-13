package com.chidicivok.adminservice.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminAuthenticationRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
