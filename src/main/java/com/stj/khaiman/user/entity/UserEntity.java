package com.stj.khaiman.user.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import com.stj.khaiman.user.enums.UserStatus;
import com.stj.khaiman.user.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@SuperBuilder
@Table(name = "users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Column(nullable = false, unique = true, updatable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
}
