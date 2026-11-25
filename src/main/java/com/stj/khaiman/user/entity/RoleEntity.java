package com.stj.khaiman.user.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RoleEntity extends BaseEntity {

    @Column(nullable = false, unique = true, name = "role_name")
    private String roleName;

    private String description;

    @Column(nullable = false, name = "built_in")
    @Builder.Default
    private Boolean builtIn = false;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleHasPermission> roleHasPermissions;

}
