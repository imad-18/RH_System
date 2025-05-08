package com.ensa.rhsystem.finaluiproject;

import com.ensa.rhsystem.finaluiproject.modules.Department;
import com.ensa.rhsystem.finaluiproject.modules.User;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ManageEmployeesController implements Initializable {
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private ComboBox<String> departmentComboBox;

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

            // Load departments from the database
            loadDepartmentNames();
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

            // Convert LocalDate from DatePicker to java.sql.Date

// Convert LocalDate from DatePicker to java.sql.Date
            LocalDate hireLocalDate = hireDatePicker.getValue();
            java.sql.Date sqlHireDate = (hireLocalDate != null) ? java.sql.Date.valueOf(hireLocalDate) : null;
            pstmt.setDate(6, sqlHireDate);


            // Get department ID by name
            int departmentId = getDepartmentIdByName(departmentComboBox.getValue());
            pstmt.setInt(7, departmentId);

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





    /*@FXML private TableColumn UserID;
    @FXML private TableColumn RoleID;
    @FXML private TableColumn DepartmentID;
    @FXML private TableColumn FirstName;
    @FXML private TableColumn LastName;
    @FXML private TableColumn Email;
    @FXML private TableColumn Phone;
    @FXML private TableColumn JobTitle;
    @FXML private TableColumn HireDate;

    //Display the user'database into the table
    private ObservableList<User> usersList = FXCollections.observableArrayList();

    public static ResultSet getAllUsers() {
        String userDataQuery = "SELECT * FROM users";
        try {
            Connection conn = DbConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(userDataQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    private void displayUsersData() throws SQLException {

        // Get student data from the database
        ResultSet rs = getAllUsers();
        while (rs != null && rs.next()){
            us
        }
    }*/


    @FXML
    private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> UserID;
    @FXML private TableColumn<User, String> RoleID;
    @FXML private TableColumn<User, Integer> DepartmentID;
    @FXML private TableColumn<User, String> FirstName;
    @FXML private TableColumn<User, String> LastName;
    @FXML private TableColumn<User, String> Email;
    @FXML private TableColumn<User, String> Phone;
    @FXML private TableColumn<User, String> JobTitle;
    @FXML private TableColumn<User, String> HireDate;

    @FXML
    private void displayUsersData() {
        ObservableList<User> userList = FXCollections.observableArrayList();

        String query = "SELECT * FROM users"; // The correct table name is 'user', not 'users'

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

                // Department is referenced by id, we only show the id
                Department department = new Department();
                department.setIdDepartment(rs.getInt("id_department"));
                user.setDepartment(department);

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
                            + ", Department ID: " + (u.getDepartment() != null ? u.getDepartment().getIdDepartment() : "None"));
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
        DepartmentID.setCellValueFactory(cellData -> {
            // Display department ID from the nested Department object
            return new SimpleIntegerProperty(cellData.getValue().getDepartment().getIdDepartment()).asObject();
        });

        usersTable.setItems(userList);
    }






}
