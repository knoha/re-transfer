package com.knoha.revolut.retransfer.services.transfers.base;

import com.knoha.revolut.retransfer.BaseIntegrationTest;
import com.knoha.revolut.retransfer.models.accounts.BankAccount;
import com.knoha.revolut.retransfer.models.enums.CurrencyCode;
import com.knoha.revolut.retransfer.models.transfers.TransferRequestDto;
import com.knoha.revolut.retransfer.services.accounts.base.BankAccountServiceImpl;
import com.knoha.revolut.retransfer.services.accounts.spi.BankAccountService;
import com.knoha.revolut.retransfer.services.transfers.exceptions.TransferProcessingException;
import com.knoha.revolut.retransfer.services.transfers.spi.TransferProcessingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransferProcessingServiceImplTest extends BaseIntegrationTest {

    private TransferProcessingService service = new TransferProcessingServiceImpl();

    private BankAccountService bankAccountService = new BankAccountServiceImpl();

    @BeforeEach
    void setUp() {
        bankAccountService.create(buildBankAccount(100, CurrencyCode.PLN, BigDecimal.TEN));
        bankAccountService.create(buildBankAccount(101, CurrencyCode.PLN, BigDecimal.TEN));
    }

    @AfterEach
    void tearDown() {
        bankAccountService.delete(100);
        bankAccountService.delete(101);
    }

    @Test
    void transfer() throws Exception {
        final TransferRequestDto request = new TransferRequestDto();
        request.setRecipientAccountId(100L);
        request.setSenderAccountId(101L);
        request.setAmount(new BigDecimal(3.56));

        service.transfer(request);

        assertEquals(new BigDecimal(6.44).setScale(2, RoundingMode.HALF_UP), bankAccountService.retrieve(101).getBalance());
        assertEquals(new BigDecimal(13.56).setScale(2, RoundingMode.HALF_UP), bankAccountService.retrieve(100).getBalance());
    }

    @Test
    void transfer_AmountNull() {
        final TransferRequestDto request = new TransferRequestDto();

        final TransferProcessingException e = assertThrows(TransferProcessingException.class, () -> service.transfer(request));
        assertEquals("Amount is required.", e.getMessage());
    }

    @Test
    void transfer_AmountIsNegative() {
        final TransferRequestDto request = new TransferRequestDto();
        request.setAmount(new BigDecimal(-1.43));

        final TransferProcessingException e = assertThrows(TransferProcessingException.class, () -> service.transfer(request));
        assertEquals("Amount is required.", e.getMessage());
    }

    @Test
    void transfer_SenderAccountNull() {
        final TransferRequestDto request = new TransferRequestDto();
        request.setSenderAccountId(1000L);
        request.setRecipientAccountId(100L);
        request.setAmount(BigDecimal.TEN);

        final TransferProcessingException e = assertThrows(TransferProcessingException.class, () -> service.transfer(request));
        assertEquals("Failed to process a transfer.", e.getMessage());
    }

    @Test
    void transfer_RecipientAccountNull() {
        final TransferRequestDto request = new TransferRequestDto();
        request.setSenderAccountId(100L);
        request.setRecipientAccountId(1000L);
        request.setAmount(BigDecimal.TEN);

        final TransferProcessingException e = assertThrows(TransferProcessingException.class, () -> service.transfer(request));
        assertEquals("Failed to process a transfer.", e.getMessage());
    }

    @Test
    void transfer_DifferentCurrencyCodes() {
        bankAccountService.create(buildBankAccount(102, CurrencyCode.GBP, BigDecimal.TEN));

        final TransferRequestDto request = new TransferRequestDto();
        request.setRecipientAccountId(100L);
        request.setSenderAccountId(102L);
        request.setAmount(new BigDecimal(3.56));

        final TransferProcessingException e = assertThrows(TransferProcessingException.class, () -> service.transfer(request));
        assertEquals("Accounts are in different currencies.", e.getMessage());
    }

    @Test
    void transfer_InsufficientFunds() {
        final TransferRequestDto request = new TransferRequestDto();
        request.setRecipientAccountId(100L);
        request.setSenderAccountId(101L);
        request.setAmount(new BigDecimal(19));

        final TransferProcessingException e = assertThrows(TransferProcessingException.class, () -> service.transfer(request));
        assertEquals("Insufficient funds on a sender account.", e.getMessage());
    }

    private BankAccount buildBankAccount(final long id, final CurrencyCode currencyCode, final BigDecimal amount) {
        final BankAccount bankAccount = new BankAccount();
        bankAccount.setId(id);
        bankAccount.setNumber(UUID.randomUUID().toString());
        bankAccount.setCurrencyCode(currencyCode);
        bankAccount.setBalance(amount);
        bankAccount.setOwnerId(1);
        return bankAccount;
    }
}