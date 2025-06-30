package kr.hhplus.be.server.concert.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RankingInfo {
    private int rank;
    private Long concertScheduleId;
    private String concertTitle;
    private LocalDateTime soldOutTime;
}
