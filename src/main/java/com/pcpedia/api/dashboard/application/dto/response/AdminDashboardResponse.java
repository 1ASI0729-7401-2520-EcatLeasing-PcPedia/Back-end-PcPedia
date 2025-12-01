package com.pcpedia.api.dashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {

    private Long totalClients;
    private Long activeContracts;
    private Long pendingRequests;
    private Long pendingQuotes;
    private Long openTickets;
    private BigDecimal monthlyRevenue;
    private BigDecimal pendingPayments;
    private Map<String, Long> equipmentByStatus;
    private Map<String, Long> ticketsByPriority;
}
