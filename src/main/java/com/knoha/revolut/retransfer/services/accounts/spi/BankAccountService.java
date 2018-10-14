package com.knoha.revolut.retransfer.services.accounts.spi;

import com.knoha.revolut.retransfer.models.accounts.BankAccount;

public interface BankAccountService {

    BankAccount retrieve(long id);

    BankAccount create(BankAccount bankAccount);

    void delete(long id);

}
