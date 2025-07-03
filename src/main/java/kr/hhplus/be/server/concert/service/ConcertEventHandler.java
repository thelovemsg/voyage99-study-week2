package kr.hhplus.be.server.concert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.common.event.ConcertSoldOutEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertEventHandler {

    private final ConcertScheduleService concertScheduleService;
    private final RankingService rankingService;

    @EventListener
    @Async
    public void handleConcertSoldOut(ConcertSoldOutEvent event) throws JsonProcessingException {
        concertScheduleService.markAsSoldOut(event.concertScheduleId());
        rankingService.addToRanking(event.concertScheduleId(), event.concertInfo(), event.soldOutDatetime());
    }
}
