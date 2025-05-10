package com.ensa.rhsystem.finaluiproject;

import com.ensa.rhsystem.finaluiproject.dao.UserDAO;
import com.ensa.rhsystem.finaluiproject.modules.User;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.ensa.rhsystem.finaluiproject.dao.UserDAO.getDepartmentIdByName;
import static com.ensa.rhsystem.finaluiproject.dao.UserDAO.showErrorDialog;

public class ManageEmployeesController implements Initializable {

    @FXML private ComboBox<String> roleComboBox;
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField jobTitleField;
    @FXML private DatePicker hireDatePicker; // Prefer using DatePicker instead
    @FXML private TextField salaryField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Get roles from a User instance
            User tempUser = new User(); // Only needed to access the array
            roleComboBox.getItems().addAll(Arrays.asList(tempUser.getRoleAdminOrEmployee()));


            usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateFieldsWithSelectedUser(newSelection);
                }
            });


            // Load departments from the database
            loadDepartmentNames();
            displayUsersData();

        } catch (Exception e) {
            showErrorDialog("Initialization Error", "An error occurred while initializing the controller.", e);
        }
    }

    private void loadDepartmentNames() throws SQLException {
        String myQuery = "SELECT name FROM department";
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
                String departmentName = rs.getString("name");
                departmentComboBox.getItems().add(departmentName);
            }

        } catch (Exception e) {
            showErrorDialog("Database Error", "An error occurred while loading department names.", e);
        }
    }



    //Insert a new user into database
    @FXML
    private void insertUser() {
        String sql = "INSERT INTO users (first_name, last_name, email_address, phone_number, job_title, hire_date, id_department, role_admin_or_employee) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, firstNameField.getText());
            pstmt.setString(2, lastNameField.getText());
            pstmt.setString(3, emailField.getText());
            pstmt.setString(4, phoneField.getText());
            pstmt.setString(5, jobTitleField.getText());

            LocalDate hireLocalDate = hireDatePicker.getValue();
            java.sql.Date sqlHireDate = (hireLocalDate != null) ? java.sql.Date.valueOf(hireLocalDate) : null;
            pstmt.setDate(6, sqlHireDate);

            // Get department ID by name
            int departmentId = getDepartmentIdByName(departmentComboBox.getValue());
            pstmt.setInt(7, departmentId);

            System.out.println("dep name = " + departmentComboBox.getValue());

            pstmt.setString(8, roleComboBox.getValue());

            int rowsInserted = pstmt.executeUpdate();
            int userId = -1;
            if (rowsInserted > 0) {

                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }

                if (userId != -1) {
                    String insertSalarySql = "INSERT INTO salary (id_user, net_salary) VALUES (?, ?)";
                    try (PreparedStatement salaryPstmt = conn.prepareStatement(insertSalarySql)) {
                        salaryPstmt.setInt(1, userId);
                        salaryPstmt.setDouble(2, Double.parseDouble(salaryField.getText()));
                        salaryPstmt.executeUpdate();
                    }
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User inserted successfully!");
                alert.showAndWait();
                System.out.println("User inserted successfully.");

                clearFormFields();
            }

        } catch (Exception e) {
            showErrorDialog("Database Error", "An error occurred while inserting the user.", e);
        }
    }




    private void clearFormFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        jobTitleField.clear();
        hireDatePicker.setValue(null);
        departmentComboBox.getSelectionModel().clearSelection();
        roleComboBox.getSelectionModel().clearSelection();
        salaryField.clear();
    }
    //------------------------------------------------



    //------------ Display data in the table view ---------
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> UserID;
    @FXML private TableColumn<User, String> RoleID;
    @FXML private TableColumn<User, String> DepartmentName;
    @FXML private TableColumn<User, String> FirstName;
    @FXML private TableColumn<User, String> LastName;
    @FXML private TableColumn<User, String> Email;
    @FXML private TableColumn<User, String> Phone;
    @FXML private TableColumn<User, String> JobTitle;
    @FXML private TableColumn<User, String> HireDate;
    @FXML private TableColumn<User, Float> NetSalary;


    // Display User data with the user table view
    @FXML
    private void displayUsersData() {
        ObservableList<User> userList = UserDAO.getAllUsers();
        // Link table columns to User fields
        UserID.setCellValueFactory(cellData -> {return new SimpleIntegerProperty(cellData.getValue().getIdUser()).asObject();});
        FirstName.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getFirstName());});
        LastName.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getLastName());});
        Email.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getEmailAddress());});
        Phone.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getPhoneNumber());});
        JobTitle.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getJobTitle());});
        HireDate.setCellValueFactory(cellData -> {
            Date hireDate = cellData.getValue().getHireDate();
            return new SimpleStringProperty(hireDate != null ? hireDate.toString() : "");
        });
        RoleID.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getRole());});
        DepartmentName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDepartmentName())
        );

        NetSalary.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getNetSalary())
        );

        usersTable.setItems(userList);
    }


    // -------------- get the selected row -------------------
    //whenever a row is clicked/selected, all the user’s data will be displayed instantly in the input fields
    // — no button needed.

    private void populateFieldsWithSelectedUser(User selected) {
        firstNameField.setText(selected.getFirstName());
        lastNameField.setText(selected.getLastName());
        emailField.setText(selected.getEmailAddress());
        phoneField.setText(selected.getPhoneNumber());
        jobTitleField.setText(selected.getJobTitle());

        // Set department name in ComboBox
        //String deptName = getDepartmentNameById(selected.getDepartment().getId());
        departmentComboBox.setValue(selected.getDepartmentName());

        // Set role
        roleComboBox.setValue(selected.getRole()); // Assuming getRole() returns "Admin" or "User"

        // Set hire date
        if (selected.getHireDate() != null) {
            hireDatePicker.setValue(selected.getHireDate().toLocalDate());
        }
        // Set net salary
        salaryField.setText(String.valueOf(selected.getNetSalary()));
    }


    // Handle 'UPDATE' button -Btw No DAO function is used!-
    public void updateUser() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No user selected.");
            return;
        }

        String userUpdateQuery = "UPDATE users SET first_name = ?, last_name = ?, email_address = ?, phone_number = ?, job_title = ?, hire_date = ?, id_department = ?, role_admin_or_employee = ? WHERE id_user = ?";
        String salaryUpdateQuery = "UPDATE salary SET net_salary = ? WHERE id_user = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement userPstmt = conn.prepareStatement(userUpdateQuery);
             PreparedStatement salaryPstmt = conn.prepareStatement(salaryUpdateQuery)) {

            // Update users table
            userPstmt.setString(1, firstNameField.getText());
            userPstmt.setString(2, lastNameField.getText());
            userPstmt.setString(3, emailField.getText());
            userPstmt.setString(4, phoneField.getText());
            userPstmt.setString(5, jobTitleField.getText());

            LocalDate hireLocalDate = hireDatePicker.getValue();
            userPstmt.setDate(6, hireLocalDate != null ? java.sql.Date.valueOf(hireLocalDate) : null);

            int departmentId = getDepartmentIdByName(departmentComboBox.getValue());
            userPstmt.setInt(7, departmentId);

            userPstmt.setString(8, roleComboBox.getValue());
            userPstmt.setInt(9, selected.getIdUser());

            userPstmt.executeUpdate();

            // Update salary table
            double netSalary = Double.parseDouble(salaryField.getText());
            salaryPstmt.setDouble(1, netSalary);
            salaryPstmt.setInt(2, selected.getIdUser());

            salaryPstmt.executeUpdate();

            System.out.println("User and salary updated successfully.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User updated successfully!");
            alert.showAndWait();
            System.out.println("User updated successfully.");

            clearFormFields();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // Handle the 'DELETE' button for a selected user/row
    @FXML
    private void handleDeleteButtonClick() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No user selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText("This will permanently remove the user and their salary record.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = UserDAO.deleteUserById(selected.getIdUser());
            if (success) {
                usersTable.getItems().remove(selected);  // remove from TableView
                System.out.println("User deleted successfully.");
            } else {
                System.err.println("Failed to delete user.");
            }
        }
        clearFormFields();
    }


}
