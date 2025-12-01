package com.pcpedia.api.support.application.service;

import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.inventory.domain.model.aggregate.Equipment;
import com.pcpedia.api.inventory.domain.repository.EquipmentRepository;
import com.pcpedia.api.support.application.dto.request.AddCommentRequest;
import com.pcpedia.api.support.application.dto.request.CreateTicketRequest;
import com.pcpedia.api.support.application.dto.response.TicketCommentResponse;
import com.pcpedia.api.support.application.dto.response.TicketResponse;
import com.pcpedia.api.support.domain.model.aggregate.Ticket;
import com.pcpedia.api.support.domain.model.entity.TicketComment;
import com.pcpedia.api.support.domain.model.enums.TicketPriority;
import com.pcpedia.api.support.domain.model.enums.TicketStatus;
import com.pcpedia.api.support.domain.repository.TicketRepository;
import com.pcpedia.api.shared.infrastructure.exception.ForbiddenException;
import com.pcpedia.api.shared.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final MessageSource messageSource;

    public Long createTicket(Long userId, CreateTicketRequest dto) {
        Ticket ticket = Ticket.builder()
                .userId(userId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .equipmentId(dto.getEquipmentId())
                .priority(dto.getPriority() != null ? dto.getPriority() : TicketPriority.MEDIUM)
                .status(TicketStatus.OPEN)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);
        return savedTicket.getId();
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long ticketId, Long userId, boolean isAdmin) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("ticket.not.found")));

        if (!isAdmin && !ticket.getUserId().equals(userId)) {
            throw new ForbiddenException(getMessage("auth.access.denied"));
        }

        return toResponse(ticket, isAdmin);
    }

    @Transactional(readOnly = true)
    public Page<TicketResponse> getAllTickets(Pageable pageable, Long userId, boolean isAdmin) {
        Page<Ticket> tickets;
        if (isAdmin) {
            tickets = ticketRepository.findAll(pageable);
        } else {
            tickets = ticketRepository.findByUserId(userId, pageable);
        }
        return tickets.map(t -> toResponse(t, isAdmin));
    }

    public void updateTicketStatus(Long ticketId, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("ticket.not.found")));

        switch (status) {
            case IN_PROGRESS -> ticket.startProgress();
            case RESOLVED -> ticket.resolve();
            case CLOSED -> ticket.close();
            case OPEN -> ticket.reopen();
        }

        ticketRepository.save(ticket);
    }

    public void addComment(Long ticketId, Long userId, boolean isAdmin, AddCommentRequest dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("ticket.not.found")));

        if (!isAdmin && !ticket.getUserId().equals(userId)) {
            throw new ForbiddenException(getMessage("auth.access.denied"));
        }

        TicketComment comment = TicketComment.builder()
                .userId(userId)
                .content(dto.getContent())
                .isInternal(isAdmin && Boolean.TRUE.equals(dto.getIsInternal()))
                .build();

        ticket.addComment(comment);
        ticketRepository.save(ticket);
    }

    private TicketResponse toResponse(Ticket ticket, boolean isAdmin) {
        User ticketUser = userRepository.findById(ticket.getUserId()).orElse(null);
        Equipment equipment = ticket.getEquipmentId() != null ?
                equipmentRepository.findById(ticket.getEquipmentId()).orElse(null) : null;

        List<Long> userIds = ticket.getComments().stream()
                .map(TicketComment::getUserId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, User> userMap = userRepository.findAllById(userIds)
                .stream().collect(Collectors.toMap(User::getId, u -> u));

        List<TicketCommentResponse> comments = ticket.getComments().stream()
                .filter(c -> isAdmin || !Boolean.TRUE.equals(c.getIsInternal()))
                .map(c -> {
                    User commentUser = userMap.get(c.getUserId());
                    return TicketCommentResponse.builder()
                            .id(c.getId())
                            .userId(c.getUserId())
                            .userName(commentUser != null ? commentUser.getName() : null)
                            .userRole(commentUser != null ? commentUser.getRole().name() : null)
                            .content(c.getContent())
                            .isInternal(c.getIsInternal())
                            .createdAt(c.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return TicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUserId())
                .userName(ticketUser != null ? ticketUser.getName() : null)
                .companyName(ticketUser != null ? ticketUser.getCompanyName() : null)
                .equipmentId(ticket.getEquipmentId())
                .equipmentName(equipment != null ? equipment.getName() : null)
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .priority(ticket.getPriority().name())
                .status(ticket.getStatus().name())
                .comments(comments)
                .resolvedAt(ticket.getResolvedAt())
                .createdAt(ticket.getCreatedAt())
                .build();
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, key, LocaleContextHolder.getLocale());
    }
}
