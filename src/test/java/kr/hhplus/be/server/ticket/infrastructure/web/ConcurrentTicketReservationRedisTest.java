package kr.hhplus.be.server.ticket.infrastructure.web;


import kr.hhplus.be.server.common.exceptions.ParameterNotValidException;
import kr.hhplus.be.server.common.utils.IdUtils;
import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.ReserveTicketCommandDto;
import kr.hhplus.be.server.ticket.application.ticket.service.redis.ReserveTicketRedisServiceImpl;
import kr.hhplus.be.server.ticket.domain.enums.TicketStatusEnum;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.domain.repository.TicketRepository;
import kr.hhplus.be.server.ticket.infrastructure.persistence.ticket.TicketRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest  // Spring Context 생성 (기존 Redis 설정 사용)
@DisplayName("동시성 티켓 예약 테스트")
class ConcurrentTicketReservationRedisTest {

    @MockitoBean
    private TicketRepositoryImpl ticketRepository;

    @Autowired
    private ReserveTicketRedisServiceImpl reserveTicketRedisService;

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
    @DisplayName("실제 DistributedLockTemplate을 이용한 동시성 테스트")
    void test_distributed_lock_with_real_redis() throws InterruptedException {

        // given
        Ticket ticket = tickets.get(0);

        // Repository Mock 설정
        when(ticketRepository.findById(ticket.getTicketId()))
                .thenReturn(Optional.of(ticket));

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when - 동시에 10명이 같은 티켓 예약 시도
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < 10; i++) {
            Long userId = (long) (i + 1);
            executor.submit(() -> {
                try {
                    ReserveTicketCommandDto.Request request =
                            createReserveRequest(ticket.getTicketId(), userId);

                    reserveTicketRedisService.reserve(request);
                    successCount.incrementAndGet();
                    System.out.println("사용자 " + userId + " 예약 성공");

                } catch (ParameterNotValidException e) {
                    // 락 획득 실패 (이미 예약됨)
                    failureCount.incrementAndGet();
                    System.out.println("사용자 " + userId + " 예약 실패: " + e.getMessage());
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    System.out.println("사용자 " + userId + " 예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // then - 분산 락이 정상 동작하면 1명만 성공해야 함
        System.out.println("성공: " + successCount.get() + ", 실패: " + failureCount.get());
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failureCount.get()).isEqualTo(9);

    }

    private ReserveTicketCommandDto.Request createReserveRequest(Long ticketId, Long userId) {
        ReserveTicketCommandDto.Request request = new ReserveTicketCommandDto.Request();
        request.setTicketId(ticketId);
        request.setUserId(userId);
        return request;
    }
}
