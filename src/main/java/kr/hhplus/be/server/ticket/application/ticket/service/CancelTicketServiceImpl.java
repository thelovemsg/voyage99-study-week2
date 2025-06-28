package kr.hhplus.be.server.ticket.application.ticket.service;

import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.CancelTicketCommandDto;
import kr.hhplus.be.server.ticket.application.ticket.port.in.CancelTicketUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelTicketServiceImpl implements CancelTicketUseCase {

    @Override
    public CancelTicketCommandDto.Response complete(CancelTicketCommandDto.Request request) {
        return null;
    }
}
