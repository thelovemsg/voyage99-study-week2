package kr.hhplus.be.server.ticket.application.ticket.port.in;

import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.CreateTicketCommandDto;

public interface CreateTicketUseCase {
    CreateTicketCommandDto.Response createTicket(CreateTicketCommandDto.Request request);
}
