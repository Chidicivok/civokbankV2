package com.chidicivok.accountservices.services.interfaces;

import com.chidicivok.accountservices.dto.requests.ChangeStatusRequest;
import com.chidicivok.accountservices.dto.requests.CreateAccountRequest;
import com.chidicivok.accountservices.dto.responses.AccountResponse;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountResponse createAccount (CreateAccountRequest request, Long actorId);
//    AccountResponse changeAccountStatus(ChangeStatusRequest request, Long actorId);
//
//    List<AccountResponse> getAllAccounts(Long actorId);
//    Optional<AccountResponse> findByAccountNumber(Long actorId);
//    Optional<AccountResponse> findByCustomerId(Long actorId);

    AccountResponse balanceEnquiry(Long customerId);


}
