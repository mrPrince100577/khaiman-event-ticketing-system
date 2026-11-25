package com.stj.khaiman.auth.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.stj.khaiman.auth.constants.JwtConstant;
import com.stj.khaiman.auth.exception.AuthenticationErrorCode;
import com.stj.khaiman.auth.record.IssuedToken;
import com.stj.khaiman.auth.security.RefreshTokenTypeValidator;
import com.stj.khaiman.common.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret-key}")
    private String JWT_SECRET_KEY;

    public String generateAccessToken(Long userId, List<String> roles, List<String> authorities, String sid){
        //Create Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        //Create Payload with claim set
        Date issueTime = new Date();
        Date expirationTime = Date.from(issueTime.toInstant().plus(15, ChronoUnit.MINUTES));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .jwtID(UUID.randomUUID().toString())
                .claim(JwtConstant.TOKEN_TYPE, JwtConstant.ACCESS_TOKEN)
                .claim(JwtConstant.AUTHORITIES, authorities)
                .claim(JwtConstant.ROLES, roles)
                .claim(JwtConstant.SESSION_ID, sid)
                .build();
        //Sign Header and Payload
        return sign(header, claimsSet);
    }

    public IssuedToken generateRefreshToken(Long userId, String sid){
        //Create Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        //Create Payload with claim set
        Date issueTime = new Date();
        Date expirationTime = Date.from(issueTime.toInstant().plus(15, ChronoUnit.DAYS));
        String jti = UUID.randomUUID().toString();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .claim(JwtConstant.TOKEN_TYPE, JwtConstant.REFRESH_TOKEN)
                .claim(JwtConstant.SESSION_ID, sid)
                .jwtID(jti)
                .build();
        //Sign Header and Payload
        String value = sign(header, claimsSet);

        return new IssuedToken(value, jti, expirationTime.toInstant(), sid, userId);
    }

    private String sign(JWSHeader header,JWTClaimsSet claims) {
        try {
            SignedJWT jwt = new SignedJWT(header, claims);
            jwt.sign(new MACSigner(JWT_SECRET_KEY));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("Cannot sign JWT", e);
        }
    }

    public Jwt verifyToken(String refreshToken){
        SecretKeySpec secretKeySpec = new SecretKeySpec(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build();

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefault(),       // token còn hạn không
                new RefreshTokenTypeValidator()));

        try {
            return decoder.decode(refreshToken);
        } catch (JwtException e) {
            throw new AppException(AuthenticationErrorCode.INVALID_TOKEN);
        }
    }

}
