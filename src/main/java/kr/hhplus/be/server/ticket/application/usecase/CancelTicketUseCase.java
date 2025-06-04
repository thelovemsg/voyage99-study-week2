package kr.hhplus.be.server.ticket.application.usecase;

import kr.hhplus.be.server.ticket.application.dto.CancelTicketCommandDto;

public interface CancelTicketUseCase {
    CancelTicketCommandDto.Response complete(CancelTicketCommandDto.Request request);
}
