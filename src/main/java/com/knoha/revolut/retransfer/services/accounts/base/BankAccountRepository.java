package com.knoha.revolut.retransfer.services.accounts.base;

import com.knoha.revolut.retransfer.db.DatabaseManager;
import com.knoha.revolut.retransfer.models.accounts.BankAccount;
import com.knoha.revolut.retransfer.models.enums.CurrencyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountRepository.class);

    private DatabaseManager manager = DatabaseManager.getInstance();

    public BankAccount retrieve(final long id) {
        final Connection connection = manager.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        BankAccount result = null;

        try {
            statement = connection
                    .prepareStatement("SELECT * FROM bank_accounts WHERE id = ?");

            statement.setLong(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                final BankAccount ba = new BankAccount();

                ba.setId(resultSet.getLong("id"));
                ba.setNumber(resultSet.getString("number"));
                ba.setCurrencyCode(CurrencyCode.valueOf(resultSet.getString("currency_code")));
                ba.setBalance(resultSet.getBigDecimal("balance"));
                ba.setOwnerId(resultSet.getLong("owner"));

                result = ba;
            }
        } catch (final SQLException e) {
            LOGGER.error("Failed to retrieve(" + id + ")", e);
        } finally {
            manager.closeQuietly(resultSet);
            manager.closeQuietly(statement);
            manager.closeQuietly(connection);
        }

        return result;
    }

    public BankAccount create(final BankAccount bankAccount) {
        final Connection connection = manager.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection
                    .prepareStatement("INSERT INTO bank_accounts VALUES (?, ?, ?, ?, ?)");

            statement.setLong(1, bankAccount.getId());
            statement.setString(2, bankAccount.getNumber());
            statement.setString(3, bankAccount.getCurrencyCode().name());
            statement.setBigDecimal(4, bankAccount.getBalance());
            statement.setLong(5, bankAccount.getOwnerId());
            statement.executeUpdate();

            connection.commit();
        } catch (final SQLException e) {
            LOGGER.error("Failed to create(" + bankAccount + ")", e);
            manager.rollback(connection);
        } finally {
            manager.closeQuietly(statement);
            manager.closeQuietly(connection);
        }

        return bankAccount;
    }

    public void delete(final long id) {
        final Connection connection = manager.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection
                    .prepareStatement("DELETE FROM bank_accounts WHERE id = ?");

            statement.setLong(1, id);
            statement.executeUpdate();

            connection.commit();
        } catch (final SQLException e) {
            LOGGER.error("Failed to delete(" + id + ")", e);
            manager.rollback(connection);
        } finally {
            manager.closeQuietly(statement);
            manager.closeQuietly(connection);
        }
    }

}
