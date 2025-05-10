package com.ensa.rhsystem.finaluiproject;

import com.ensa.rhsystem.finaluiproject.dao.CompteDAO;
import com.ensa.rhsystem.finaluiproject.modules.Compte;
import com.ensa.rhsystem.finaluiproject.modules.User;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.Optional;

import static com.ensa.rhsystem.finaluiproject.dao.CompteDAO.*;
import static com.ensa.rhsystem.finaluiproject.dao.UserDAO.showErrorDialog;

public class ManageUsersController {

    @FXML private ComboBox<String> comboBoxField;
    @FXML private TableColumn<Compte, String> FirstNameColumn;

    @FXML private TableColumn<Compte, Integer> IdUserColumn;

    @FXML private TableColumn<Compte, Integer> IdAccountColumn;

    @FXML private TableColumn<Compte, String> LoginColumn;

    @FXML private TableColumn<Compte, String> PasswordColumn;

    @FXML private TableView<Compte> comptesTable;

    @FXML private TextField loginTextField;

    @FXML private TextField passwordTextField;

    @FXML private TextField userIdField;

    public void initialize() throws SQLException {

        displyaAccountsData();
        loadDepartmentNames();

        // Load account' info into text fields when a certain row is clicked
        comptesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFieldsWithSelectedAccount(newSelection);
            }
        });

    }


    private void loadDepartmentNames() throws SQLException {
        String myQuery = "SELECT u.first_name, u.last_name " +
                "FROM users u " +
                "JOIN salary s on u.id_user = s.id_user";

        Connection conn = DbConnection.getConnection();

        try (conn){
            // Ensure the connection is not null
            System.out.println("Connection established.");

            // Create PreparedStatement
            Statement stmt = conn.createStatement();
            System.out.println("Connection established2.");

            // Execute query and get result set
            ResultSet rs = stmt.executeQuery(myQuery);
            System.out.println("Connection established3.");
            while (rs.next()) {
                String firstnames = rs.getString("first_name") + " " + rs.getString("last_name");
                comboBoxField.getItems().add(firstnames);
            }

        } catch (Exception e) {
            showErrorDialog("Database Error", "An error occurred while loading full names.", e);
        }
    }



    // Create new account for a certain user with a specific userID!!
    public void createCompte() {
        String createAccountQuery = "insert into compte(login, password, id_user) values(?,?,?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(createAccountQuery)) {

            pstmt.setString(1, loginTextField.getText());
            pstmt.setString(2, passwordTextField.getText());
            //pstmt.setInt(3, Integer.parseInt(userIdField.getText()));
            System.out.println(comboBoxField.getValue());
            pstmt.setInt(3, getUserIdByFullName(comboBoxField.getValue()));

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Account created successfully for user with ID = "+getUserIdByFullName(comboBoxField.getValue())+"!");
                alert.showAndWait();
                System.out.println("Account created successfully.");
            }

            clearCreateAccountsForm();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // To clear text fields after inserting
    private void clearCreateAccountsForm() {
        loginTextField.clear();
        passwordTextField.clear();
        comboBoxField.getSelectionModel().clearSelection();
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

        FirstNameColumn.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getNom()+" "+cellData.getValue().getPrenom());});
        comptesTable.setItems(comptesList);
    }

    // Display the elements of the selected row
    public void populateFieldsWithSelectedAccount(Compte selected) {
        loginTextField.setText(selected.getLogin());
        passwordTextField.setText(selected.getPassword());
        comboBoxField.getSelectionModel().select(selected.getNom()+" "+selected.getPrenom());
    }

    // // Handle 'UPDATE' button -Btw No DAO function is used!-
    public void updateAccount() {
        Compte seletedAccount = comptesTable.getSelectionModel().getSelectedItem();
        String updateAccountSql = "update compte set login = ?, password = ? where id_user = ?";

        try(Connection conn = DbConnection.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(updateAccountSql);
            pstmt.setString(1, loginTextField.getText());
            pstmt.setString(2, passwordTextField.getText());
            pstmt.setInt(3, getUserIdByFullName(comboBoxField.getValue()));
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Account updated successfully for user with ID = "+getUserIdByFullName(comboBoxField.getValue())+"!");
                alert.showAndWait();
            }
            clearCreateAccountsForm();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Handle the 'DELETE' button for a selected user ACCOUNT
    public void handleDeleteAccountButtonClick() throws SQLException {
        Compte selectedRow = comptesTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText("This will permanently remove the user and their salary record.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = CompteDAO.deleteAccountById(selectedRow.getIdCompte());
            if (success){
                comptesTable.getItems().remove(selectedRow);
                System.out.println("Account deleted successfully");
            }
            clearCreateAccountsForm();
        }

    }


}