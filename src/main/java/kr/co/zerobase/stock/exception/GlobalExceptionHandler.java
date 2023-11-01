package kr.co.zerobase.stock.exception;

import static kr.co.zerobase.stock.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static kr.co.zerobase.stock.exception.ErrorCode.INVALID_REQUEST;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockException.class)
    public ResponseEntity<ErrorResponseDto> handleAccountException(StockException e) {
        log.error("{} is occurred.", e.getErrorCode(), e);
        return getErrorResponseResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(HttpServletRequest req,
        MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException is occurred.", e);
        return getErrorResponseResponseEntity(INVALID_REQUEST,
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error("Exception is occurred.", e);
        return getErrorResponseResponseEntity(INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<ErrorResponseDto> getErrorResponseResponseEntity(
        ErrorCode errorCode) {
        return new ResponseEntity<>(ErrorResponseDto.from(errorCode),
            HttpStatus.valueOf(errorCode.getStatus()));
    }

    private static ResponseEntity<ErrorResponseDto> getErrorResponseResponseEntity(
        ErrorCode errorCode, String message) {
        return new ResponseEntity<>(ErrorResponseDto.builder()
            .status(errorCode.getStatus())
            .errorCode(errorCode)
            .description(message)
            .build(),
            HttpStatus.valueOf(errorCode.getStatus()));
    }
}
