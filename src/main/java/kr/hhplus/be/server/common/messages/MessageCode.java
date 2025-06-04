package kr.hhplus.be.server.common.messages;


import java.text.MessageFormat;

public enum MessageCode {

    //콘서트
    CONCERT_NOT_FOUND("콘서트가 존재하지 않습니다. [id: {0}]"),
    CONCERT_NOT_AVAILABLE_ERROR("콘서트가 이용가능한 콘서트가 아닙니다"),

    //공연장
    VENUE_NOT_FOUND("공연장이 존재하지 않습니다. [id: {0}]"),

    //콘서트스케줄
    CONCERT_PLAY_TIME_NOT_PROPER_ERROR("공연장스케줄 시간이 올바르지 않습니다. [startTime: {0}, endTime: {1}]"),

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