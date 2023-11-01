package kr.co.zerobase.stock.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseDto {

    private final int status;

    private final ErrorCode errorCode;

    private final String description;

    public static ErrorResponseDto from(ErrorCode errorCode) {
        return ErrorResponseDto.builder()
            .status(errorCode.getStatus())
            .errorCode(errorCode)
            .description(errorCode.getDescription())
            .build();
    }
}

