package kr.hhplus.be.server.concert.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertCreateDto {

    @Getter
    @Setter
    public static class Request {
        private String description;
        private String artistName;
        private String concertName;
    }

    public static class Response {
        private Long concertId;
    }

}
