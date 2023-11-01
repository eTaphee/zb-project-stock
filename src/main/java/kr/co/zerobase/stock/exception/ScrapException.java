package kr.co.zerobase.stock.exception;

public class ScrapException extends RuntimeException {
    public ScrapException(String message) {
        super(message);
    }

    public ScrapException(Throwable cause) {
        super(cause);
    }
}
