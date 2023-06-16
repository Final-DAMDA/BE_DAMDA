package com.damda.back.data.common;

public enum CodeEnum {

    SUCCESS(200,"success"),
    BAD_REQUEST(400,"잘못된 요청"),
    UNKNOWN_ERROR(500, "An unknown error has occurred."),

    NO_PERMISSIONS(403,"권한없음");


    private final int code;
    private final String message;

    private CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
