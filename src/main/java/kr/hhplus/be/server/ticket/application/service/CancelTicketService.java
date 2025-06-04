package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.ticket.application.dto.CancelTicketCommandDto;
import kr.hhplus.be.server.ticket.application.usecase.CancelTicketUseCase;
import org.springframework.stereotype.Service;

@Service
public class CancelTicketService implements CancelTicketUseCase {

    @Override
    public CancelTicketCommandDto.Response complete(CancelTicketCommandDto.Request request) {
        return null;
    }
}
