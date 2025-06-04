package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.ticket.application.dto.GetTicketCommandDto;
import kr.hhplus.be.server.ticket.application.usecase.GetUserTicketListUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserTicketListService implements GetUserTicketListUseCase {

    @Override
    public List<GetTicketCommandDto.Response> getUserTickets(Long userId) {
        return null;
    }
}
