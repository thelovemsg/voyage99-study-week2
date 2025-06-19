package kr.hhplus.be.server.ticket.application.port.ticket.in;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.CreateTicketCommandDto;
import kr.hhplus.be.server.ticket.domain.model.Ticket;

public interface CreateTicketUseCase {
    CreateTicketCommandDto.Response createTicket(CreateTicketCommandDto.Request request);
}
