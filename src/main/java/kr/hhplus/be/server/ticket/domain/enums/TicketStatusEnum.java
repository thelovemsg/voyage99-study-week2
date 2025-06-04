package kr.hhplus.be.server.ticket.domain.enums;

/**
 * RESERVED : 예약됨
 * CONFIRMED : 확정됨
 * CANCELLED : 취소됨
 * EXPIRED : 만료됨
 */
public enum TicketStatusEnum {
    AVAILABLE,
    RESERVED,
    CONFIRMED,
    CANCELLED,
    EXPIRED;
}

