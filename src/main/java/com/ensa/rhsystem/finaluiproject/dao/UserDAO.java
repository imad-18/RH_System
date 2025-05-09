package com.ensa.rhsystem.finaluiproject.dao;

import com.ensa.rhsystem.finaluiproject.modules.User;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class UserDAO {

    public static ObservableList<User> getAllUsers(){
        ObservableList<User> userList = FXCollections.observableArrayList();

        String query = "SELECT u.*, d.name AS department_name, s.net_salary " +
                "FROM users u " +
                "JOIN department d ON u.id_department = d.id_department " +
                "JOIN salary s ON u.id_user = s.id_user";

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
                user.setRole(rs.getString("role_admin_or_employee"));
                user.setDepartmentName(rs.getString("department_name"));
                user.setNetSalary(rs.getFloat("net_salary"));

                userList.add(user);
            }

            /*if (userList.isEmpty()) {
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
            }*/

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }


    //Method to get the id of the selected Department
    public static int getDepartmentIdByName(String departmentName) throws Exception {
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



    public static void showErrorDialog(String title, String header, Exception e) {
        // Show an alert with the error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(e.getMessage());
        alert.showAndWait();

        // You can also print the stack trace for debugging in the console
        e.printStackTrace();
    }


    // delete the seleceted user
    // Be carefull to not delete the user from the users table before deleting from the salary table.
    // beacause if we did so. We won't be able to delete the user'salary info from salary table.
    // cus the salary tab won't find the 'id_user' -already deleted from the 'users' tab -

    public static boolean deleteUserById(int userId) {
        String deleteSalaryQuery = "DELETE FROM salary WHERE id_user = ?";
        String deleteUserQuery = "DELETE FROM users WHERE id_user = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement salaryPstmt = conn.prepareStatement(deleteSalaryQuery);
             PreparedStatement userPstmt = conn.prepareStatement(deleteUserQuery)) {

            // Delete from child table first (salary)
            salaryPstmt.setInt(1, userId);
            salaryPstmt.executeUpdate();

            // Then delete from parent table (users)
            userPstmt.setInt(1, userId);
            int rowsAffected = userPstmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
