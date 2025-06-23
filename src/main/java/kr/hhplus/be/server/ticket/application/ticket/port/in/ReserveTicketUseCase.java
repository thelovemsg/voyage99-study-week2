package kr.hhplus.be.server.ticket.application.ticket.port.in;

import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.ReserveTicketCommandDto;

public interface ReserveTicketUseCase {
    ReserveTicketCommandDto.Response reserve(ReserveTicketCommandDto.Request request);
}
