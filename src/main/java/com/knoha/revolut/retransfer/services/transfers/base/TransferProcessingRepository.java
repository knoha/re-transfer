package com.knoha.revolut.retransfer.services.transfers.base;

import com.knoha.revolut.retransfer.db.DatabaseManager;
import com.knoha.revolut.retransfer.models.accounts.BankAccount;
import com.knoha.revolut.retransfer.services.transfers.exceptions.TransferProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransferProcessingRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferProcessingRepository.class);

    private DatabaseManager manager = DatabaseManager.getInstance();

    public void transfer(final BankAccount senderAccount, final BankAccount recipientAccount)
            throws TransferProcessingException {

        final Connection connection = manager.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection
                    .prepareStatement("UPDATE bank_accounts SET balance = ? WHERE id = ?");

            statement.setBigDecimal(1, senderAccount.getBalance());
            statement.setLong(2, senderAccount.getId());
            statement.executeUpdate();

            statement.setBigDecimal(1, recipientAccount.getBalance());
            statement.setLong(2, recipientAccount.getId());
            statement.executeUpdate();

            connection.commit();
        } catch (final SQLException e) {
            LOGGER.error("Failed to transfer(" + senderAccount + ", " + recipientAccount + ")", e);
            manager.rollback(connection);

            throw new TransferProcessingException("Failed to process a transfer.");
        } finally {
            manager.closeQuietly(statement);
            manager.closeQuietly(connection);
        }
    }
}
