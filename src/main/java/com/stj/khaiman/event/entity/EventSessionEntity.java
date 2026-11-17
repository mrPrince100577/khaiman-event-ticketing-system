package com.stj.khaiman.event.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import com.stj.khaiman.event.enums.SessionStatus;
import com.stj.khaiman.ticket.entity.TicketTierSessionEntity;
import com.stj.khaiman.ticket.entity.ZoneInventoryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_sessions")
public class EventSessionEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Column(nullable = false, name = "session_status")
    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @OneToMany(mappedBy = "eventSession")
    private Set<ZoneInventoryEntity> zoneInventoryEntities;

    @OneToMany(mappedBy = "eventSession")
    private Set<TicketTierSessionEntity> ticketTierSessions;
}
