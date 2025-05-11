package com.ensa.rhsystem.finaluiproject.dao;

import com.ensa.rhsystem.finaluiproject.modules.LeaveType;
import com.ensa.rhsystem.finaluiproject.modules.VacationLeave;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class LeavesDAO {

    public static ObservableList<VacationLeave> getLeavesRequest() throws SQLException {

        ObservableList<VacationLeave> vacationLeaveList = FXCollections.observableArrayList();

        String leavesQuery = "SELECT u.first_name, u.last_name, vl.*, lt.vacation_type " +
                "FROM users u " +
                "JOIN vacation_leave vl ON u.id_user = vl.id_user " +
                "JOIN leave_type lt ON vl.id_leave_type = lt.id_leave_type";

        Connection conn = DbConnection.getConnection();

        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(leavesQuery)){

            while(rs.next()){
                VacationLeave vacationLeave = new VacationLeave();
                vacationLeave.setFirstName(rs.getString("first_name"));
                vacationLeave.setLastName(rs.getString("last_name"));
                vacationLeave.setIdVacationLeave(rs.getInt("id_vacation_leave"));
                vacationLeave.setStartDate(rs.getDate("start_date"));
                vacationLeave.setEndDate(rs.getDate("end_date"));
                vacationLeave.setIdUser(rs.getInt("id_user"));
                vacationLeave.setLeaveReason(rs.getString("vacation_type"));

                vacationLeaveList.add(vacationLeave);

            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return vacationLeaveList;
    }


    public static void updateLeaveStatusInDatabase(VacationLeave selectedLeave) throws SQLException {
        String updateQuery = "UPDATE vacation_leave SET status_ok_no = ? WHERE id_vacation_leave = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, selectedLeave.getStatusOkNo());
            pstmt.setInt(2, selectedLeave.getIdVacationLeave());

            int rowsUpdated = pstmt.executeUpdate();

        }
    }

    public static void showError(String databaseError, String message) {
        return;
    }

}
