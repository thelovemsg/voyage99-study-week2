package kr.hhplus.be.server.ticket.infrastructure.web;

import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.CreateTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.GetTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.PurchaseTicketCommandDto;
import kr.hhplus.be.server.ticket.application.port.ticket.in.dto.ReserveTicketCommandDto;
import kr.hhplus.be.server.ticket.application.service.ticket.CreateTicketServiceImpl;
import kr.hhplus.be.server.ticket.application.service.ticket.GetTicketServiceImpl;
import kr.hhplus.be.server.ticket.application.service.ticket.PurchaseTicketServiceImpl;
import kr.hhplus.be.server.ticket.application.service.ticket.ReserveTicketServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {

    private final GetTicketServiceImpl getTicketService;
    private final CreateTicketServiceImpl createTicketService;
    private final PurchaseTicketServiceImpl purchaseTicketService;
    private final ReserveTicketServiceImpl reserveTicketService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<GetTicketCommandDto.Response> getTicket(@PathVariable("ticketId") Long ticketId) {
        GetTicketCommandDto.Response ticketResponse = getTicketService.getTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(ticketResponse);
    }

    @PostMapping
    public ResponseEntity<CreateTicketCommandDto.Response> createTicket(@RequestBody CreateTicketCommandDto.Request request) {
        CreateTicketCommandDto.Response createResponse = createTicketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseTicketCommandDto.Response> purchaseTicket(@RequestBody PurchaseTicketCommandDto.Request request) throws Exception {
        PurchaseTicketCommandDto.Response purchase = purchaseTicketService.purchaseWithPessimicticLock(request);
        return ResponseEntity.status(HttpStatus.OK).body(purchase);
    }

    @PatchMapping("/reserve")
    public ResponseEntity<ReserveTicketCommandDto.Response> reserveTicket(@RequestBody ReserveTicketCommandDto.Request request) {
        ReserveTicketCommandDto.Response reserve = reserveTicketService.reserve(request);
        return ResponseEntity.status(HttpStatus.OK).body(reserve);
    }
}
