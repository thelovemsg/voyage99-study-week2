package kr.hhplus.be.server.ticket.application.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.ticket.application.dto.PurchaseTicketCommandDto;
import kr.hhplus.be.server.ticket.application.usecase.PurchaseTicketUseCase;
import org.springframework.stereotype.Service;

@Service
public class PurchaseTicketService implements PurchaseTicketUseCase {
    @Override
    public PurchaseTicketCommandDto.Response purchase(PurchaseTicketCommandDto.Request request) {
        return null;
    }
}
