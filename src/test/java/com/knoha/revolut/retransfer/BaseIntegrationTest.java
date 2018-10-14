package com.knoha.revolut.retransfer;

import com.knoha.revolut.retransfer.db.DatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;

public class BaseIntegrationTest {

    private static DatabaseManager databaseManager;

    @BeforeAll
    public static void setUpClass() {
        databaseManager = DatabaseManager.getInstance();
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
        dropDb();
    }

    private static void dropDb() throws Exception {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            DriverManager.getConnection("jdbc:derby:memory:" + DatabaseManager.DB_NAME + ";drop=true");
        } catch (final SQLNonTransientConnectionException e) {
            // Suppress java.sql.SQLNonTransientConnectionException: Database 'memory:re_transfer_oltp' dropped.
        }
    }

    protected DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

}
