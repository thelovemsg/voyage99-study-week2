package kr.hhplus.be.server.ticket.application.port.in.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetTicketCommandDto {

    @Getter
    @Setter
    public static class Request{
        private Long ticketId;
    }

    public static class Response {

    }
}
