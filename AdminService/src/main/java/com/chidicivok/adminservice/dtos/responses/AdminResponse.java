package com.chidicivok.adminservice.dtos.responses;

import com.chidicivok.adminservice.enums.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AdminResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private AdminRole adminRole;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;

}
