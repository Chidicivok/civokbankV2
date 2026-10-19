package com.chidicivok.customerservice.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePhoneCustomerRequest {

    @NotBlank
    @Size(min = 3)
    private String phone;

}
