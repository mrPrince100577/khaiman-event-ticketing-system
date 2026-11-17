package com.stj.khaiman.ticket.entity;

import com.stj.khaiman.common.entity.BaseEntity;
import com.stj.khaiman.event.entity.EventEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@Table(name = "zones",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_zone_event",
                columnNames = {"name", "event_id"}
        )

)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ZoneEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int defaultCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @OneToMany(mappedBy = "zone")
    private Set<ZoneInventoryEntity> zoneInventoryEntities;
}
