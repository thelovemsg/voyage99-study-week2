package kr.hhplus.be.server.queue.controller.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueueCreateCommandDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long userId;
        private Long concertScheduleId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long queueId;
        private Long userId;
        private String status;
        private String message;
        private LocalDateTime createdAt;
    }
}
