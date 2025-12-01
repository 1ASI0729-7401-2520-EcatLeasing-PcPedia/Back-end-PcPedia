package com.pcpedia.api.support.application.dto.request;

import com.pcpedia.api.support.domain.model.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketStatusRequest {

    @NotNull(message = "{validation.status.required}")
    private TicketStatus status;
}
