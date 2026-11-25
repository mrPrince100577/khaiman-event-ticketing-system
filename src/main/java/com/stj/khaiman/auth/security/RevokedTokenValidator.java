package com.stj.khaiman.auth.security;

import com.stj.khaiman.auth.repository.BlackListTokenRepository;
import com.stj.khaiman.user.entity.UserEntity;
import com.stj.khaiman.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RevokedTokenValidator implements OAuth2TokenValidator<Jwt> {

    private final BlackListTokenRepository blackListTokenRepository;
    private final UserRepository userRepository;

    @Override
    public @NonNull OAuth2TokenValidatorResult validate(@NonNull Jwt token) {
        String jti = token.getId();
        if (jti == null || blackListTokenRepository.existsById(jti)) {
            return failure("Token has been revoked");
        }

        Optional<UserEntity> user = userRepository.findById(Long.valueOf(token.getSubject()));
        if (user.isEmpty()) {
            return failure("User of this token no longer exists");
        }

        if (!isIssuedAfter(token.getIssuedAt(), user.get().getTokenValidAfter())) {
            return failure("Token was issued before the user's tokens were revoked");
        }
        return OAuth2TokenValidatorResult.success();
    }

    private boolean isIssuedAfter(Instant issuedAt, Instant tokenValidAfter) {
        if (tokenValidAfter == null) {
            return true;
        }
        return issuedAt != null && !issuedAt.isBefore(tokenValidAfter);
    }

    private OAuth2TokenValidatorResult failure(String description) {
        return OAuth2TokenValidatorResult.failure(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, description, null));
    }
}
