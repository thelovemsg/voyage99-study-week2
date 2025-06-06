package kr.hhplus.be.server.ticket.infrastructure.persistence;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.ticket.domain.enums.TicketStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketJpaRepository extends JpaRepository<TicketEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TicketEntity t WHERE t.ticketId = :ticketId")
    Optional<TicketEntity> findByIdWithLock(Long ticketId);
    List<TicketEntity> findByUserId(Long userId);
    Optional<TicketEntity> findByTicketNo(String ticketNo);
    boolean existsBySeatIdAndTicketStatusEnum(Long seatId, TicketStatusEnum status);
    List<TicketEntity> findListByConcertScheduleId(Long concertScheduleId);
}
