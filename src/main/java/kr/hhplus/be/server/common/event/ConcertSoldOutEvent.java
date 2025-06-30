package kr.hhplus.be.server.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ConcertSoldOutEvent {
    private final Long concertScheduleId;
    private final String concertInfo;
    private final LocalDateTime soldOutDatetime;
}
