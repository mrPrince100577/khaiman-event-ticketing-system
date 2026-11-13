package com.stj.khaiman.auth.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret-key}")
    private String JWT_SECRET_KEY;

    public String generateAccessToken(Long userId, String role){
        //Create Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        //Create Payload with claim set
        Date issueTime = new Date();
        Date expirationTime = Date.from(issueTime.toInstant().plus(15, ChronoUnit.MINUTES));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .claim("role", role)
                .build();
        //Sign Header and Payload
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        //Create signature
        try {
            JWSSigner signer = new MACSigner(JWT_SECRET_KEY);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateRefreshToken(Long userId){
        //Create Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        //Create Payload with claim set
        Date issueTime = new Date();
        Date expirationTime = Date.from(issueTime.toInstant().plus(15, ChronoUnit.DAYS));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .build();
        //Sign Header and Payload
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        //Create signature
        try {
            JWSSigner signer = new MACSigner(JWT_SECRET_KEY);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
