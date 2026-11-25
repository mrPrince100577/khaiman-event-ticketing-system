package com.stj.khaiman.ticket.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import com.stj.khaiman.event.entity.EventSessionEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@SuperBuilder
@Table(name = "ticket_tier_sessions",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_ticket_tier_sessions",
                columnNames = {"event_session_id", "ticket_tier_id"}
        )
)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TicketTierSessionEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_session_id", nullable = false)
    private EventSessionEntity eventSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_tier_id", nullable = false)
    private TicketTierEntity ticketTier;
}
