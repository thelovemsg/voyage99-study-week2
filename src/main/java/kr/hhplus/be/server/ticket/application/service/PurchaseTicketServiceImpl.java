package kr.hhplus.be.server.ticket.application.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.common.exceptions.ParameterNotValidException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.concert.domain.ConcertScheduleEntity;
import kr.hhplus.be.server.concert.repository.ConcertScheduleRepository;
import kr.hhplus.be.server.ticket.application.port.in.PurchaseTicketUseCase;
import kr.hhplus.be.server.ticket.application.port.in.dto.PurchaseTicketCommandDto;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.domain.repository.TicketRepository;
import kr.hhplus.be.server.ticket.domain.service.TicketDomainService;
import kr.hhplus.be.server.user.domain.UserEntity;
import kr.hhplus.be.server.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ConcertScheduleRepository concertScheduleRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public PurchaseTicketCommandDto.Response purchase(PurchaseTicketCommandDto.Request request) {
        Long userId = request.getUserId();
        Long concertScheduleId = request.getConcertScheduleId();
        Long ticketId = request.getTicketId();

        // 1. 사용자 정보 조회 및 검증
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageCode.USER_NOT_FOUND, userId));

        // 2. 콘서트 스케줄 조회 및 검증
        ConcertScheduleEntity schedule = concertScheduleRepository.findById(concertScheduleId)
                .orElseThrow(() -> new NotFoundException(MessageCode.CONCERT_SCHEDULE_NOT_FOUND, concertScheduleId));

        // 3. 티켓 조회 (락 사용으로 동시성 제어)
        Ticket ticket = ticketRepository.findByIdWithLock(ticketId)
                .orElseThrow(() -> new NotFoundException(MessageCode.TICKET_NOT_FOUND, ticketId));

        // 4. 도메인 검증들
        ticketDomainService.validateUserHasEnoughPoint(user, request.getUseAmount());
        ticketDomainService.validateConcertScheduleAvailable(schedule);
        ticketDomainService.validateTicketCanBeReserved(ticket, userId);

        // 5. 비즈니스 로직 실행
        ticket.reserve(userId);
        user.usePoint(request.getUseAmount());

        // 6. 변경사항 저장
        ticketRepository.save(ticket);
        userRepository.save(user);

        return PurchaseTicketCommandDto.Response.builder()
                .ticketId(ticketId)
                .isSuccess(Boolean.TRUE)
                .build();
    }
}