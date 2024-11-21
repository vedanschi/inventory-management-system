package com.inventory.config;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/iop";
    private static final String USER = "root"; 
    private static final String PASSWORD = "vedanshi06"; 

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        // Load MySQL JDBC Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Establish Connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void cleanup() {
    try {
        java.sql.Driver driver = DriverManager.getDriver("jdbc:mysql://");
        DriverManager.deregisterDriver(driver);
        AbandonedConnectionCleanupThread.checkedShutdown();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connected to the database successfully.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Connection test failed.");
            e.printStackTrace();
        }
    }
}
