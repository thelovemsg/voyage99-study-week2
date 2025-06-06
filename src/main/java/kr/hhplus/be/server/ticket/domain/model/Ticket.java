package kr.hhplus.be.server.ticket.domain.model;

import kr.hhplus.be.server.ticket.domain.enums.TicketStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class Ticket {

    private Long ticketId;
    private Long userId;
    private Long seatId;
    private Long concertScheduleId;
    private String ticketNo;
    private String concertInfo;
    private String seatInfo;
    private TicketStatusEnum ticketStatus;
    private LocalDateTime purchaseDateTime;
    private BigDecimal totalAmount;

    // 예약 관련 필드 (임시 예약용)
    private LocalDateTime reservedUntil;
    private Long reservedBy;

    // 비즈니스 메서드들
    public void reserve(Long userId) {
        this.reservedBy = userId;
        this.reservedUntil = LocalDateTime.now().plusMinutes(5);
        this.ticketStatus = TicketStatusEnum.RESERVED;
    }

    public boolean isReservedByOther(Long userId) {
        return reservedBy != null && !reservedBy.equals(userId);
    }

    public boolean isReservationExpired() {
        return reservedUntil != null && LocalDateTime.now().isAfter(reservedUntil);
    }

    // 정적 팩토리 메서드
    public static Ticket create(Long userId, Long seatId, Long concertScheduleId,
                                String ticketNo, BigDecimal totalAmount) {
        return new Ticket(
                null, // ID는 저장 시 생성
                userId,
                seatId,
                concertScheduleId,
                ticketNo,
                null, // concertInfo는 나중에 설정
                null, // seatInfo는 나중에 설정
                TicketStatusEnum.AVAILABLE,
                null, // purchaseDateTime은 구매 완료 시 설정
                totalAmount,
                null, // reservedUntil
                null  // reservedBy
        );
    }
}