package kr.hhplus.be.server.ticket.domain.repository;

import kr.hhplus.be.server.ticket.domain.model.Token;

import java.util.Optional;

public interface TokenRepository {
    Optional<Token> findById(Long id);
    void save(Token token);
}
