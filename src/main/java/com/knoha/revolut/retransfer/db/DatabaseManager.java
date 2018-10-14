package com.knoha.revolut.retransfer.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    public static final String DB_NAME = "re_transfer_oltp";

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);

    private static DatabaseManager instance = new DatabaseManager();
    private static boolean initialized = false;

    private DatabaseManager() {
        if (!initialized) {
            try {
                initDb();
                initialized = true;
            } catch (final SQLException e) {
                throw new RuntimeException("Failed to initialize data source.", e);
            }
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }

        return instance;
    }

    public Connection getConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("Failed to load JDBC driver.", e);
        }

        try {
            return DriverManager.getConnection("jdbc:derby:memory:" + DB_NAME + ";create=true");
        } catch (final SQLException e) {
            throw new RuntimeException("Failed to create a connection.", e);
        }
    }

    public void rollback(final Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (final SQLException e) {
                LOGGER.warn("Failed to rollback a transaction.", e);
            }
        }
    }

    public void closeQuietly(final AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final Exception e) {
                LOGGER.warn("Failed to close a resource.", e);
            }
        }
    }

    private void initDb() throws SQLException {
        final Connection connection = getConnection();

        LOGGER.info("Initializing database schema ...");

        // >>> DDL
        connection.prepareStatement("CREATE TABLE users(" +
                "  id BIGINT PRIMARY KEY," +
                "  first_name VARCHAR(50)," +
                "  last_name VARCHAR(50)," +
                "  username VARCHAR(30) NOT NULL," +
                "  email VARCHAR(100)," +
                "  CONSTRAINT users_uni01 UNIQUE (username)" +
                ")").execute();

        connection.prepareStatement("CREATE TABLE bank_accounts(" +
                "  id BIGINT PRIMARY KEY," +
                "  number VARCHAR(36) NOT NULL," +
                "  currency_code VARCHAR(3) NOT NULL," +
                "  balance DECIMAL(20, 2)," +
                "  owner BIGINT," +
                "  CONSTRAINT bank_accounts_fk01 FOREIGN KEY (owner) REFERENCES users(id)," +
                "  CONSTRAINT bank_accounts_uni01 UNIQUE (number)" +
                ")").execute();

        // >>> DML
        connection.prepareStatement("INSERT INTO users VALUES (1, 'John', 'Doe', 'jdoe', 'jdoe@gmail.com')").execute();
        connection.prepareStatement("INSERT INTO users VALUES (2, 'Sam', 'Fisher', 'sfisher', 'sfisher@gmail.com')").execute();
        connection.prepareStatement("INSERT INTO bank_accounts VALUES (1, 'a6b6f9b9-6e62-4fe6-a689-37e3ab0d812c', 'USD', 10000.59, 1)").execute();
        connection.prepareStatement("INSERT INTO bank_accounts VALUES (2, 'bfe87d38-0c94-4389-8416-602151e58481', 'USD', 245.21, 2)").execute();

        LOGGER.info("Initialization finished.");
    }

}
