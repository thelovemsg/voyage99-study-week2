package kr.hhplus.be.server.ticket.application.port.token.in.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidateTokenCommandDto {

    @Getter
    @Setter
    public static class Request {
        private String rawTokenId;
    }

    public static class Response {

    }
}
