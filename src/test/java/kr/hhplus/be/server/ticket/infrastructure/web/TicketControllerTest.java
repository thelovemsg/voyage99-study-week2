package kr.hhplus.be.server.ticket.infrastructure.web;

import kr.hhplus.be.server.ticket.application.service.ticket.PurchaseTicketServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PurchaseTicketServiceImpl purchaseTicketService;

    @Test
    @DisplayName("티켓 구매 통합테스트")
    void purchaseTicket() {

    }
}