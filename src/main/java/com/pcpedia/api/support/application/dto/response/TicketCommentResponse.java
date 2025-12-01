package com.pcpedia.api.support.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketCommentResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String userRole;
    private String content;
    private Boolean isInternal;
    private LocalDateTime createdAt;
}
