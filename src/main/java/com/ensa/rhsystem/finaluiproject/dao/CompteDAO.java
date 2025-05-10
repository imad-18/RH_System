package com.ensa.rhsystem.finaluiproject.dao;

import com.ensa.rhsystem.finaluiproject.modules.Compte;
import com.ensa.rhsystem.finaluiproject.modules.User;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CompteDAO {

    public static ObservableList<Compte> getComptes() {
        ObservableList<Compte> accountsList = FXCollections.observableArrayList();

        String sql = "SELECT c.*, u.first_name, u.last_name "+
                "FROM compte c "+
                "JOIN users u ON u.id_user = c.id_user";

        try(Connection conn = DbConnection.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Compte compte = new Compte();

                compte.setIdCompte(rs.getInt("id_compte"));
                compte.setLogin(rs.getString("login"));
                compte.setPassword(rs.getString("password"));
                compte.setIdUser(rs.getInt("id_user"));
                compte.setNom(rs.getString("first_name"));
                compte.setPrenom(rs.getString("last_name"));

                accountsList.add(compte);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accountsList;
    }

    public static boolean deleteAccountById(int comteId) throws SQLException {
        String deleteAccountQuery = "DELETE FROM compte WHERE id_compte = ?";

        try(Connection conn = DbConnection.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(deleteAccountQuery);
            pstmt.setInt(1, comteId);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public static int getUserIdByFullName(String fullName) throws Exception {
        // Split the full name
        String[] parts = fullName.trim().split(" ");
        if (parts.length < 2) {
            throw new Exception("Invalid full name format: " + fullName);
        }
        String firstName = parts[0];
        String lastName = parts[1]; // You can enhance this if you expect multiple-word last names

        String query = "SELECT id_user FROM users WHERE first_name = ? AND last_name = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_user");
            } else {
                throw new Exception("User not found: " + fullName);
            }
        }
    }

}
