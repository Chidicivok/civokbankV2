package com.chidicivok.adminservice.dtos.requests;

import com.chidicivok.adminservice.enums.AdminRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleAdminRequest {

    @NotNull
    private AdminRole adminRole;
}
