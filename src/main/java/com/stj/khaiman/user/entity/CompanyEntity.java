package com.stj.khaiman.user.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import com.stj.khaiman.event.entity.EventEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@Table(name = "companies")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity extends BaseEntity {

    @Column(nullable = false)
    private String companyName;

    @Column(unique = true)
    private String taxCode;

    private String contactEmail;
    private String contactPhone;
    private String address;

    @Column(length = 2000)
    private String description;

    private String logoUrl;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<UserEntity> users;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<EventEntity> events;
}
