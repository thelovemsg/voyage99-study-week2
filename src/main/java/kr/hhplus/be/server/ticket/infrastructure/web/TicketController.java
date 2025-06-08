package kr.hhplus.be.server.ticket.infrastructure.web;

import kr.hhplus.be.server.ticket.application.port.in.dto.GetTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.in.dto.PurchaseTicketCommandDto;
import kr.hhplus.be.server.ticket.application.service.GetTicketServiceImpl;
import kr.hhplus.be.server.ticket.application.service.PurchaseTicketServiceImpl;
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

    @GetMapping("/{id}")
    public ResponseEntity<GetTicketCommandDto.Response> getTicket(@PathVariable("id") Long id) {
        GetTicketCommandDto.Response ticketResponse = getTicketService.getTicket(id);
        return ResponseEntity.status(HttpStatus.OK).body(ticketResponse);
    }

    @PostMapping
    public ResponseEntity<PurchaseTicketCommandDto.Response> purchaseTicket(PurchaseTicketCommandDto.Request request) {
        PurchaseTicketCommandDto.Response purchase = purchaseTicketService.purchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }
}
