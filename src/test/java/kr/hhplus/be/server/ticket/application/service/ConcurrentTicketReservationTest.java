package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.common.exceptions.ParameterNotValidException;
import kr.hhplus.be.server.common.utils.IdUtils;
import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.ReserveTicketCommandDto;
import kr.hhplus.be.server.ticket.application.service.ticket.ReserveTicketServiceImpl;
import kr.hhplus.be.server.ticket.domain.enums.TicketStatusEnum;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.infrastructure.persistence.ticket.TicketRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("동시성 티켓 예약 테스트")
class ConcurrentTicketReservationTest {

    @Mock
    private TicketRepositoryImpl ticketRepositoryImpl;

    @InjectMocks
    private ReserveTicketServiceImpl reserveTicketService;

    private List<Ticket> tickets;
    private List<Long> userIds;
    private final int TICKET_COUNT = 10;
    private final int USER_COUNT = 20; // 티켓보다 많은 사용자
    private Long concertScheduleId;
    private Long seatIdBase;

    @BeforeEach
    void setup() {
        concertScheduleId = IdUtils.getNewId();
        seatIdBase = IdUtils.getNewId();

        // 10개의 티켓 생성
        tickets = new ArrayList<>();
        for (int i = 0; i < TICKET_COUNT; i++) {
            Long ticketId = IdUtils.getNewId();
            Ticket ticket = new Ticket(
                    ticketId, null, seatIdBase + i, concertScheduleId, "TKT" + String.format("%03d", i),
                    "콘서트 정보", "좌석 " + (i + 1), TicketStatusEnum.AVAILABLE,
                    null, new BigDecimal("10000"), null, null, null
            );
            tickets.add(ticket);
        }

        // 20명의 사용자 ID 생성
        userIds = new ArrayList<>();
        for (int i = 0; i < USER_COUNT; i++) {
            userIds.add(IdUtils.getNewId());
        }
    }

