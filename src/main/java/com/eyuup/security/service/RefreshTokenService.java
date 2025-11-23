package com.eyuup.security.service;

import com.eyuup.security.model.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String token,Long userId);

    void revokedToken(RefreshToken token);
    
    Boolean isTokenExpired(RefreshToken token);
}
