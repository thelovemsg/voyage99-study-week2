package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.ticket.application.port.ticket.in.ReserveTicketUseCase;
import kr.hhplus.be.server.ticket.application.service.ticket.ReserveTicketAtomicServiceImpl;
import kr.hhplus.be.server.ticket.application.service.ticket.ReserveTicketServiceImpl;
import kr.hhplus.be.server.ticket.domain.repository.TicketRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class PerformanceTestConfig {

    @Bean
    @Primary
    public ReserveTicketUseCase pessimisticLockService(TicketRepository ticketRepository) {
        return new ReserveTicketServiceImpl(ticketRepository);
    }

    @Bean
    public ReserveTicketUseCase atomicUpdateService(TicketRepository ticketRepository) {
        return new ReserveTicketAtomicServiceImpl(ticketRepository);
    }
}
