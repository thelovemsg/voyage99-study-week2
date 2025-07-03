package kr.hhplus.be.server.ticket.application.ticket.service;

import kr.hhplus.be.server.common.exceptions.ParameterNotValidException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.common.redis.RedisCacheTemplate;
import kr.hhplus.be.server.common.redis.RedisKeyUtils;
import kr.hhplus.be.server.ticket.application.ticket.port.in.GetTicketUseCase;
import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.GetTicketCommandDto;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.infrastructure.persistence.ticket.TicketRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTicketCacheServiceImpl implements GetTicketUseCase {

    private final TicketRepositoryImpl ticketJpaRepository;
    private final RedisCacheTemplate cacheTemplate;

    @Override
    public GetTicketCommandDto.Response getTicket(Long ticketId) {
        String ticketInfoKey = RedisKeyUtils.getTicketInfoKey(ticketId);
        Ticket ticket = cacheTemplate.getOrSet(
                            ticketInfoKey
                            , () -> ticketJpaRepository.findById(ticketId).orElseThrow(() -> new ParameterNotValidException(MessageCode.TICKET_NOT_FOUND))
                            , Ticket.class
                        );
        return GetTicketCommandDto.Response.fromDomain(ticket);
    }
}
