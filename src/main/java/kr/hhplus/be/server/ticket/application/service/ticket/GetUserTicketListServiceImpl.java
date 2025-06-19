package kr.hhplus.be.server.ticket.application.service.ticket;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.GetTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.ticket.in.GetUserTicketListUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserTicketListServiceImpl implements GetUserTicketListUseCase {

    @Override
    public List<GetTicketCommandDto.Response> getUserTickets(Long userId) {
        return null;
    }
}
