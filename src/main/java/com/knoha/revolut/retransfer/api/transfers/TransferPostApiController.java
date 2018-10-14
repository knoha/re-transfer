package com.knoha.revolut.retransfer.api.transfers;

import com.knoha.revolut.retransfer.api.BaseApiController;
import com.knoha.revolut.retransfer.exceptions.BaseException;
import com.knoha.revolut.retransfer.models.transfers.TransferRequestDto;
import com.knoha.revolut.retransfer.services.transfers.base.TransferProcessingServiceImpl;
import com.knoha.revolut.retransfer.services.transfers.spi.TransferProcessingService;
import spark.Request;
import spark.Response;

public class TransferPostApiController extends BaseApiController {

    private TransferProcessingService processingService = new TransferProcessingServiceImpl();

    public TransferPostApiController(final Request request, final Response response) {
        super(request, response);
    }

    @Override
    public Object handle() {
        try {
            final TransferRequestDto transferRequest = readPayload(TransferRequestDto.class);

            processingService.transfer(transferRequest);

            return ok("Transfer was processed successfully.");
        } catch (final BaseException e) {
            return serverError(e.getMessage());
        }
    }
}
