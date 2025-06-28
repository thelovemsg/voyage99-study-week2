package kr.hhplus.be.server.ticket.application.ticket.port.in;

import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.CancelTicketCommandDto;

public interface CancelTicketUseCase {
    CancelTicketCommandDto.Response complete(CancelTicketCommandDto.Request request);
}
