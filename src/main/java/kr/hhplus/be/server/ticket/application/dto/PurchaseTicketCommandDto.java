package kr.hhplus.be.server.ticket.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PurchaseTicketCommandDto {

    @Getter
    @Setter
    public static class Request{
        private Long userId;
        private Long ticketTypeId;
        private Long seatId;
        private Long concertScheduleId;
    }

    public static class Response {

    }
}
