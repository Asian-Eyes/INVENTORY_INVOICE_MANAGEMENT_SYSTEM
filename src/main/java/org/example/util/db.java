package org.example.util;

import java.sql.*;

public final class db {
    private static final String URL = "jdbc:mysql://localhost:3308/inventory_invoice_db";
    private static final String USER = "root";
    private static final String PASSWORD = "ZZ@\"4._V(q9z^j&j";

    private db () {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void ping() throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT 1");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            System.out.println("DB connection successful, ping result: " + resultSet.getInt(1));
        }
    }
}