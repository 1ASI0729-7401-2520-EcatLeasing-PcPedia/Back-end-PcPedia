package com.pcpedia.api.support.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String companyName;
    private Long equipmentId;
    private String equipmentName;
    private String title;
    private String description;
    private String priority;
    private String status;
    private List<TicketCommentResponse> comments;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
}
