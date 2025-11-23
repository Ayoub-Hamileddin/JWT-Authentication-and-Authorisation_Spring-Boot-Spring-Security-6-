package com.eyuup.security.service;

import com.eyuup.security.payload.request.LoginRequest;
import com.eyuup.security.payload.request.RegisterRequest;
import com.eyuup.security.payload.response.AuthResponse;
import com.eyuup.security.payload.response.TokenResponseDto;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
     AuthResponse register(RegisterRequest request);

     AuthResponse Login(LoginRequest request,HttpServletResponse response);

     TokenResponseDto refreshToken(String token);


}
