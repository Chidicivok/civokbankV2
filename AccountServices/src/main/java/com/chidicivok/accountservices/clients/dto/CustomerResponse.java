package com.chidicivok.accountservices.clients.dto;

import java.time.LocalDateTime;
public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String city,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String message,
        boolean active
) {}
