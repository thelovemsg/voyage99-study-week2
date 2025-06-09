package kr.hhplus.be.server.ticket.application.port.token.in;

public interface ValidateTokenUseCase {
    Boolean validateToken(String rawTokenId);
}
