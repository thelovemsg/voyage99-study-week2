package kr.hhplus.be.server.concert.enums;

/**
 * 콘서트 상태
 * - STOPPED : 정지
 * - CHECKING : 검사중
 * - READY : 준비(판매 예정)
 * - ON_SELLING : 판매중
 * - EXPIRE : 만료
 */
public enum ConcertStatus {
    STOPPED, CHECKING, READY, ON_SELLING, EXPIRE;
}
