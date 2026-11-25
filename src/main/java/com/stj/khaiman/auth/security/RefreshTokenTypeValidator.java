package com.stj.khaiman.auth.security;

import com.stj.khaiman.auth.constants.JwtConstant;
import com.stj.khaiman.auth.exception.AuthenticationErrorCode;
import org.jspecify.annotations.NonNull;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class RefreshTokenTypeValidator implements OAuth2TokenValidator<Jwt> {

    @Override
    @NonNull
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if(JwtConstant.REFRESH_TOKEN.equals(token.getClaim(JwtConstant.TOKEN_TYPE))){
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(new OAuth2Error(AuthenticationErrorCode.UNAUTHORIZED.name(), AuthenticationErrorCode.UNAUTHORIZED.getMessage(), null));
    }
}
