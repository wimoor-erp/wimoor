package com.wimoor.finance.closing.domain;
public class ReverseClosingException extends RuntimeException {

    public ReverseClosingException(String message) {
        super(message);
    }

    public ReverseClosingException(String message, Throwable cause) {
        super(message, cause);
    }
}