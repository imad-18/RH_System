package com.ensa.rhsystem.finaluiproject.employeeControllers;

import com.ensa.rhsystem.finaluiproject.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class EmployeeMainLayoutController {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    public void initialize() {
        try {
            Parent employeeHomePage = FXMLLoader.load(getClass().getResource("/com/ensa/rhsystem/finaluiproject/employee/employee-home.fxml"));
            mainBorderPane.setCenter(employeeHomePage);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load the home page.").showAndWait();
        }
    }

    @FXML
    public void handleHomeButton(ActionEvent event) {
        try {
            Parent employeeHomePage = FXMLLoader.load(getClass().getResource("/com/ensa/rhsystem/finaluiproject/employee/employee-home.fxml"));
            mainBorderPane.setCenter(employeeHomePage);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load the home page.").showAndWait();
        }
    }

    public void handleManageLeavesByEmployeeButton(ActionEvent actionEvent) {
        try {
            Parent manageLeavesPage = FXMLLoader.load(getClass().getResource("/com/ensa/rhsystem/finaluiproject/employee/employee-manages-leaves.fxml"));
            mainBorderPane.setCenter(manageLeavesPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCoursesEventsButton(ActionEvent actionEvent) {
        try {
            Parent manageLeavesPage = FXMLLoader.load(getClass().getResource("/com/ensa/rhsystem/finaluiproject/employee/employee-courses.fxml"));
            mainBorderPane.setCenter(manageLeavesPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserData(MouseEvent mouseEvent) {
        try {
            Parent employeeProfilePage = FXMLLoader.load(getClass().getResource("/com/ensa/rhsystem/finaluiproject/employee/employee-profile.fxml"));
            mainBorderPane.setCenter(employeeProfilePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("You will be redirected to the login page.");

        // Show the alert and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // If the user clicks "OK" (CONFIRMATION), proceed with logout
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Clear session
            Session.clear();

            // Navigate back to the login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ensa/rhsystem/finaluiproject/signIn-page.fxml"));
            Parent root = loader.load();

            // Get current stage and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

}
