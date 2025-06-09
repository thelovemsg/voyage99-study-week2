package kr.hhplus.be.server.ticket.application.service.token;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.utils.CryptoUtils;
import kr.hhplus.be.server.ticket.application.port.token.in.IssueTokenUseCase;
import kr.hhplus.be.server.ticket.application.port.token.in.dto.IssueTokenCommandDto;
import kr.hhplus.be.server.ticket.domain.model.Token;
import kr.hhplus.be.server.ticket.infrastructure.persistence.token.TokenMapper;
import kr.hhplus.be.server.ticket.infrastructure.persistence.token.TokenRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueTokenServiceImpl implements IssueTokenUseCase {

    private final TokenRepositoryAdapter tokenRepositoryAdapter;
    private final CryptoUtils cryptoUtils;

    @Override
    @Transactional
    public IssueTokenCommandDto.Response issueToken(IssueTokenCommandDto.Request request) {
        String rawTokenId = cryptoUtils.generateSecureToken();

        String encryptedValue = cryptoUtils.encrypt(rawTokenId);

        Token token = Token.issueNewToken(request.getUserId(),
                                            request.getQueueId(),
                                            request.getConcertScheduleId(),
                                            encryptedValue);

        tokenRepositoryAdapter.save(token);

        return new IssueTokenCommandDto.Response(rawTokenId);
    }
}
