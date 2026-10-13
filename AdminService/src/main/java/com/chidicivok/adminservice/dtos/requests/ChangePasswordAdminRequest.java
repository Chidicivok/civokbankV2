package com.chidicivok.adminservice.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordAdminRequest {

    @NotBlank
    @Size(min = 8)
    private String newPassword;
}
