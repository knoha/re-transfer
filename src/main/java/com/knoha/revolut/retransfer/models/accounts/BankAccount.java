package com.knoha.revolut.retransfer.models.accounts;

import com.knoha.revolut.retransfer.models.enums.CurrencyCode;

import java.math.BigDecimal;

public class BankAccount {

    private Long id;

    private String number;
    private CurrencyCode currencyCode;

    private BigDecimal balance;

    private long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BankAccount)) {
            return false;
        }

        final BankAccount that = (BankAccount) o;

        return getNumber().equals(that.getNumber());
    }

    @Override
    public int hashCode() {
        return getNumber().hashCode();
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", currencyCode=" + currencyCode +
                ", ownerId=" + ownerId +
                '}';
    }
}
