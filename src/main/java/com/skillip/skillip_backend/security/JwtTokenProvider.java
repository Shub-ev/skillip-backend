package com.skillip.skillip_backend.security;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;



@Component
public class JwtTokenProvider{
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

}