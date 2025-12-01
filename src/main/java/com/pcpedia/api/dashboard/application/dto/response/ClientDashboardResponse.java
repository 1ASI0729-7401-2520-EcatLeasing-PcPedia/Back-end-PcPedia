package com.pcpedia.api.dashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDashboardResponse {

    private Long activeContracts;
    private Long totalEquipment;
    private Long pendingRequests;
    private Long pendingQuotes;
    private Long openTickets;
    private Long pendingInvoices;
    private LocalDate nextPaymentDate;
    private BigDecimal nextPaymentAmount;
}
