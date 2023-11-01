package kr.co.zerobase.stock.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
//        MethodArgumentNotValidException e) {
//        log.error("MethodArgumentNotValidException is occurred.", e);
//        return getErrorResponseResponseEntity(INVALID_REQUEST);
//    }
//
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameterException(
//        MissingServletRequestParameterException e) {
//        log.error("MissingServletRequestParameterException is occurred.", e);
//        return getErrorResponseResponseEntity(INVALID_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
//        log.error("Exception is occurred.", e);
//        return getErrorResponseResponseEntity(INTERNAL_SERVER_ERROR);
//    }

    private static ResponseEntity<ErrorResponseDto> getErrorResponseResponseEntity(
        ErrorCode errorCode) {
        return new ResponseEntity<>(ErrorResponseDto.from(errorCode),
            HttpStatus.valueOf(errorCode.getStatus()));
    }
}
