package com.eyuup.security.service.impl;



import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eyuup.security.config.JwtService;
import com.eyuup.security.model.RefreshToken;
import com.eyuup.security.model.Role;
import com.eyuup.security.model.User;
import com.eyuup.security.payload.request.LoginRequest;
import com.eyuup.security.payload.request.RegisterRequest;
import com.eyuup.security.payload.response.AuthResponse;
import com.eyuup.security.payload.response.TokenResponseDto;
import com.eyuup.security.repository.RefreshTokenRepository;
import com.eyuup.security.repository.UserRepository;
import com.eyuup.security.service.AuthService;
import com.eyuup.security.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

     @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;
    
    @Override
    public AuthResponse register(RegisterRequest request) {

        var user=User.builder()
        .firstName(request.getFirstName())
        .LastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

        userRepository.save(user);

        String accessToken=jwtService.generateAcessToken(null, user);
        String refreshToken=jwtService.generateRefreshToken(null, user);

        refreshTokenService.createRefreshToken(refreshToken, user.getId());

      return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
            .build();
    }




    @Override
    public AuthResponse Login(LoginRequest request,HttpServletResponse response) {
        authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(
             request.getEmail(),
            request.getPassword())
        );
          var user =userRepository.findByEmail(request.getEmail()).orElseThrow();
          String accessToken=jwtService.generateAcessToken(null, user);
          String refreshToken=jwtService.generateRefreshToken(null, user);

         refreshTokenService.createRefreshToken(refreshToken, user.getId());

         ResponseCookie newRefreshToken=ResponseCookie.from("refreshToken",refreshToken)
         .httpOnly(true)
         .secure(true)
         .path("/")
         .sameSite("None")
         .maxAge(refreshTokenExpiration)
         .build();


         response.addHeader("Set-Cookie", newRefreshToken.toString());;

      return AuthResponse.builder()
                .token(accessToken)
            .build();
    }




    @Override
    public TokenResponseDto refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshTokenService.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh Token Expired, please login again");
        }

        if (refreshToken.getRevoked()) {
            throw new RuntimeException("Refresh Token is revoked, please login again");
        }

        String newAccessToken = jwtService.generateAcessToken(null, refreshToken.getUser());
        String newRefreshToken = jwtService.generateRefreshToken(null, refreshToken.getUser());

      
        refreshToken.setToken(newRefreshToken);
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() +refreshTokenExpiration) );
        refreshTokenRepository.save(refreshToken);


        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
        .build();

    }

}
