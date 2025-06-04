package kr.hhplus.be.server.ticket.domain.model;

import kr.hhplus.be.server.ticket.domain.enums.TicketStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    private Long ticketId;
    private Long userId;
    private Long ticketTypeId;
    private Long seatId;
    private Long concertScheduleId;
    private String ticketNo;           // 티켓번호
    private String concertInfo;        // 콘서트정보
    private String seatInfo;           // 좌석정보
    private TicketStatusEnum ticketStatusEnum;
    private LocalDateTime purchaseDateTime; // 구매일시
    private BigDecimal totalAmount;    // 총금액

}