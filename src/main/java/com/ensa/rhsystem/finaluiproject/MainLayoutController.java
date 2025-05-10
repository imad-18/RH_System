package com.ensa.rhsystem.finaluiproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class
MainLayoutController {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    public void initialize() {
        try {
            Parent adminHomePage = FXMLLoader.load(getClass().getResource("home-page.fxml"));
            mainBorderPane.setCenter(adminHomePage);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load the home page.").showAndWait();
        }
    }

    public void handleHomeButton(ActionEvent actionEvent) {
        try {
            Parent adminHomePage = FXMLLoader.load(getClass().getResource("home-page.fxml"));
            mainBorderPane.setCenter(adminHomePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleManageEmployeesButton(ActionEvent actionEvent) {
        try {
            Parent manageEmployeesPage = FXMLLoader.load(getClass().getResource("manage-employees.fxml"));
            mainBorderPane.setCenter(manageEmployeesPage);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleManageUsersButton(ActionEvent event) {
        try {
            Parent manageUsersPage = FXMLLoader.load(getClass().getResource("manage-users.fxml"));
            mainBorderPane.setCenter(manageUsersPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleManageLeavesButton(ActionEvent actionEvent) {
        try {
            Parent manageLeavesPage = FXMLLoader.load(getClass().getResource("manage-leaves.fxml"));
            mainBorderPane.setCenter(manageLeavesPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //To delete. It was used just for testing!!
    public void handleManageLeavesByEmployeeButton(ActionEvent actionEvent) {
        try {
            Parent manageLeavesByEmployeePage = FXMLLoader.load(getClass().getResource("employee/employee-manages-leaves.fxml"));
            mainBorderPane.setCenter(manageLeavesByEmployeePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
