package com.knoha.revolut.retransfer.services.transfers.base;

import com.knoha.revolut.retransfer.models.accounts.BankAccount;
import com.knoha.revolut.retransfer.models.transfers.TransferRequestDto;
import com.knoha.revolut.retransfer.services.accounts.base.BankAccountServiceImpl;
import com.knoha.revolut.retransfer.services.accounts.spi.BankAccountService;
import com.knoha.revolut.retransfer.services.transfers.exceptions.TransferProcessingException;
import com.knoha.revolut.retransfer.services.transfers.spi.TransferProcessingService;

import java.math.RoundingMode;

public class TransferProcessingServiceImpl implements TransferProcessingService {

    private BankAccountService bankAccountService = new BankAccountServiceImpl();

    private TransferProcessingRepository transferProcessingRepository = new TransferProcessingRepository();

    @Override
    public void transfer(final TransferRequestDto transferRequest) throws TransferProcessingException {
        if (transferRequest.getAmount() == null || transferRequest.getAmount().doubleValue() < 0) {
            throw new TransferProcessingException("Amount is required.");
        }

        final BankAccount senderAccount = bankAccountService.retrieve(transferRequest.getSenderAccountId());
        final BankAccount recipientAccount = bankAccountService.retrieve(transferRequest.getRecipientAccountId());

        if (senderAccount == null || recipientAccount == null) {
            throw new TransferProcessingException("Failed to process a transfer.");
        }

        if (!senderAccount.getCurrencyCode().equals(recipientAccount.getCurrencyCode())) {
            throw new TransferProcessingException("Accounts are in different currencies.");
        }

        if (senderAccount.getBalance().doubleValue() < transferRequest.getAmount().doubleValue()) {
            throw new TransferProcessingException("Insufficient funds on a sender account.");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(transferRequest.getAmount()).setScale(2, RoundingMode.HALF_UP));
        recipientAccount.setBalance(recipientAccount.getBalance().add(transferRequest.getAmount()).setScale(2, RoundingMode.HALF_UP));

        transferProcessingRepository.transfer(senderAccount, recipientAccount);
    }

}
