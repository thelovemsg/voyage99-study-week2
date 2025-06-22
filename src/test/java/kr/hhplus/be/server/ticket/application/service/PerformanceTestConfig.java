package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.ticket.application.ticket.port.in.ReserveTicketUseCase;
import kr.hhplus.be.server.ticket.application.ticket.service.ReserveTicketAtomicServiceImpl;
import kr.hhplus.be.server.ticket.application.ticket.service.ReserveTicketServiceImpl;
import kr.hhplus.be.server.ticket.domain.repository.TicketRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class PerformanceTestConfig {

    @Bean
    @Primary
    public ReserveTicketUseCase reserveTicketService(TicketRepository ticketRepository) {
        return new ReserveTicketServiceImpl(ticketRepository);
    }

    @Bean
    public ReserveTicketUseCase reserveTicketAtomicService(TicketRepository ticketRepository) {
        return new ReserveTicketAtomicServiceImpl(ticketRepository);
    }
}
