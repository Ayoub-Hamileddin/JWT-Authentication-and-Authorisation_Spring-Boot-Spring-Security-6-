package com.eyuup.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eyuup.security.config.JwtService;
import com.eyuup.security.model.RefreshToken;
import com.eyuup.security.payload.request.LoginRequest;
import com.eyuup.security.payload.request.RegisterRequest;
import com.eyuup.security.payload.response.AuthResponse;
import com.eyuup.security.payload.response.TokenResponseDto;
import com.eyuup.security.repository.RefreshTokenRepository;
import com.eyuup.security.service.AuthService;
import com.eyuup.security.service.RefreshTokenService;
import com.eyuup.security.service.impl.AuthServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository ;
    private final JwtService jwtService;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
            return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginRequest request,HttpServletResponse response) {
            return ResponseEntity.ok(authService.Login(request,response));
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken) {
                TokenResponseDto tokens=authService.refreshToken(refreshToken);

                ResponseCookie newRefreshCookie=ResponseCookie.from("refreshToken",tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .sameSite("None")
                .build();



                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                                .body(Map.of("acessToken",tokens.getAccessToken()));
    }
    
}
