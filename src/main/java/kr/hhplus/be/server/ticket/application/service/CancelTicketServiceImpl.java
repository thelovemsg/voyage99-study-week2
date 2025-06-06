package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.ticket.application.port.in.dto.CancelTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.in.CancelTicketUseCase;
import org.springframework.stereotype.Service;

@Service
public class CancelTicketServiceImpl implements CancelTicketUseCase {

    @Override
    public CancelTicketCommandDto.Response complete(CancelTicketCommandDto.Request request) {
        return null;
    }
}
