package com.eyuup.security.service.impl;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eyuup.security.model.RefreshToken;
import com.eyuup.security.model.User;
import com.eyuup.security.payload.request.refreshTokenRequest;
import com.eyuup.security.repository.RefreshTokenRepository;
import com.eyuup.security.repository.UserRepository;
import com.eyuup.security.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService  {

    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access-token-expiration}")
    private Long refreshTokenExpiration;
    
    @Override
    public RefreshToken createRefreshToken(String token,Long userId) {
        User user=userRepository.findById(userId).orElseThrow();


      RefreshToken refreshToken= RefreshToken.builder()
        .user(user)
        .token(token)
        .expireDate(new Date(System.currentTimeMillis()+ refreshTokenExpiration))
        .revoked(false)
        .createdAt(LocalDateTime.now())
       .build();

       return refreshTokenRepository.save(refreshToken);


      


    }

    @Override
    public void revokedToken(RefreshToken token) {
       token.setRevoked(true);
       refreshTokenRepository.save(token);
    }

    @Override
    public Boolean isTokenExpired(RefreshToken token) {
       return  token.getExpireDate().before(new Date());
    }

}
