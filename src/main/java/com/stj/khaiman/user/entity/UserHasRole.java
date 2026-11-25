package com.stj.khaiman.user.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "user_has_role",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_user_has_role",
                columnNames = {"user_id", "role_id"}
        )
)
public class UserHasRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;
}
