package kr.hhplus.be.server.ticket.infrastructure.web;

import kr.hhplus.be.server.ticket.application.port.in.dto.GetTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.in.dto.PurchaseTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.in.dto.ReserveTicketCommandDto;
import kr.hhplus.be.server.ticket.application.service.GetTicketServiceImpl;
import kr.hhplus.be.server.ticket.application.service.PurchaseTicketServiceImpl;
import kr.hhplus.be.server.ticket.application.service.ReserveTicketServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {

    private final GetTicketServiceImpl getTicketService;
    private final PurchaseTicketServiceImpl purchaseTicketService;
    private final ReserveTicketServiceImpl reserveTicketService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<GetTicketCommandDto.Response> getTicket(@PathVariable("ticketId") Long ticketId) {
        GetTicketCommandDto.Response ticketResponse = getTicketService.getTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(ticketResponse);
    }

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseTicketCommandDto.Response> purchaseTicket(PurchaseTicketCommandDto.Request request) {
        PurchaseTicketCommandDto.Response purchase = purchaseTicketService.purchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReserveTicketCommandDto.Response> purchaseTicket(ReserveTicketCommandDto.Request request) {
        ReserveTicketCommandDto.Response reserve = reserveTicketService.reserve(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserve);
    }
}
