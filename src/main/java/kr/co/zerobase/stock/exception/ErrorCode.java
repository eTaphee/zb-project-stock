package kr.co.zerobase.stock.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    COMPANY_NOT_FOUND(BAD_REQUEST.value(), "존재하지 않는 회사명입니다."),
    COMPANY_ALREADY_EXISTS(BAD_REQUEST.value(), "이미 존재하는 회사입니다."),
    USERNAME_NOT_FOUND(BAD_REQUEST.value(), "존재하지 않는 사용자입니다."),
    USERNAME_ALREADY_EXISTS(BAD_REQUEST.value(), "이미 존재하는 사용자입니다."),
    PASSWORD_UN_MATCHED(BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다.");


    private final int status;
    private final String description;
}
