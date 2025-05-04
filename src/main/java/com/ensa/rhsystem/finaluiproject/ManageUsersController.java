package com.ensa.rhsystem.finaluiproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ManageUsersController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}