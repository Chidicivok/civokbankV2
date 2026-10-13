package com.chidicivok.authservice.clients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminAuthenticateRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
