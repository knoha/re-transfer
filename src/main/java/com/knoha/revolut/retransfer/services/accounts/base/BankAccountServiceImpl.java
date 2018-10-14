package com.knoha.revolut.retransfer.services.accounts.base;

import com.knoha.revolut.retransfer.models.accounts.BankAccount;
import com.knoha.revolut.retransfer.services.accounts.spi.BankAccountService;

public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountRepository repository = new BankAccountRepository();

    @Override
    public BankAccount retrieve(final long id) {
        return repository.retrieve(id);
    }

    @Override
    public BankAccount create(final BankAccount bankAccount) {
        return repository.create(bankAccount);
    }

    @Override
    public void delete(final long id) {
        repository.delete(id);
    }

}
