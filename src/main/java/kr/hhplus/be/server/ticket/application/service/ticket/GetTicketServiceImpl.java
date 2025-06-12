package kr.hhplus.be.server.ticket.application.service.ticket;

import kr.hhplus.be.server.common.exceptions.ParameterNotValidException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.ticket.application.port.ticket.in.GetTicketUseCase;
import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.GetTicketCommandDto;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.infrastructure.persistence.ticket.TicketRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTicketServiceImpl implements GetTicketUseCase {

    private final TicketRepositoryAdapter ticketJpaRepository;

    @Override
    public GetTicketCommandDto.Response getTicket(Long ticketId) {
        Ticket ticket = ticketJpaRepository.findById(ticketId).orElseThrow(() -> new ParameterNotValidException(MessageCode.TICKET_NOT_FOUND));
        // TODO : 현재 로그인한 사람이 실제로 ticket을 구매한 사람인지 확인해야함
        //      ticket.isReservedByOther(userId);
        return GetTicketCommandDto.Response.fromDomain(ticket);
    }
}
