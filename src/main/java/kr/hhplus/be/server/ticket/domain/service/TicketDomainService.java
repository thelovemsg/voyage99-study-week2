package kr.hhplus.be.server.ticket.domain.service;

public interface TicketDomainService {
    void validateTicketPurchase(Long userId, Long ticketTypeId, Long seatId);
    void validateUserTicketLimit(Long userId, Long concertScheduleId);
}
