package com.ensa.rhsystem.finaluiproject.employeeControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

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
}
