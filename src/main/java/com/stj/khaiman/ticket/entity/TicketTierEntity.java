package com.stj.khaiman.ticket.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@Table(name = "ticket_tiers")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TicketTierEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private ZoneEntity zone;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 12, scale = 0)
    private BigDecimal price;

    @Column(name = "sale_start_at", nullable = false)
    private Instant saleStartAt;

    @Column(name = "sale_end_at", nullable = false)
    private Instant saleEndAt;

    private int maxPerOrder;

    @OneToMany(mappedBy = "ticketTier")
    private Set<TicketTierSessionEntity> ticketTierSessions;
}
