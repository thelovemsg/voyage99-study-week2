package kr.hhplus.be.server.concert.controller.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class RankingInfo {
    private int rank;
    private Long concertScheduleId;
    private String concertTitle;
    private LocalDateTime soldOutTime;
}
