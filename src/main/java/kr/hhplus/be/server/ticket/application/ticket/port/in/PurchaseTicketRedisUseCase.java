package kr.hhplus.be.server.ticket.application.ticket.port.in;

import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.PurchaseTicketCommandDto;

public interface PurchaseTicketRedisUseCase {
    PurchaseTicketCommandDto.Response purchase(PurchaseTicketCommandDto.Request request);
}
