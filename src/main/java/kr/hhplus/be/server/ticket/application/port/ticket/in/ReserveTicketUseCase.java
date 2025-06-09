package kr.hhplus.be.server.ticket.application.port.ticket.in;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.ReserveTicketCommandDto;

public interface ReserveTicketUseCase {
    ReserveTicketCommandDto.Response reserve(ReserveTicketCommandDto.Request request);
}
