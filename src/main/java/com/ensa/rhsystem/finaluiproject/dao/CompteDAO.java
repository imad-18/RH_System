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

        String sql = "SELECT * FROM compte";

        try(Connection conn = DbConnection.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Compte compte = new Compte();

                compte.setIdCompte(rs.getInt("id_compte"));
                compte.setLogin(rs.getString("login"));
                compte.setPassword(rs.getString("password"));
                compte.setIdUser(rs.getInt("id_user"));

                accountsList.add(compte);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accountsList;
    }
}
