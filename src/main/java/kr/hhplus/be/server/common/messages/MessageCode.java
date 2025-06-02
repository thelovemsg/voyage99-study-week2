package kr.hhplus.be.server.common.messages;


import java.text.MessageFormat;

public enum MessageCode {

    CONCERT_NOT_FOUND("콘서트가 존재하지 않습니다. [id: {0}]"),
    CONCERT_NOT_AVAILABLE_ERROR("콘서트가 이용가능한 콘서트가 아닙니다");

    private final String messageTemplate;

    MessageCode(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String format(Object... args) {
        return MessageFormat.format(messageTemplate, args);
    }
}