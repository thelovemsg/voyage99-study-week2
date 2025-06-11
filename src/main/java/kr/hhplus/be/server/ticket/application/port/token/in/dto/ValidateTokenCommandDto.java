package kr.hhplus.be.server.ticket.application.port.token.in.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidateTokenCommandDto {

    @Getter
    @Setter
    public static class Request {
        private String rawTokenId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Boolean isValidToken;
    }
}
