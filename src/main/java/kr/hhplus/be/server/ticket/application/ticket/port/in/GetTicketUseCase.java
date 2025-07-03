package kr.hhplus.be.server.ticket.application.ticket.port.in;

import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.GetTicketCommandDto;

public interface GetTicketUseCase {
    GetTicketCommandDto.Response getTicket(Long ticketId);
}

