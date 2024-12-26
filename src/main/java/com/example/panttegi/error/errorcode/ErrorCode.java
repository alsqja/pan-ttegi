package com.example.panttegi.error.errorcode;

import com.example.panttegi.error.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "지원하지 않는 양식의 파일입니다."),
    LARGE_FILE(HttpStatus.BAD_REQUEST, "파일의 용량은 최대 5MB 입니다."),
    BAD_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력값 입니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호 입니다."),
    UNAUTHORIZED_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    UNCHECKED_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호 확인이 필요합니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "본인이 아닙니다."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 데이터 입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ErrorCode fromName(String name) throws CustomException {
        for (ErrorCode code : ErrorCode.values()) {
            if (code.name().equalsIgnoreCase(name)) {
                return code;
            }
        }
        throw new CustomException(ErrorCode.BAD_INPUT);
    }
}
