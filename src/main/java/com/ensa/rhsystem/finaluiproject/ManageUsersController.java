package com.ensa.rhsystem.finaluiproject;

import com.ensa.rhsystem.finaluiproject.modules.Compte;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.ensa.rhsystem.finaluiproject.dao.CompteDAO.getComptes;

public class ManageUsersController {

    @FXML private TableColumn<Compte, Integer> IdUserColumn;

    @FXML private TableColumn<Compte, Integer> IdAccountColumn;

    @FXML private TableColumn<Compte, String> LoginColumn;

    @FXML private TableColumn<Compte, String> PasswordColumn;

    @FXML private TableView<Compte> comptesTable;

    @FXML private TextField loginTextField;

    @FXML private TextField passwordTextField;

    @FXML private TextField userIdField;

    public void initialize() {
        displyaAccountsData();
        comptesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFieldsWithSelectedAccount(newSelection);
            }
        });

    }



    // Create new account for a certain user with a specific userID
    public void createCompte() {
        String createAccountQuery = "insert into compte(login, password, id_user) values(?,?,?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(createAccountQuery)) {

            pstmt.setString(1, loginTextField.getText());
            pstmt.setString(2, passwordTextField.getText());
            pstmt.setInt(3, Integer.parseInt(userIdField.getText()));

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Account created successfully for user with ID = "+userIdField.getText()+"!");
                alert.showAndWait();
                System.out.println("Account created successfully.");
            }

            clearCreateAccountsForm();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // To clear text fields after inserting
    private void clearCreateAccountsForm() {
        loginTextField.clear();
        passwordTextField.clear();
        userIdField.clear();
    }

    // Display created accounts into the table view
    public void displyaAccountsData(){
        ObservableList<Compte> comptesList = getComptes();
        // Link table columns to the Compte fields
        LoginColumn.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getLogin());
        });
        PasswordColumn.setCellValueFactory(cellData -> {return  new SimpleStringProperty(cellData.getValue().getPassword());});

        IdAccountColumn.setCellValueFactory(cellData -> {return new SimpleIntegerProperty(cellData.getValue().getIdCompte()).asObject();
        });

        IdUserColumn.setCellValueFactory(cellData -> { return new SimpleIntegerProperty(cellData.getValue().getIdUser()).asObject();});

        comptesTable.setItems(comptesList);
    }

    // Display the elements of the selected row
    public void populateFieldsWithSelectedAccount(Compte selected) {
        userIdField.setText(String.valueOf(selected.getIdUser()));
        loginTextField.setText(selected.getLogin());
        passwordTextField.setText(selected.getPassword());
    }

    // update a user account
    public void updateAccount() {
        Compte seletedAccount = comptesTable.getSelectionModel().getSelectedItem();
        String updateAccountSql = "update compte set login = ?, password = ? where id_user = ?";

        try(Connection conn = DbConnection.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(updateAccountSql);
            pstmt.setString(1, loginTextField.getText());
            pstmt.setString(2, passwordTextField.getText());
            pstmt.setInt(3, Integer.parseInt(userIdField.getText()));
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Account updated successfully for user with ID = "+userIdField.getText()+"!");
                alert.showAndWait();
            }
            clearCreateAccountsForm();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


}