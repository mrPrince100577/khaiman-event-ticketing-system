package com.stj.khaiman.auth.record;

import java.time.Instant;

public record IssuedToken(
        String value,
        String jti,
        Instant expiresAt,
        String sid,
        Long userId
) {
}
