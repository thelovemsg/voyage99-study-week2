package kr.hhplus.be.server.ticket.domain.model;

import kr.hhplus.be.server.ticket.domain.enums.TicketGradeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketType {
    private Long ticketTypeId;
    private Long concertScheduleId;
    private BigDecimal price;
    private TicketGradeEnum grade;
    private TicketType ticketTypeStatusCode;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
