package kr.hhplus.be.server.ticket.application.ticket.port.in;

import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.PurchaseTicketCommandDto;

public interface PurchaseTicketUseCase {
    PurchaseTicketCommandDto.Response purchaseWithPessimicticLock(PurchaseTicketCommandDto.Request request) throws Exception;
    PurchaseTicketCommandDto.Response purchaseWithUpdateLock(PurchaseTicketCommandDto.Request request) throws Exception;
}
