package kr.hhplus.be.server.ticket.application.usecase;

import kr.hhplus.be.server.ticket.application.dto.GetTicketCommandDto;

import java.util.List;

public interface GetTicketUseCase {
    GetTicketCommandDto.Response getTicket(Long ticketId);
}

