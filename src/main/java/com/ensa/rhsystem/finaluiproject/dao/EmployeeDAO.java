package com.ensa.rhsystem.finaluiproject.dao;

import com.ensa.rhsystem.finaluiproject.modules.Attendance;
import com.ensa.rhsystem.finaluiproject.modules.DateDim;
import com.ensa.rhsystem.finaluiproject.modules.Salary;
import com.ensa.rhsystem.finaluiproject.modules.TrainingEnrollment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDAO {

    /*public static Object SignIn (Connection conn, String email, String pass) throws SQLException {
        String req = "SELECT id_user FROM compte WHERE login= ? AND password= ?";
        PreparedStatement stm = conn.prepareStatement(req);
        stm.setString(1,email);
        stm.setString(2,pass);
        ResultSet rslt = stm.executeQuery();
        if (rslt.next()){
            int id = rslt.getInt("id_user");
            return id;
        }
        return null;
    }*/

    public static Map<String, Object> SignIn(Connection conn, String email, String pass) throws SQLException {
        String req = "SELECT u.id_user, u.role_admin_or_employee "+
        "FROM users u " +
        "JOIN compte c ON u.id_user = c.id_user " +
        "WHERE login= ? AND password= ?";
        PreparedStatement stm = conn.prepareStatement(req);
        stm.setString(1, email);
        stm.setString(2, pass);
        ResultSet rslt = stm.executeQuery();

        if (rslt.next()) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id_user", rslt.getInt("id_user"));
            userData.put("role_admin_or_employee", rslt.getString("role_admin_or_employee"));
            return userData;
        }
        return null;
    }


    /*public static void loadFXMLScene(ActionEvent event, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(EmployeeDAO.class.getResource(fxml));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    public static ObservableList<TrainingEnrollment> training(int id_user, Connection conn) throws SQLException {
        if (conn != null) {
            String req = "SELECT * FROM training_enrollment WHERE id_user = ?";
            PreparedStatement stm = conn.prepareStatement(req);
            stm.setInt(1, id_user);
            ResultSet rslt = stm.executeQuery();

            ObservableList<TrainingEnrollment> training_list = FXCollections.observableArrayList();
            while (rslt.next()) {
                TrainingEnrollment t = new TrainingEnrollment();
                t.setIdEnrollment(rslt.getInt("id_enrollment"));
                t.setCourseName(rslt.getString("course_name"));
                t.setStartDate(rslt.getDate("start_date"));
                t.setEndDate(rslt.getDate("end_date"));
                t.setUser(null);
                t.setStatus(rslt.getString("status"));
                training_list.add(t);
            }
            return training_list;
        }
        return FXCollections.observableArrayList();
    }

    public static List<Object> salary_info(int id_user, Connection conn) throws SQLException {
        if (conn != null) {
            List<Object> salaire_info = new ArrayList<>();

            // Salary
            String req = "SELECT * FROM Salary WHERE id_user = ?";
            PreparedStatement stm = conn.prepareStatement(req);
            stm.setInt(1, id_user);
            ResultSet rslt = stm.executeQuery();
            if (rslt.next()) {
                Salary salaire = new Salary();
                salaire.setIdSalary(rslt.getInt("id_salary"));
                salaire.setBrutSalary(rslt.getBigDecimal("brut_salary"));
                salaire.setBonus(rslt.getBigDecimal("bonus"));
                salaire.setNetSalary(rslt.getBigDecimal("net_salary"));
                salaire.setTaxes(rslt.getBigDecimal("taxes"));
                salaire_info.add(salaire);
            }

            // Salary file
            String req1 = "SELECT file_path FROM salary_file WHERE id_user = ?";
            PreparedStatement stm1 = conn.prepareStatement(req1);
            stm1.setInt(1, id_user);
            ResultSet rslt1 = stm1.executeQuery();
            if (rslt1.next()) {
                salaire_info.add(rslt1.getString("file_path"));
            }

            return salaire_info;
        }
        return new ArrayList<>();
    }

    public static List<Attendance> attendance_info(int id_user, Connection conn) throws SQLException {
        if (conn != null) {
            LocalDate today = LocalDate.now();
            LocalDate fourDaysAgo = today.minusDays(4);

            String query = "SELECT a.*, d.day, d.month, d.year " +
                    "FROM attendance a " +
                    "JOIN date_dim d ON a.id_start_date = d.id_start_date " +
                    "WHERE a.id_user = ? AND " +
                    "STR_TO_DATE(CONCAT(d.year, '-', d.month, '-', d.day), '%Y-%m-%d') BETWEEN ? AND ?";

            PreparedStatement stm = conn.prepareStatement(query);
            stm.setInt(1, id_user);
            stm.setDate(2, Date.valueOf(fourDaysAgo));
            stm.setDate(3, Date.valueOf(today));
            ResultSet rslt = stm.executeQuery();

            List<Attendance> attendanceList = new ArrayList<>();
            while (rslt.next()) {
                Attendance a = new Attendance();
                a.setIdAttendance(rslt.getInt("id_attendance"));
                a.setStatus(rslt.getString("status"));
                a.setRemarks(rslt.getString("remarks"));
                a.setCheckinTime(rslt.getTimestamp("checkin_time"));
                a.setCheckoutTime(rslt.getTimestamp("checkout_time"));
                a.setUser(null);

                DateDim datee = new DateDim();
                datee.setDay(rslt.getInt("day"));
                datee.setMonth(rslt.getInt("month"));
                datee.setYear(rslt.getInt("year"));
                a.setDateDim(datee);

                attendanceList.add(a);
            }
            return attendanceList;
        }
        return new ArrayList<>();
    }

    public static Attendance current_attendance(int id_user, Connection conn, int id_date) throws SQLException {
        if (conn != null) {
            String query = "SELECT a.*, d.day, d.month, d.year " +
                    "FROM attendance a " +
                    "JOIN date_dim d ON a.id_start_date = d.id_start_date " +
                    "WHERE a.id_user = ? AND a.id_start_date = ?";

            PreparedStatement stm = conn.prepareStatement(query);
            stm.setInt(1, id_user);
            stm.setInt(2, id_date);
            ResultSet rslt = stm.executeQuery();

            if (rslt.next()) {
                Attendance att_today = new Attendance();
                att_today.setIdAttendance(rslt.getInt("id_attendance"));
                att_today.setStatus(rslt.getString("status"));
                att_today.setRemarks(rslt.getString("remarks"));
                att_today.setCheckinTime(rslt.getTimestamp("checkin_time"));
                att_today.setCheckoutTime(rslt.getTimestamp("checkout_time"));
                att_today.setUser(null);

                DateDim datee = new DateDim();
                datee.setDay(rslt.getInt("day"));
                datee.setMonth(rslt.getInt("month"));
                datee.setYear(rslt.getInt("year"));
                att_today.setDateDim(datee);

                return att_today;
            }
        }
        return null;
    }*/
}
