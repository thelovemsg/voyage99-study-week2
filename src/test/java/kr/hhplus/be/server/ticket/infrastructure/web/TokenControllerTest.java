package kr.hhplus.be.server.ticket.infrastructure.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("토큰 생성")
    void issueToken() {
        
    }
}
