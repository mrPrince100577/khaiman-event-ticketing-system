package com.stj.khaiman.user.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import com.stj.khaiman.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@Table(name = "users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails {
    @Column(nullable = false, unique = true, updatable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserHasRole> userHasRoles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @Column(name = "token_valid_after")
    @Builder.Default
    private Instant tokenValidAfter = null;

    public void addRole(RoleEntity role){
        this.userHasRoles.add(new UserHasRole(this, role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userHasRoles.stream()
                .flatMap(userHasRole -> userHasRole.getRole().getRoleHasPermissions().stream())
                .map(roleHasPermission -> new SimpleGrantedAuthority(roleHasPermission.getPermission().getCode()))
                .collect(Collectors.toSet());
    }

    public List<String> getRoleNames() {
        return userHasRoles.stream()
                .map(userHasRole -> userHasRole.getRole().getRoleName())
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.userStatus != UserStatus.BANNED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.userStatus == UserStatus.ACTIVE;
    }
}
