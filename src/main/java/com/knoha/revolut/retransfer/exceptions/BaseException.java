package com.knoha.revolut.retransfer.exceptions;

public class BaseException extends Exception {

    public BaseException(final Throwable throwable) {
        super(throwable);
    }

    public BaseException(final String message) {
        super(message);
    }

}
