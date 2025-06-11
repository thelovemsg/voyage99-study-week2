package kr.hhplus.be.server.ticket.application.port.token.in.dto;

import kr.hhplus.be.server.ticket.infrastructure.persistence.token.TokenEntity;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueTokenCommandDto {

    @Getter
    @Setter
    public static class Request {
        private Long userId;
        private Long queueId;
        private Long concertScheduleId;

        public static TokenEntity toNewEntity(IssueTokenCommandDto.Request request) {
            return TokenEntity.builder()
                    .userId(request.getUserId())
                    .queueId(request.getQueueId())
                    .concertScheduleId(request.getConcertScheduleId())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String rawTokenId;

    }
}
