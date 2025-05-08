package com.ensa.rhsystem.finaluiproject.utils;

import java.sql.*;

public class DbConnection {


    private static final String URL =
            "jdbc:mysql://localhost:3306/rh_system";
    private static final String USER = "root";
    private static final String PASSWORD = "imad0003";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /*public static Connection getConnection() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rh_system", "root", "imad0003");
            statement = connection.createStatement();

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();  // Detailed error
        } catch (SQLException e) {
            System.out.println("Database connection error.");
            e.printStackTrace();  // Detailed error
        } finally {
            // Ensure the resources are closed properly
            try {
                if (statement != null) {
                    System.out.println("success");
                    statement.close();
                }
                if (connection != null) {
                    System.out.println("success");
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Print if closing resources fails
            }
        }
        return connection;
    }*/
}