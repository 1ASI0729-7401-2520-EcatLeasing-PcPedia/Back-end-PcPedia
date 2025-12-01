package com.pcpedia.api.support.domain.repository;

import com.pcpedia.api.support.domain.model.aggregate.Ticket;
import com.pcpedia.api.support.domain.model.enums.TicketPriority;
import com.pcpedia.api.support.domain.model.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByUserId(Long userId, Pageable pageable);

    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findByPriority(TicketPriority priority, Pageable pageable);

    Page<Ticket> findByUserIdAndStatus(Long userId, TicketStatus status, Pageable pageable);

    long countByStatus(TicketStatus status);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, TicketStatus status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status IN ('OPEN', 'IN_PROGRESS')")
    long countOpenTickets();

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.userId = :userId AND t.status IN ('OPEN', 'IN_PROGRESS')")
    long countOpenTicketsByUserId(@Param("userId") Long userId);

    long countByPriority(TicketPriority priority);
}