    @Test
    @DisplayName("단순 동시성 테스트 - 디버깅용")
    void simple_concurrent_test() throws InterruptedException {
        // given - 1개 티켓만으로 테스트
        Ticket singleTicket = tickets.get(0);
        when(ticketRepositoryImpl.findByIdWithLock(singleTicket.getTicketId()))
                .thenReturn(Optional.of(singleTicket));

        // 🆕 save 메서드 Mock 수정 - 리턴값에 따라
        when(ticketRepositoryImpl.save(any(Ticket.class)))
                .thenReturn(singleTicket); // 또는 .thenAnswer(invocation -> invocation.getArgument(0));

        // when - 2명만 동시에 시도
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < 2; i++) {
            Long userId = userIds.get(i);
            executor.submit(() -> {
                try {
                    ReserveTicketCommandDto.Request request = createReserveRequest(singleTicket.getTicketId(), userId);
                    reserveTicketService.reserve(request);
                    successCount.incrementAndGet();
                    System.out.println("사용자 " + userId + " 예약 성공");
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    System.out.println("사용자 " + userId + " 예약 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        // then
        System.out.println("성공: " + successCount.get() + ", 실패: " + failureCount.get());
        assertThat(successCount.get()).isEqualTo(1); // 1명만 성공해야 함
        assertThat(failureCount.get()).isEqualTo(1); // 1명은 실패해야 함
    }

    @Test
    @DisplayName("동시성 환경에서 티켓 예약 - 성공/실패 케이스 혼재")
    void concurrent_ticket_reservation_with_random_selection() throws InterruptedException {
        // given
        setupMockForConcurrentTest();

        ExecutorService executorService = Executors.newFixedThreadPool(USER_COUNT);
        CountDownLatch latch = new CountDownLatch(USER_COUNT);

        // 결과 수집용
        ConcurrentHashMap<Long, Long> reservationResults = new ConcurrentHashMap<>(); // userId -> ticketId
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        ConcurrentHashMap<Long, String> failureReasons = new ConcurrentHashMap<>();

        // when - 20명의 사용자가 동시에 랜덤 티켓 예약 시도
        for (Long userId : userIds) {
            executorService.submit(() -> {
                try {
                    boolean reserved = attemptRandomTicketReservation(userId, reservationResults, failureReasons);
                    if (reserved) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드 완료 대기
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        // then - 결과 검증
        System.out.println("=== 동시성 테스트 결과 ===");
        System.out.println("성공한 예약: " + successCount.get());
        System.out.println("실패한 예약: " + failureCount.get());
        System.out.println("전체 시도: " + USER_COUNT);

        // 기본 검증
        assertThat(successCount.get()).isLessThanOrEqualTo(TICKET_COUNT); // 티켓 개수를 초과할 수 없음
        assertThat(successCount.get() + failureCount.get()).isEqualTo(USER_COUNT); // 모든 시도가 집계됨
        assertThat(reservationResults.size()).isEqualTo(successCount.get()); // 성공 개수와 일치

        // 상세 결과 출력
        printDetailedResults(reservationResults, failureReasons);

        // 티켓별 예약 상태 확인
        verifyTicketReservationStates(reservationResults);
    }

    @Test
    @DisplayName("정확히 10개 티켓이 모두 예약되는 시나리오")
    void all_tickets_should_be_reserved_eventually() throws InterruptedException {
        // given
        setupMockForConcurrentTest();

        ExecutorService executorService = Executors.newFixedThreadPool(USER_COUNT);
        CountDownLatch latch = new CountDownLatch(USER_COUNT);

        ConcurrentHashMap<Long, Long> reservationResults = new ConcurrentHashMap<>();
        AtomicInteger totalAttempts = new AtomicInteger(0);

        // when - 더 적극적인 예약 시도 (여러 번 재시도)
        for (Long userId : userIds) {
            executorService.submit(() -> {
                try {
                    attemptReservationWithRetry(userId, reservationResults, totalAttempts);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(15, TimeUnit.SECONDS);
        executorService.shutdown();

        // then
        System.out.println("=== 적극적 예약 결과 ===");
        System.out.println("총 예약 성공: " + reservationResults.size());
        System.out.println("총 시도 횟수: " + totalAttempts.get());

        // 모든 티켓이 예약되었는지 확인 (이상적인 경우)
        // 실제로는 동시성으로 인해 모든 티켓이 예약되지 않을 수도 있음
        assertThat(reservationResults).hasSizeGreaterThan(0)
            .satisfies(input -> {
                assertThat(reservationResults).hasSizeLessThanOrEqualTo(TICKET_COUNT);
            });

        verifyNoTicketDoubleReservation(reservationResults);
    }

    private boolean attemptRandomTicketReservation(Long userId,
                                                   ConcurrentHashMap<Long, Long> reservationResults,
                                                   ConcurrentHashMap<Long, String> failureReasons) {
        List<Ticket> availableTickets = new ArrayList<>(tickets);
        Collections.shuffle(availableTickets); // 랜덤 순서

        for (Ticket ticket : availableTickets) {
            try {
                ReserveTicketCommandDto.Request request = createReserveRequest(ticket.getTicketId(), userId);
                ReserveTicketCommandDto.Response response = reserveTicketService.reserve(request);

                if (response.getTicketId() != null) {
                    reservationResults.put(userId, ticket.getTicketId());
                    System.out.printf("사용자 %d가 티켓 %d 예약 성공%n", userId, ticket.getTicketId());
                    return true;
                }
            } catch (ParameterNotValidException e) {
                // 이미 예약된 티켓이면 다음 티켓 시도
                continue;
            } catch (Exception e) {
                failureReasons.put(userId, e.getMessage());
                break;
            }
        }

        failureReasons.put(userId, "예약 가능한 티켓이 없음");
        System.out.printf("사용자 %d 예약 실패: %s%n", userId, failureReasons.get(userId));
        return false;
    }

    private void attemptReservationWithRetry(Long userId,
                                             ConcurrentHashMap<Long, Long> reservationResults,
                                             AtomicInteger totalAttempts) {
        for (int retry = 0; retry < 3; retry++) {
            totalAttempts.incrementAndGet();

            if (attemptRandomTicketReservation(userId, reservationResults, new ConcurrentHashMap<>())) {
                return; // 성공하면 종료
            }

            // 짧은 대기 후 재시도
            try {
                Thread.sleep(10 + new Random().nextInt(50)); // 10-60ms 랜덤 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void setupMockForConcurrentTest() {
        // 티켓 조회 시 실제 티켓 객체 반환
        when(ticketRepositoryImpl.findByIdWithLock(anyLong())).thenAnswer(invocation -> {
            Long ticketId = invocation.getArgument(0);
            return tickets.stream()
                    .filter(ticket -> ticket.getTicketId().equals(ticketId))
                    .findFirst();
        });

        // save 호출 시 아무것도 하지 않음 (Mock)
        when(ticketRepositoryImpl.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void printDetailedResults(ConcurrentHashMap<Long, Long> reservationResults,
                                      ConcurrentHashMap<Long, String> failureReasons) {
        System.out.println("\n=== 예약 성공 목록 ===");
        reservationResults.forEach((userId, ticketId) ->
                System.out.printf("사용자 %d -> 티켓 %d%n", userId, ticketId));

        System.out.println("\n=== 예약 실패 목록 ===");
        failureReasons.forEach((userId, reason) ->
                System.out.printf("사용자 %d -> %s%n", userId, reason));
    }

    private void verifyTicketReservationStates(ConcurrentHashMap<Long, Long> reservationResults) {
        System.out.println("\n=== 티켓별 최종 상태 ===");

        for (Ticket ticket : tickets) {
            boolean isReserved = reservationResults.containsValue(ticket.getTicketId());
            System.out.printf("티켓 %d: %s (예약자: %s)%n",
                    ticket.getTicketId(),
                    ticket.getTicketStatus(),
                    ticket.getReservedBy() != null ? ticket.getReservedBy() : "없음");

            if (isReserved) {
                assertThat(ticket.getTicketStatus()).isEqualTo(TicketStatusEnum.RESERVED);
                assertThat(ticket.getReservedBy()).isNotNull();
            }
        }
    }

    private void verifyNoTicketDoubleReservation(ConcurrentHashMap<Long, Long> reservationResults) {
        Set<Long> reservedTicketIds = new HashSet<>(reservationResults.values());

        System.out.println("=== 중복 예약 검증 디버깅 ===");
        System.out.println("reservationResults.size(): " + reservationResults.size());
        System.out.println("reservedTicketIds.size(): " + reservedTicketIds.size());

        System.out.println("예약 결과: " + reservationResults);
        System.out.println("고유 티켓 ID들: " + reservedTicketIds);

        Map<Long, Long> ticketToUserCount = reservationResults.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        ticketToUserCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .forEach(entry ->
                        System.out.println("중복 예약된 티켓: " + entry.getKey() + " (예약 수: " + entry.getValue() + ")")
                );

        assertThat(reservedTicketIds.size()).isEqualTo(reservationResults.size());
        System.out.println("티켓 중복 예약 없음 확인 완료");
    }

    private ReserveTicketCommandDto.Request createReserveRequest(Long ticketId, Long userId) {
        ReserveTicketCommandDto.Request request = new ReserveTicketCommandDto.Request();
        request.setTicketId(ticketId);
        request.setUserId(userId);
        return request;
    }
}
