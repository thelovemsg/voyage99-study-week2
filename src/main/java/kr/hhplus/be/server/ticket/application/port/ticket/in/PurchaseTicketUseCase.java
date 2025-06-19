package kr.hhplus.be.server.ticket.application.port.ticket.in;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.PurchaseTicketCommandDto;

public interface PurchaseTicketUseCase {
    PurchaseTicketCommandDto.Response purchaseWithPessimicticLock(PurchaseTicketCommandDto.Request request) throws Exception;
    PurchaseTicketCommandDto.Response purchaseWithUpdateLock(PurchaseTicketCommandDto.Request request) throws Exception;
}
