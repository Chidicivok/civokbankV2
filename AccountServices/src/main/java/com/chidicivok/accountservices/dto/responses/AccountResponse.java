package com.chidicivok.accountservices.dto.responses;

import com.chidicivok.accountservices.enums.AccountStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;



public record AccountResponse(
        Long accountId,
        String accountNumber,
        Long customerId,
        String accountHolderName,
        BigDecimal balance,
        AccountStatus status,
        LocalDateTime createdAt
) {
}