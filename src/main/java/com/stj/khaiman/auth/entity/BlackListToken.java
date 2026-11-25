package com.stj.khaiman.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "black_list_tokens")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlackListToken {
    @Id
    private String jti;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "user_id")
    private Long userId;
}
