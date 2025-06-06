package kr.hhplus.be.server.ticket.domain.repository;

import kr.hhplus.be.server.ticket.domain.enums.TicketStatusEnum;
import kr.hhplus.be.server.ticket.domain.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket save(Ticket ticket);
    Optional<Ticket> findById(Long ticketId);
    Optional<Ticket> findByIdWithLock(Long ticketId);
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByConcertScheduleId(Long concertScheduleId);
    Optional<Ticket> findByTicketNo(String ticketNo);
    boolean existsBySeatIdAndTicketStatus(Long seatId, TicketStatusEnum status);
}
