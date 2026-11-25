package com.stj.khaiman.auth.entity;

import com.stj.khaiman.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens",
    uniqueConstraints = @UniqueConstraint(
            name = "uq_refresh_token_user",
            columnNames = {"jti", "user_id"}
    )
)
@Getter
@Setter
public class RefreshToken {
    @Id
    @Column(nullable = false)
    private String jti;

    @Column(nullable = false, name = "expires_time")
    private Instant expTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, name = "session_id")
    private String sid;
}
