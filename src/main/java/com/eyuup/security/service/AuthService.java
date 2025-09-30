package com.eyuup.security.service;

import com.eyuup.security.payload.request.LoginRequest;
import com.eyuup.security.payload.request.RegisterRequest;
import com.eyuup.security.payload.response.LoginResponse;
import com.eyuup.security.payload.response.RegisterResponse;

public interface AuthService {
    public RegisterResponse register(RegisterRequest request);

    public LoginResponse Login(LoginRequest request);
}
