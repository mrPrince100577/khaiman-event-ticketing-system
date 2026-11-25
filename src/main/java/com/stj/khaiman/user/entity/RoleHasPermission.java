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
@Table(name = "role_has_permissions",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_role_has_permissions",
                columnNames = {"role_id", "permission_id"}
        )
)
public class RoleHasPermission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "role_id")
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "permission_id")
    private PermissionEntity permission;
}
