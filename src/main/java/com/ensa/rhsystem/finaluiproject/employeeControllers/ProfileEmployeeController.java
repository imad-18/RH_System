package com.ensa.rhsystem.finaluiproject.employeeControllers;



import com.ensa.rhsystem.finaluiproject.Session;
import com.ensa.rhsystem.finaluiproject.dao.EmployeeDAO;
import com.ensa.rhsystem.finaluiproject.modules.Compte;
import com.ensa.rhsystem.finaluiproject.modules.Department;
import com.ensa.rhsystem.finaluiproject.modules.User;
import java.sql.Connection;
import java.sql.SQLException;

import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProfileEmployeeController {

    @FXML private Label fullname;

    @FXML
    private TextField loginField;   // Corresponds to fx:id="emailTextField"
    @FXML
    private TextField passwordField;  // Corresponds to fx:id="passwordField"
    @FXML
    private TextField phoneNumberField;  // Corresponds to fx:id="phoneNumberField"
    @FXML
    private TextField AddressField;      // Corresponds to fx:id="AddressField"
    @FXML
    private TextField BirthDateField;    // Corresponds to fx:id="BirthDateField"
    @FXML
    private TextField HireDateField;     // Corresponds to fx:id="HireDateField"
    @FXML
    private TextField JobTitleField;     // Corresponds to fx:id="JobTitleField"
    @FXML
    private TextField DepartmentTextField;   // Corresponds to fx:id="DepartmentTextField"import java.sql.Connection;





        @FXML
        public void initialize() throws SQLException {
           // Explicit connection initialization
            setUserData();
        }

        // Method to set the user data
        public void setUserData() {
            try {
                // Call the DAO method to get user and account information
                ArrayList<Object> userAndCompteList = EmployeeDAO.getUserAndCompteInfo(Session.loggedInUserId);

                // If data is successfully retrieved, populate the UI elements
                if (userAndCompteList.size() >= 2) {
                    // Extract User and Compte objects from the list
                    User user = (User) userAndCompteList.get(0);
                    Compte compte = (Compte) userAndCompteList.get(1);

                    // Populate the labels with the user data
                    fullname.setText(user.getFirstName() + " " + user.getLastName());
                    loginField.setText(compte.getLogin());
                    passwordField.setText(compte.getPassword());
                    phoneNumberField.setText(user.getPhoneNumber());
                    AddressField.setText(user.getAddress());
                    //BirthDateField.setText(user.getDateOfBirth().toString());
                    //HireDateField.setText(user.getHireDate().toString());

                    BirthDateField.setText(String.valueOf(user.getDateOfBirth()));
                    HireDateField.setText(String.valueOf(user.getHireDate()));

                    JobTitleField.setText(user.getJobTitle());
                    Department d = user.getDepartment();
                    String d_name = d.getName();
                    DepartmentTextField.setText(d_name);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

}
