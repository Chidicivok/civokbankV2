package com.chidicivok.authservice.services.interfaces;

import com.chidicivok.authservice.dtos.requests.LoginRequest;
import com.chidicivok.authservice.dtos.responses.LoginResponse;

public interface AuthService {

    LoginResponse loginAdmin(LoginRequest request);

    LoginResponse loginCustomer(LoginRequest request);
}
