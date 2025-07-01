package kr.hhplus.be.server.concert.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@Data
public class RankingData {
    private Long concertScheduleId;
    private String concertTitle;
    private LocalDateTime soldOutTime;

    @JsonCreator
    public RankingData(
            @JsonProperty("concertScheduleId") Long concertScheduleId,
            @JsonProperty("concertTitle") String concertTitle,
            @JsonProperty("soldOutTime") LocalDateTime soldOutTime
    ) {
        this.concertScheduleId = concertScheduleId;
        this.concertTitle = concertTitle;
        this.soldOutTime = soldOutTime;
    }

}
