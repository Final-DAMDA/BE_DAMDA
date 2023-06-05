package com.damda.back.utils;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

public interface JwtManager {
    public String jwtToken(String name,String gender,String phoneNumber,String profileImage);

    public DecodedJWT verify(String jwt) throws SignatureVerificationException, TokenExpiredException;
}
