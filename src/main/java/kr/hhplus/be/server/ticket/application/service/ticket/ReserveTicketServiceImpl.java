package kr.hhplus.be.server.ticket.application.service.ticket;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.ticket.application.port.ticket.in.ReserveTicketUseCase;
import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.ReserveTicketCommandDto;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.infrastructure.persistence.ticket.TicketRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveTicketServiceImpl implements ReserveTicketUseCase {

    private final TicketRepositoryAdapter ticketRepository;

    @Override
    @Transactional
    public ReserveTicketCommandDto.Response reserve(ReserveTicketCommandDto.Request request) {
        Long ticketId = request.getTicketId();
        Long userId = request.getUserId();

        Ticket ticket = ticketRepository.findByIdWithLock(ticketId)
                .orElseThrow(() -> new NotFoundException(MessageCode.TICKET_NOT_FOUND, ticketId));
        ticket.reserve(userId);

        return new ReserveTicketCommandDto.Response(Boolean.TRUE);
    }

}