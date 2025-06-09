package kr.hhplus.be.server.ticket.infrastructure.persistence.token;

import kr.hhplus.be.server.ticket.domain.model.Token;

public class TokenMapper {

    public static TokenEntity toEntity(Token token) {
        if (token == null) {
            return null;
        }

        return TokenEntity.builder()
                .tokenId(token.getTokenId())
                .userId(token.getUserId())
                .queueId(token.getQueueId())
                .concertScheduleId(token.getConcertScheduleId())
                .encryptedValue(token.getEncryptedValue())
                .issuedAt(token.getIssuedAt())
                .expireAt(token.getExpireAt())
                .build();
    }

    /**
     * TokenEntity를 Token 도메인 모델로 변환
     */
    public static Token toDomain(TokenEntity tokenEntity) {
        if (tokenEntity == null) {
            return null;
        }

        return new Token(
                tokenEntity.getTokenId(),
                tokenEntity.getUserId(),
                tokenEntity.getQueueId(),
                tokenEntity.getConcertScheduleId(),
                tokenEntity.getEncryptedValue(),
                tokenEntity.getIssuedAt(),
                tokenEntity.getExpireAt()
        );
    }

}
