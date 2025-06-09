package kr.hhplus.be.server.ticket.application.port.ticket.in;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.PurchaseTicketCommandDto;

public interface PurchaseTicketUseCase {
    PurchaseTicketCommandDto.Response purchase(PurchaseTicketCommandDto.Request request);
}
