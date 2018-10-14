package com.knoha.revolut.retransfer.services.transfers.spi;

import com.knoha.revolut.retransfer.models.transfers.TransferRequestDto;
import com.knoha.revolut.retransfer.services.transfers.exceptions.TransferProcessingException;

public interface TransferProcessingService {

    void transfer(TransferRequestDto transferRequest)
            throws TransferProcessingException;

}
