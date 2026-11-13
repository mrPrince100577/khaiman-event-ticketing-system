package com.stj.khaiman.user.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@Table(name = "user_profile")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Past
    @Column(nullable = false, name = "date_of_birth")
    private LocalDate dob;

    @Column(name = "avatar")
    private String avatarUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
}
