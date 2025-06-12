package kr.hhplus.be.server.ticket.application.port.ticket.in;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.GetTicketCommandDto;

import java.util.List;

public interface GetUserTicketListUseCase {
    List<GetTicketCommandDto.Response> getUserTickets(Long userId);
}
