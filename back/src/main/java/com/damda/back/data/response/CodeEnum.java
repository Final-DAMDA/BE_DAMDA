package com.damda.back.data.response;

public enum CodeEnum {

    SUCCESS(200,"success"),
    UNKNOWN_ERROR(500, "An unknown error has occurred.");


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
