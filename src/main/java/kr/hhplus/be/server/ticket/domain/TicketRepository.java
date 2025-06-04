package kr.hhplus.be.server.ticket.domain;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket save(Ticket ticket);
    Optional<Ticket> findById(Long ticketId);
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByConcertScheduleId(Long concertScheduleId);
    boolean existsBySeatNumberAndConcertScheduleIdAndStatus(String seatNumber, Long concertScheduleId, TicketStatus status);
    List<Ticket> findExpiredReservations();
    void delete(Ticket ticket);
}
