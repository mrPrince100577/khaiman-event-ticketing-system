package com.stj.khaiman.user.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PermissionEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    private String module;

}
