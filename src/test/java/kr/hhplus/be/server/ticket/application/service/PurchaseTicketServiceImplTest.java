package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.common.exceptions.ReservationNotValidException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.common.utils.IdUtils;
import kr.hhplus.be.server.concert.domain.ConcertScheduleEntity;
import kr.hhplus.be.server.concert.enums.CommonStatusEnum;
import kr.hhplus.be.server.concert.repository.ConcertScheduleRepository;
import kr.hhplus.be.server.ticket.application.port.in.dto.PurchaseTicketCommandDto;
import kr.hhplus.be.server.ticket.domain.enums.TicketStatusEnum;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.domain.repository.TicketRepository;
import kr.hhplus.be.server.ticket.domain.service.TicketDomainService;
import kr.hhplus.be.server.user.domain.UserEntity;
import kr.hhplus.be.server.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PurchaseTicketServiceImpl 테스트")
class PurchaseTicketServiceImplTest {

    @Mock
    private TicketDomainService ticketDomainService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConcertScheduleRepository concertScheduleRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private PurchaseTicketServiceImpl purchaseTicketService;


    // 공통 테스트 데이터
    private Long userId;
    private Long concertScheduleId;
    private Long ticketId;
    private BigDecimal useAmount;
    private PurchaseTicketCommandDto.Request request;
    private UserEntity mockUser;
    private ConcertScheduleEntity mockSchedule;
    private Ticket mockTicket;
    private Ticket savedTicket;

    @BeforeEach
    void setUp() {
        // 기본 테스트 데이터 설정
        userId = IdUtils.getNewId();
        concertScheduleId = IdUtils.getNewId();
        ticketId = IdUtils.getNewId();
        useAmount = new BigDecimal("50000");

        // Request 객체 생성
        request = PurchaseTicketCommandDto.Request.builder()
                .userId(userId)
                .concertScheduleId(concertScheduleId)
                .ticketId(ticketId)
                .useAmount(useAmount)
                .build();

        // Mock 객체들 생성
        mockUser = UserEntity.builder()
                .userId(userId)
                .pointAmount(new BigDecimal("100000"))
                .build();

        mockSchedule = ConcertScheduleEntity.builder()
                .concertScheduleId(concertScheduleId)
                .scheduleStatus(CommonStatusEnum.READY)
                .build();

        mockTicket = new Ticket(
                ticketId, null, 1L, concertScheduleId, "TKT001",
                "콘서트 정보", "좌석 정보", TicketStatusEnum.AVAILABLE,
                null, useAmount, null, null
        );

        savedTicket = new Ticket(
                ticketId, userId, 1L, concertScheduleId, "TKT001",
                "콘서트 정보", "좌석 정보", TicketStatusEnum.RESERVED,
                null, useAmount, LocalDateTime.now().plusMinutes(5), userId
        );
    }


    @Test
    @DisplayName("티켓 구매 성공 테스트")
    void purchase_Success() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(concertScheduleRepository.findById(concertScheduleId)).thenReturn(Optional.of(mockSchedule));
        when(ticketRepository.findByIdWithLock(ticketId)).thenReturn(Optional.of(mockTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        doNothing().when(ticketDomainService).validateUserHasEnoughPoint(any(), any());
        doNothing().when(ticketDomainService).validateConcertScheduleAvailable(any());
        doNothing().when(ticketDomainService).validateTicketCanBeReserved(any(), any());

        // When
        PurchaseTicketCommandDto.Response response = purchaseTicketService.purchase(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTicketId()).isEqualTo(ticketId);
        assertThat(response.isSuccess()).isTrue();

        verify(userRepository).findById(userId);
        verify(concertScheduleRepository).findById(concertScheduleId);
        verify(ticketRepository).findByIdWithLock(ticketId);
        verify(ticketDomainService).validateUserHasEnoughPoint(mockUser, useAmount);
        verify(ticketDomainService).validateConcertScheduleAvailable(mockSchedule);
        verify(ticketDomainService).validateTicketCanBeReserved(mockTicket, userId);
        verify(ticketRepository).save(any(Ticket.class));
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("사용자를 찾을 수 없는 경우 예외 발생")
    void purchase_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> purchaseTicketService.purchase(request))
                .isInstanceOf(NotFoundException.class);

        verify(userRepository).findById(userId);
        verifyNoInteractions(concertScheduleRepository, ticketRepository, ticketDomainService);
    }

    @Test
    @DisplayName("콘서트 스케줄을 찾을 수 없는 경우 예외 발생")
    void purchase_ConcertScheduleNotFound_ThrowsException() {
        // Given
        Long userId = 1L;
        Long concertScheduleId = 999L;

        PurchaseTicketCommandDto.Request request = PurchaseTicketCommandDto.Request.builder()
                .userId(userId)
                .concertScheduleId(concertScheduleId)
                .ticketId(1L)
                .useAmount(new BigDecimal("50000"))
                .build();

        UserEntity mockUser = UserEntity.builder()
                .userId(userId)
                .pointAmount(new BigDecimal("100000"))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(concertScheduleRepository.findById(concertScheduleId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> purchaseTicketService.purchase(request))
                .isInstanceOf(NotFoundException.class);

        verify(userRepository).findById(userId);
        verify(concertScheduleRepository).findById(concertScheduleId);
        verifyNoInteractions(ticketRepository, ticketDomainService);
    }

    @Test
    @DisplayName("티켓을 찾을 수 없는 경우 예외 발생")
    void purchase_TicketNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(concertScheduleRepository.findById(concertScheduleId)).thenReturn(Optional.of(mockSchedule));
        when(ticketRepository.findByIdWithLock(ticketId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> purchaseTicketService.purchase(request))
                .isInstanceOf(NotFoundException.class);

        verify(userRepository).findById(userId);
        verify(concertScheduleRepository).findById(concertScheduleId);
        verify(ticketRepository).findByIdWithLock(ticketId);
        verifyNoInteractions(ticketDomainService);
    }

    @Test
    @DisplayName("포인트 부족으로 구매 실패")
    void purchase_InsufficientPoint_ThrowsException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(concertScheduleRepository.findById(concertScheduleId)).thenReturn(Optional.of(mockSchedule));
        when(ticketRepository.findByIdWithLock(ticketId)).thenReturn(Optional.of(mockTicket));

        doThrow(new ReservationNotValidException(MessageCode.USER_POINT_NOT_ENOUGH))
                .when(ticketDomainService).validateUserHasEnoughPoint(mockUser, useAmount);

        // When & Then
        assertThatThrownBy(() -> purchaseTicketService.purchase(request))
                .isInstanceOf(RuntimeException.class);

        verify(ticketDomainService).validateUserHasEnoughPoint(mockUser, useAmount);
        verify(ticketRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

}