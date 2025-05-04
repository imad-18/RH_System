package com.ensa.rhsystem.finaluiproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class
MainLayoutController {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    public void handleHomeButton(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("manage-users.fxml"));
            mainBorderPane.setCenter(homePage);
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
}
