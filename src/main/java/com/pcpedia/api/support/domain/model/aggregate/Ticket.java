package com.pcpedia.api.support.domain.model.aggregate;

import com.pcpedia.api.shared.domain.model.AggregateRoot;
import com.pcpedia.api.shared.domain.model.AuditableEntity;
import com.pcpedia.api.support.domain.model.entity.TicketComment;
import com.pcpedia.api.support.domain.model.enums.TicketPriority;
import com.pcpedia.api.support.domain.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends AuditableEntity implements AggregateRoot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "equipment_id")
    private Long equipmentId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private TicketPriority priority = TicketPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private TicketStatus status = TicketStatus.OPEN;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @Builder.Default
    private List<TicketComment> comments = new ArrayList<>();

    // Domain methods
    public void addComment(TicketComment comment) {
        comments.add(comment);
        comment.setTicket(this);
    }

    public void startProgress() {
        if (this.status != TicketStatus.OPEN) {
            throw new IllegalStateException("Only open tickets can be started");
        }
        this.status = TicketStatus.IN_PROGRESS;
    }

    public void resolve() {
        this.status = TicketStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }

    public void close() {
        if (this.status != TicketStatus.RESOLVED) {
            throw new IllegalStateException("Only resolved tickets can be closed");
        }
        this.status = TicketStatus.CLOSED;
    }

    public void reopen() {
        this.status = TicketStatus.OPEN;
        this.resolvedAt = null;
    }

    public boolean isOpen() {
        return this.status == TicketStatus.OPEN;
    }
}
