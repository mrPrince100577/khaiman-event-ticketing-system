package com.stj.khaiman.ticket.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import com.stj.khaiman.event.entity.EventSessionEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "zone_inventories",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_zone_inventories",
                columnNames = {"event_session_id", "zone_id"}
        )
)
public class ZoneInventoryEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_session_id", nullable = false)
    private EventSessionEntity eventSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private ZoneEntity zone;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false, name = "available_quantity")
    private int availableQuantity;
}
