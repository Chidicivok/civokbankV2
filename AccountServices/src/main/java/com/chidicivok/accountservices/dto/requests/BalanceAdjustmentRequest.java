package com.chidicivok.accountservices.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BalanceAdjustmentRequest {

    @NotNull
    private Long customerId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}
