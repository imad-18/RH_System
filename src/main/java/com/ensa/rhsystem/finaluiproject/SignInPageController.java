package com.ensa.rhsystem.finaluiproject;

import com.ensa.rhsystem.finaluiproject.dao.EmployeeDAO;
import com.ensa.rhsystem.finaluiproject.employeeControllers.EmployeeHomeController;
import com.ensa.rhsystem.finaluiproject.employeeControllers.EmployeeMainLayoutController;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;




public class SignInPageController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private Connection connection;

    @FXML
    public void initialize() {
        loginButton.setOnAction(this::handleLogin);
    }


    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String pass = passwordField.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            System.out.println("Error: Please fill in both fields.");
            return;
        }

        try {
            connection = DbConnection.getConnection();
            assert connection != null;

            Map<String, Object> result = EmployeeDAO.SignIn(connection, email, pass);

            if (result != null) {
                int userId = (int) result.get("id_user");
                String role = (String) result.get("role_admin_or_employee");

                // Store user info in session
                Session.loggedInUserId = userId;
                Session.loggedInUserRole = role;

                navigateToHomePage(event, userId, role);
            } else {
                System.out.println("Login Failed: Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Navigation Error: " + e.getMessage());
        }
    }


    private void navigateToHomePage(ActionEvent event, int userId, String role) throws IOException {
        String fxmlFile = role.equalsIgnoreCase("Admin") ? "mainLayout.fxml" : "employee/employee-mainLayout.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        // Optionally pass user ID to the next controller here if needed
        // Example:
        // AdminController controller = loader.getController();
        // controller.setUserId(userId);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /*private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String pass = passwordField.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            return;
        }

        try {
            connection = DbConnection.getConnection();
            assert connection != null;

            Map<String, Object> result = EmployeeDAO.SignIn(connection, email, pass);

            if (result != null) {
                int userId = (int) result.get("id_user");
                String role = (String) result.get("role_admin_or_employee");

                String fxmlPage = role.equalsIgnoreCase("User") ? "../employee/employee-mainLayout.fxml" : "../mainLayout.fxml";
                loadFXMLScene(event, fxmlPage); // Helper method below

            } else {
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/


    /*private void navigateToHomePage(ActionEvent event, int userId, Connection conn) throws IOException, SQLException {
        try {
            // Load the FXML for the home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hrm/project/hrmproject/views/Employee/employee-home.fxml"));
            Parent homePageRoot = loader.load();

            // Get the controller and initialize it with user data
            EmployeeMainLayoutController homeController = loader.getController();
            homeController.initializeData(conn, userId);

            // Set up the new scene
            Scene homeScene = new Scene(homePageRoot);

            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Employee Dashboard");
            stage.setScene(homeScene);
            stage.show();
        } catch (Exception e) {
            // Close the connection if navigation fails
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            throw e;
        }
    }*/



    // Method to clean up resources when the controller is no longer needed
    /*public void cleanup() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
