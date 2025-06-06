package kr.hhplus.be.server.common.messages;


import java.text.MessageFormat;

public enum MessageCode {

    //사용자
    USER_NOT_FOUND("사용자가 존재하지 않습니다. [id: {0}]"),
    USER_POINT_NOT_ENOUGH("사용자의 포인트가 충분하지 않습니다. [현재 포인트: {0}]"),
    
    //콘서트
    CONCERT_NOT_FOUND("콘서트가 존재하지 않습니다. [id: {0}]"),
    CONCERT_NOT_AVAILABLE_ERROR("콘서트가 이용가능한 콘서트가 아닙니다"),

    //공연장
    VENUE_NOT_FOUND("공연장이 존재하지 않습니다. [id: {0}]"),

    //좌석
    SEAT_NOT_FOUND("좌석 정보가 존재하지 않습니다. [id: {0}]"),

    //티켓
    TICKET_NOT_FOUND("티켓 정보가 존재하지 않습니다. [id: {0}]"),
    TICKET_ALREADY_OCCUPIED("티켓이 이미 예약되어 있습니다. [id: {0}]"),

    //콘서트스케줄
    CONCERT_SCHEDULE_NOT_FOUND("콘서트 스케줄이 존재하지 않습니다. [id: {0}]"),
    CONCERT_SCHEDULE_PLAY_TIME_NOT_PROPER_ERROR("공연장스케줄 시간이 올바르지 않습니다. [startTime: {0}, endTime: {1}]"),
    CONCERT_SCHEDULE_NOT_AVAILABLE("공연장스케줄 예약이 가능하지 않습니다."),

    //공통
    INPUT_DATE_RANGE_NOT_PROPER_ERROR("입력한 일정이 올바르지 않습니다.");

    private final String messageTemplate;

    MessageCode(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String format(Object... args) {
        return MessageFormat.format(messageTemplate, args);
    }
}