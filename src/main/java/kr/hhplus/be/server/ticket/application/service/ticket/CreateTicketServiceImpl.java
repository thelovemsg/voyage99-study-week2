package kr.hhplus.be.server.ticket.application.service.ticket;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.ticket.application.port.ticket.in.CreateTicketUseCase;
import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.CreateTicketCommandDto;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.ticket.infrastructure.persistence.ticket.TicketRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreateTicketServiceImpl implements CreateTicketUseCase {

    private final TicketRepositoryAdapter ticketRepositoryAdapter;

    @Override
    @Transactional
    public CreateTicketCommandDto.Response createTicket(CreateTicketCommandDto.Request request) {

        // TODO : 좌석 정보와 콘서트 스케줄이 미존재시 에러 반환 -> 후순위
        Long seatId = request.getSeatId();
        Long concertScheduleId = request.getConcertScheduleId();

        // TODO : 티켓 금액도 정책으로 조회해서 반환하는 로직 필요. -> 후순위 : 현재는 ticket을 생성하는데 집중
        BigDecimal totalAmount = new BigDecimal("10000");

        // TODO : 티켓 번호 자동 채번 기능 필요 -> JPA 를 활용해서 만들면 좋음
        Ticket unusedTicket = Ticket.createUnusedTicket(seatId, concertScheduleId, totalAmount);
        Ticket savedTicket = ticketRepositoryAdapter.save(unusedTicket);
        return CreateTicketCommandDto.Response.fromDomain(savedTicket);
    }
}
