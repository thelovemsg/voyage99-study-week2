package kr.hhplus.be.server.ticket.domain.model;

import kr.hhplus.be.server.common.messages.MessageCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    private Long tokenId;
    private Long userId;
    private Long queueId;
    private Long concertScheduleId;
    private String encryptedValue; // 암호화된 토큰 값
    private LocalDateTime issuedAt;
    private LocalDateTime expireAt;

    // 도메인 팩토리 메서드는 명시적으로 작성 (비즈니스 의도 표현)
    public static Token issueNewToken(Long userId, Long queueId, Long concertScheduleId, String encryptedValue) {
        LocalDateTime now = LocalDateTime.now();
        return Token.builder()
                .userId(userId)
                .queueId(queueId)
                .concertScheduleId(concertScheduleId)
                .encryptedValue(encryptedValue)
                .issuedAt(now)
                .expireAt(now.plusMinutes(10)) // 10분 유효
                .build();
    }

    // 도메인 로직은 명시적으로 작성 (비즈니스 규칙 표현)
    private boolean isValid() {
        return LocalDateTime.now().isBefore(expireAt);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireAt);
    }

    public void extendExpiry(int minutes) {
        if (isExpired()) {
            throw new IllegalStateException(MessageCode.TOKEN_EXPIRED.getMessage());
        }
        this.expireAt = this.expireAt.plusMinutes(minutes);
    }


}
