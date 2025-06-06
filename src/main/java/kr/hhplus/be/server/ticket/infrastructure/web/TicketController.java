package kr.hhplus.be.server.ticket.infrastructure.web;

import kr.hhplus.be.server.ticket.application.port.in.dto.PurchaseTicketCommandDto;
import kr.hhplus.be.server.ticket.application.service.PurchaseTicketServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {

    private final PurchaseTicketServiceImpl purchaseTicketService;

    @PostMapping
    public ResponseEntity<PurchaseTicketCommandDto.Response> purchaseTicket(PurchaseTicketCommandDto.Request request) {
        PurchaseTicketCommandDto.Response purchase = purchaseTicketService.purchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }
}
