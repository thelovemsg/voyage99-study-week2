package kr.hhplus.be.server.ticket.application.port.ticket.in;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.CancelTicketCommandDto;

public interface CancelTicketUseCase {
    CancelTicketCommandDto.Response complete(CancelTicketCommandDto.Request request);
}
