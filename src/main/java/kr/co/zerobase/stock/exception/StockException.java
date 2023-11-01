package kr.co.zerobase.stock.exception;

import lombok.Getter;

@Getter
public class StockException extends RuntimeException {

    private final int status;
    private final ErrorCode errorCode;

    public StockException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.errorCode = errorCode;
    }
}
