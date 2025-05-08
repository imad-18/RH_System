package com.ensa.rhsystem.finaluiproject;

import com.ensa.rhsystem.finaluiproject.modules.Department;
import com.ensa.rhsystem.finaluiproject.modules.User;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ManageEmployeesController implements Initializable {

    @FXML private ComboBox<String> roleComboBox;
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField jobTitleField;
    @FXML private DatePicker hireDatePicker; // Prefer using DatePicker instead

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


    private void showErrorDialog(String title, String header, Exception e) {
        // Show an alert with the error message
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(e.getMessage());
        alert.showAndWait();

        // You can also print the stack trace for debugging in the console
        e.printStackTrace();
    }




    //Insert a new user into database
    @FXML
    private void insertUser() {
        String sql = "INSERT INTO users (first_name, last_name, email_address, phone_number, job_title, hire_date, id_department, role_admin_or_employee) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
            if (rowsInserted > 0) {
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

    //Method to get the id of the selected Department
    private int getDepartmentIdByName(String departmentName) throws Exception {
        String query = "SELECT id_department FROM department WHERE name = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, departmentName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_department");
            } else {
                throw new Exception("Department not found: " + departmentName);
            }
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

    @FXML
    private void displayUsersData() {
        ObservableList<User> userList = FXCollections.observableArrayList();

        String query = "SELECT u.*, d.name AS department_name " +
                "FROM users u " +
                "JOIN department d ON u.id_department = d.id_department"; // The correct table name is 'user', not 'users'

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User();
                user.setIdUser(rs.getInt("id_user"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmailAddress(rs.getString("email_address"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setJobTitle(rs.getString("job_title"));
                user.setHireDate(rs.getDate("hire_date"));

                // Role is stored in 'role' column
                user.setRole(rs.getString("role_admin_or_employee"));

                // 🟢 Set department name
                String deptName = rs.getString("department_name");
                System.out.println("Fetched department name: " + deptName); // Debug
                user.setDepartmentName(deptName);  // ⬅️ Make sure this is called before adding to list

                userList.add(user);
                if (userList.isEmpty()) {
                    System.out.println("No users found.");
                } else {
                    System.out.println("Users loaded: " + userList.size());
                }
                for (User u : userList) {
                    System.out.println("ID: " + u.getIdUser()
                            + ", Name: " + u.getFirstName() + " " + u.getLastName()
                            + ", Email: " + u.getEmailAddress()
                            + ", Role: " + u.getRole()
                            + ", Department naaaamw: " + (u.getDepartmentName() != null ? u.getDepartmentName() : "None"));
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

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


        if (DepartmentName == null) {
            System.out.println("⚠ DepartmentName column is not initialized!");
        }

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
    }






}
