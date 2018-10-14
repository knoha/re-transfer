package com.knoha.revolut.retransfer.services.transfers.exceptions;

import com.knoha.revolut.retransfer.exceptions.BaseException;

public class TransferProcessingException extends BaseException {

    public TransferProcessingException(final String message) {
        super(message);
    }

}
