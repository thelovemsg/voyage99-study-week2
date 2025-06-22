package kr.hhplus.be.server.ticket.application.ticket.port.in;

import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.PurchaseTicketCommandDto;

public interface PurchaseTicketPessimisticLockUseCase {
    PurchaseTicketCommandDto.Response purchaseWithPessimisticLock(PurchaseTicketCommandDto.Request request);
}
