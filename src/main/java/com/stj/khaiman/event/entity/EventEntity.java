package com.stj.khaiman.event.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import com.stj.khaiman.event.enums.EventCategory;
import com.stj.khaiman.event.enums.EventStatus;
import com.stj.khaiman.ticket.entity.ZoneEntity;
import com.stj.khaiman.user.entity.CompanyEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class EventEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventCategory eventCategory;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "venue_name", nullable = false)
    private String venueName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @Column(name = "event_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(mappedBy = "event")
    private Set<EventSessionEntity> sessionEntitySet;

    @OneToMany(mappedBy = "event")
    private Set<ZoneEntity> zoneEntitySet;

}
