package com.pcpedia.api.dashboard.application.service;

import com.pcpedia.api.billing.domain.model.aggregate.Invoice;
import com.pcpedia.api.billing.domain.model.enums.InvoiceStatus;
import com.pcpedia.api.billing.domain.repository.InvoiceRepository;
import com.pcpedia.api.billing.domain.repository.PaymentRepository;
import com.pcpedia.api.dashboard.application.dto.response.AdminDashboardResponse;
import com.pcpedia.api.dashboard.application.dto.response.ClientDashboardResponse;
import com.pcpedia.api.iam.domain.model.enums.Role;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.inventory.domain.model.enums.EquipmentStatus;
import com.pcpedia.api.inventory.domain.repository.EquipmentRepository;
import com.pcpedia.api.sales.domain.model.enums.ContractStatus;
import com.pcpedia.api.sales.domain.model.enums.QuoteStatus;
import com.pcpedia.api.sales.domain.model.enums.RequestStatus;
import com.pcpedia.api.sales.domain.repository.ContractRepository;
import com.pcpedia.api.sales.domain.repository.QuoteRepository;
import com.pcpedia.api.sales.domain.repository.RequestRepository;
import com.pcpedia.api.support.domain.model.enums.TicketPriority;
import com.pcpedia.api.support.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final RequestRepository requestRepository;
    private final QuoteRepository quoteRepository;
    private final ContractRepository contractRepository;
    private final TicketRepository ticketRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    public AdminDashboardResponse getAdminDashboard() {
        // Equipment by status
        Map<String, Long> equipmentByStatus = new HashMap<>();
        for (EquipmentStatus status : EquipmentStatus.values()) {
            equipmentByStatus.put(status.name(), equipmentRepository.countByStatus(status));
        }

        // Tickets by priority
        Map<String, Long> ticketsByPriority = new HashMap<>();
        for (TicketPriority priority : TicketPriority.values()) {
            ticketsByPriority.put(priority.name(), ticketRepository.countByPriority(priority));
        }

        // Monthly revenue (current month)
        LocalDate now = LocalDate.now();
        BigDecimal monthlyRevenue = paymentRepository.sumAmountByMonth(now.getMonthValue(), now.getYear());

        return AdminDashboardResponse.builder()
                .totalClients(userRepository.countByRole(Role.CLIENT))
                .activeContracts(contractRepository.countByStatus(ContractStatus.ACTIVE))
                .pendingRequests(requestRepository.countByStatus(RequestStatus.PENDING))
                .pendingQuotes(quoteRepository.countByStatus(QuoteStatus.SENT))
                .openTickets(ticketRepository.countOpenTickets())
                .monthlyRevenue(monthlyRevenue)
                .pendingPayments(invoiceRepository.sumPendingAmount())
                .equipmentByStatus(equipmentByStatus)
                .ticketsByPriority(ticketsByPriority)
                .build();
    }

    public ClientDashboardResponse getClientDashboard(Long userId) {
        // Get next pending invoice
        List<Invoice> pendingInvoices = invoiceRepository.findPendingByUserId(userId);
        LocalDate nextPaymentDate = null;
        BigDecimal nextPaymentAmount = null;
        if (!pendingInvoices.isEmpty()) {
            Invoice nextInvoice = pendingInvoices.get(0);
            nextPaymentDate = nextInvoice.getDueDate();
            nextPaymentAmount = nextInvoice.getAmount();
        }

        // Count equipment from active contracts
        long totalEquipment = contractRepository.findByUserIdAndStatus(userId, ContractStatus.ACTIVE, null)
                .getContent()
                .stream()
                .mapToLong(c -> c.getItems().size())
                .sum();

        return ClientDashboardResponse.builder()
                .activeContracts(contractRepository.countByUserIdAndStatus(userId, ContractStatus.ACTIVE))
                .totalEquipment(totalEquipment)
                .pendingRequests(requestRepository.countByUserIdAndStatus(userId, RequestStatus.PENDING))
                .pendingQuotes(quoteRepository.countByUserIdAndStatus(userId, QuoteStatus.SENT))
                .openTickets(ticketRepository.countOpenTicketsByUserId(userId))
                .pendingInvoices(invoiceRepository.countByUserIdAndStatus(userId, InvoiceStatus.PENDING) +
                        invoiceRepository.countByUserIdAndStatus(userId, InvoiceStatus.OVERDUE))
                .nextPaymentDate(nextPaymentDate)
                .nextPaymentAmount(nextPaymentAmount)
                .build();
    }
}
