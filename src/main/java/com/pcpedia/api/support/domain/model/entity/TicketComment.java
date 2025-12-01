package com.pcpedia.api.support.domain.model.entity;

import com.pcpedia.api.shared.domain.model.AuditableEntity;
import com.pcpedia.api.support.domain.model.aggregate.Ticket;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketComment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_internal")
    @Builder.Default
    private Boolean isInternal = false;
}
