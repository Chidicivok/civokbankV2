package com.chidicivok.authservice.clients.dto;

import com.chidicivok.authservice.enums.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminAuthenticationResponse {

    private Long adminId;
    private String email;
    private AdminRole adminRole;
}