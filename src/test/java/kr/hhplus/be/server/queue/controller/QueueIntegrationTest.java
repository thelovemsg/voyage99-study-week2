package kr.hhplus.be.server.queue.controller;

import kr.hhplus.be.server.queue.repository.QueueRepository;
import kr.hhplus.be.server.queue.service.QueueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("동시성 티켓 예약 테스트")
public class QueueIntegrationTest {

    @Mock
    QueueRepository queueRepository;

    @InjectMocks
    QueueService queueService;


    void setup() {

    }
}
