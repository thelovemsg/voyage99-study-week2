package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.ticket.application.dto.GetTicketCommandDto;
import kr.hhplus.be.server.ticket.application.usecase.GetTicketUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetTicketService implements GetTicketUseCase {

    @Override
    public GetTicketCommandDto.Response getTicket(Long ticketId) {
        return null;
    }
}
