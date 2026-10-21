package com.chidicivok.accountservices.dto.requests;

import com.chidicivok.accountservices.enums.AccountStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeStatusRequest {

    private AccountStatus status;
}
