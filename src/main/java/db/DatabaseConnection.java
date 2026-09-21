package db;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    /**
     * Creates and returns a NEW database connection.
     *
     * The caller is responsible for closing the connection.
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    DatabaseConfig.URL,
                    DatabaseConfig.USER,
                    DatabaseConfig.PASSWORD
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database connection failed: " + e.getMessage(),
                    e
            );
        }
    }
}