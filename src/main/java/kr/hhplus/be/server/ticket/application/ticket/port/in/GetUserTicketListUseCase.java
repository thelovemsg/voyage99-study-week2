package kr.hhplus.be.server.ticket.application.ticket.port.in;

import kr.hhplus.be.server.ticket.application.ticket.port.in.dto.GetTicketCommandDto;

import java.util.List;

public interface GetUserTicketListUseCase {
    List<GetTicketCommandDto.Response> getUserTickets(Long userId);
}
