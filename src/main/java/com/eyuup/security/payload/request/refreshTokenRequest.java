package com.eyuup.security.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class refreshTokenRequest {
    private String token;
}
