package kr.hhplus.be.server.ticket.application.port.ticket.in;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.GetTicketCommandDto;

public interface GetTicketUseCase {
    GetTicketCommandDto.Response getTicket(Long ticketId);
}

