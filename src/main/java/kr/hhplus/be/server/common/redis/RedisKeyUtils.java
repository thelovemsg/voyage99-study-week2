package kr.hhplus.be.server.common.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeyUtils {

    // 키 접두사 상수 정의
    private static final String LOCK_PREFIX = "lock:";
    private static final String TICKET_PREFIX = "ticket:";
    private static final String REVERSE_PREFIX = "reverse:";

    // 티켓 잠금 키 생성
    public static String getTicketReverseLockKey(Long ticketId) {
        return LOCK_PREFIX + TICKET_PREFIX + REVERSE_PREFIX + ticketId;
    }

    // 다른 티켓 관련 키도 추가 가능
    public static String getTicketInfoKey(Long ticketId) {
        return TICKET_PREFIX + "info:" + ticketId;
    }

    // 다른 도메인의 키도 추가 가능
    public static String getUserLockKey(Long userId) {
        return LOCK_PREFIX + "user:" + userId;
    }
}