package kr.hhplus.be.server.ticket.application.service.token;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.exceptions.ParameterNotValidException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.common.utils.CryptoUtils;
import kr.hhplus.be.server.ticket.application.port.token.in.ValidateTokenUseCase;
import kr.hhplus.be.server.ticket.domain.model.Token;
import kr.hhplus.be.server.ticket.infrastructure.persistence.token.TokenRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ValidateTokenServiceImpl implements ValidateTokenUseCase {

    private final TokenRepositoryAdapter tokenRepositoryAdapter;
    private final CryptoUtils cryptoUtils;

    @Override
    @Transactional
    public Boolean validateToken(String rawTokenId) {
        String encryptValue = cryptoUtils.encrypt(rawTokenId);

        Token token = tokenRepositoryAdapter.findByEncryptedValue(encryptValue)
                .orElseThrow(() -> new ParameterNotValidException(MessageCode.TOKEN_NOT_FOUND.getMessage()));

        LocalDateTime expireAt = token.getExpireAt();

        return expireAt.isBefore(LocalDateTime.now());
    }
}
