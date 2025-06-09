package kr.hhplus.be.server.ticket.infrastructure.persistence.token;

import kr.hhplus.be.server.ticket.domain.model.Token;
import kr.hhplus.be.server.ticket.domain.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepository {

    private final TokenJpaRepository jpaRepository;

    @Override
    public Optional<Token> findById(Long id) {
        return jpaRepository.findById(id).map(TokenMapper::toDomain);
    }

    @Override
    public void save(Token token) {
        jpaRepository.save(TokenMapper.toEntity(token));
    }
}
