package com.pcpedia.api.support.application.dto.request;

import com.pcpedia.api.support.domain.model.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {

    @NotBlank(message = "{validation.title.required}")
    private String title;

    @NotBlank(message = "{validation.description.required}")
    private String description;

    private Long equipmentId;

    private TicketPriority priority;
}
