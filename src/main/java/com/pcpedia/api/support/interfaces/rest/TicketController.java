package com.pcpedia.api.support.interfaces.rest;

import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.support.application.dto.request.AddCommentRequest;
import com.pcpedia.api.support.application.dto.request.CreateTicketRequest;
import com.pcpedia.api.support.application.dto.request.UpdateTicketStatusRequest;
import com.pcpedia.api.support.application.dto.response.TicketResponse;
import com.pcpedia.api.support.application.service.TicketService;
import com.pcpedia.api.shared.interfaces.rest.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Support ticket management")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Create ticket", description = "Client creates a support ticket")
    public ResponseEntity<ApiResponse<Long>> createTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateTicketRequest request) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Long ticketId = ticketService.createTicket(user.getId(), request);
        String message = getMessage("ticket.created");
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message, ticketId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID", description = "Get ticket details")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        boolean isAdmin = user.isAdmin();
        TicketResponse response = ticketService.getTicketById(id, user.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "List tickets", description = "Get paginated list of tickets")
    public ResponseEntity<ApiResponse<Page<TicketResponse>>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        boolean isAdmin = user.isAdmin();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TicketResponse> tickets = ticketService.getAllTickets(pageable, user.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(tickets));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update ticket status", description = "Admin updates ticket status")
    public ResponseEntity<ApiResponse<Void>> updateTicketStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request) {

        ticketService.updateTicketStatus(id, request.getStatus());
        String message = getMessage("ticket.status.updated");
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Add comment", description = "Add a comment to a ticket")
    public ResponseEntity<ApiResponse<Void>> addComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddCommentRequest request) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        boolean isAdmin = user.isAdmin();
        ticketService.addComment(id, user.getId(), isAdmin, request);
        String message = getMessage("ticket.comment.added");
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, key, LocaleContextHolder.getLocale());
    }
}
