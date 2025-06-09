package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.common.utils.IdUtils;
import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.ReserveTicketCommandDto;
import kr.hhplus.be.server.ticket.application.service.ticket.ReserveTicketServiceImpl;
import kr.hhplus.be.server.ticket.domain.enums.TicketStatusEnum;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.infrastructure.persistence.ticket.TicketRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReserveTicketServiceImpl 테스트")
class ReserveTicketServiceImplTest {

    @Mock
    private TicketRepositoryAdapter ticketRepository;

    @InjectMocks
    private ReserveTicketServiceImpl reserveTicketService;

    private Long ticketId;
    private Long userId;
    private Long seatId;
    private Long concertScheduleId;
    private Ticket ticket;
    private ReserveTicketCommandDto.Request request;

    @BeforeEach
    void setup() {
        this.ticketId = IdUtils.getNewId();
        this.userId = IdUtils.getNewId();
        this.seatId = IdUtils.getNewId();
        this.concertScheduleId = IdUtils.getNewId();

        ticket = new Ticket(
                ticketId, userId, seatId, concertScheduleId, "TKT001",
                "콘서트 정보", "좌석 정보", TicketStatusEnum.AVAILABLE,
                null, new BigDecimal("10000"), null, null
        );

        request = new ReserveTicketCommandDto.Request();
        request.setTicketId(ticketId);
        request.setUserId(userId);

    }

    @Test
    @DisplayName("티켓 구매 전 예약 테스트")
    void reserve_ticket() {
        //given
        Mockito.when(ticketRepository.findByIdWithLock(ticketId)).thenReturn(Optional.of(ticket));

        //when
        ReserveTicketCommandDto.Response reservedResponse = reserveTicketService.reserve(request);

        //then
        assertThat(reservedResponse.isSuccess()).isEqualTo(Boolean.TRUE);
    }

}