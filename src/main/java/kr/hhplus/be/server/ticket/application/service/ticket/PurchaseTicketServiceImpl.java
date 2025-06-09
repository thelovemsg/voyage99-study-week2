package kr.hhplus.be.server.ticket.application.service.ticket;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.ticket.application.port.ticket.in.PurchaseTicketUseCase;
import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.PurchaseTicketCommandDto;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.domain.service.TicketDomainService;
import kr.hhplus.be.server.ticket.infrastructure.persistence.ticket.TicketRepositoryAdapter;
import kr.hhplus.be.server.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 좌석 정보도 현재 관리하려고 했는데,
 * 시간이 없는 관계로 핵심만 작성 => ticket
 */
@Service
@RequiredArgsConstructor
public class PurchaseTicketServiceImpl implements PurchaseTicketUseCase {

    private final TicketDomainService ticketDomainService;
    private final TicketRepositoryAdapter ticketRepositoryAdapter;

    @Override
    @Transactional
    public PurchaseTicketCommandDto.Response purchase(PurchaseTicketCommandDto.Request request) {
        Long userId = request.getUserId();
        Long concertScheduleId = request.getConcertScheduleId();
        Long ticketId = request.getTicketId();

        // 3. 티켓 조회 (락 사용으로 동시성 제어)
        Ticket ticket = ticketRepositoryAdapter.findByIdWithLock(ticketId)
                .orElseThrow(() -> new NotFoundException(MessageCode.TICKET_NOT_FOUND, ticketId));

        // 4. 도메인 검증들
        UserEntity userEntity = ticketDomainService.validateUserHasEnoughPoint(userId, request.getUseAmount());
        ticketDomainService.validateConcertScheduleAvailable(concertScheduleId);
        ticketDomainService.validateTicketCanBeReserved(ticket, userId);

        // 5. 비즈니스 로직 실행
        ticketDomainService.useUserPoint(userEntity, request.getUseAmount());

        // 6. 저장
        ticketRepositoryAdapter.save(ticket);

        return PurchaseTicketCommandDto.Response.builder()
                .ticketId(ticketId)
                .isSuccess(Boolean.TRUE)
                .build();
    }

}