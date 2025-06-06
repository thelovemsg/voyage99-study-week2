package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.ticket.application.port.in.dto.GetTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.in.GetTicketUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetTicketServiceImpl implements GetTicketUseCase {

    @Override
    public GetTicketCommandDto.Response getTicket(Long ticketId) {
        return null;
    }
}
