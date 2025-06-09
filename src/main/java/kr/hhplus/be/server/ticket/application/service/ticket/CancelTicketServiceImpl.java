package kr.hhplus.be.server.ticket.application.service.ticket;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.CancelTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.ticket.in.CancelTicketUseCase;
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
