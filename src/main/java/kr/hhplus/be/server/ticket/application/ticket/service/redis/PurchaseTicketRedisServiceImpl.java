package kr.hhplus.be.server.ticket.application.ticket.service.redis;

import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.common.exceptions.TicketPurchaseException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.redis.service.DistributedLockTemplate;
import kr.hhplus.be.server.redis.utils.RedisKeyUtils;
import kr.hhplus.be.server.ticket.application.ticket.port.in.PurchaseTicketRedisUseCase;
import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.PurchaseTicketCommandDto;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.domain.repository.TicketRepository;
import kr.hhplus.be.server.ticket.domain.service.TicketDomainService;
import kr.hhplus.be.server.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 좌석 정보도 현재 관리하려고 했는데,
 * 시간이 없는 관계로 핵심만 작성 => ticket
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseTicketRedisServiceImpl implements PurchaseTicketRedisUseCase {

    private final DistributedLockTemplate lockTemplate;
    private final TicketDomainService ticketDomainService;
    private final TicketRepository ticketRepository;

    @Override
    public PurchaseTicketCommandDto.Response purchase(PurchaseTicketCommandDto.Request request) {
        Long userId = request.getUserId();
        Long concertScheduleId = request.getConcertScheduleId();
        Long ticketId = request.getTicketId();

        String lockKey = RedisKeyUtils.getTicketReverseLockKey(ticketId);

        return lockTemplate.executeWithLock(
                lockKey,
                () -> {
                    Ticket ticket = ticketRepository.findById(ticketId)
                            .orElseThrow(() -> new NotFoundException(MessageCode.TICKET_NOT_FOUND, ticketId));

                // 2. 도메인 검증들
                    ticketDomainService.validateConcertScheduleAvailable(concertScheduleId);
                    ticketDomainService.validateTicketCanBeReserved(ticket, userId);

                    // 3. 비즈니스 로직 실행
                    UserEntity userEntity = ticketDomainService.validateUserHasEnoughPoint(userId, request.getUseAmount());
                    ticketDomainService.useUserPoint(userEntity, request.getUseAmount());

                    // 4. 낙관적 업데이트 시도 (여기서 동시성 제어)
                    ticket.completePurchase(userId); // 상태를 PAID로 변경
                    int updatedRows = ticketRepository.updateWithOptimisticLock(ticket);

                    // 5. 업데이트 실패 시 예외 발생 (다른 사용자가 먼저 구매)
                    if (updatedRows == 0) {
                        throw new TicketPurchaseException(MessageCode.TICKET_PURCHASE_ERROR, ticketId);
                    }

                    return PurchaseTicketCommandDto.Response.builder()
                            .ticketId(ticketId)
                            .isSuccess(Boolean.TRUE)
                            .build();
                },
                () -> new TicketPurchaseException(MessageCode.TICKET_PURCHASE_ERROR, ticketId)
        );
    }

}