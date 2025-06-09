package kr.hhplus.be.server.ticket.application.service.token;

import kr.hhplus.be.server.ticket.application.port.token.in.ValidateTokenUseCase;
import kr.hhplus.be.server.ticket.infrastructure.persistence.token.TokenRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateTokenUseCaseImpl implements ValidateTokenUseCase {

    private final TokenRepositoryAdapter tokenRepositoryAdapter;

    @Override
    public Boolean validateToken(String rawTokenId) {
        return null;
    }
}
