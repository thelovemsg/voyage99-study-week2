package kr.hhplus.be.server.ticket.application.usecase;

import kr.hhplus.be.server.ticket.application.dto.PurchaseTicketCommandDto;

public interface PurchaseTicketUseCase {
    PurchaseTicketCommandDto.Response purchase(PurchaseTicketCommandDto.Request request);
}
