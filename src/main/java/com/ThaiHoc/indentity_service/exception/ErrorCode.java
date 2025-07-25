package com.ThaiHoc.indentity_service.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED(1002,"user existed", HttpStatus.BAD_REQUEST),

    INVALID_KEY(1001,"INVALID_KEY exception", HttpStatus.BAD_REQUEST),

    UNHANDLED_EXCEPTION(999,"unhandled exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1003, "user name invalid: user name must be at least 3 character", HttpStatus.BAD_REQUEST),

    PASSWORD_INVALID(1004, "password invalid: password must be at least 8 character", HttpStatus.BAD_REQUEST),

    USERNAME_NOT_FOUND(1005,"user name not found", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1006, "unauthenticated", HttpStatus.UNAUTHORIZED),

    UNAUTHORIZED(1007, "you do not have permission", HttpStatus.FORBIDDEN)

    ;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;


}
