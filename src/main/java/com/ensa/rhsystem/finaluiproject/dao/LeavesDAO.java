package com.ensa.rhsystem.finaluiproject.dao;

import com.ensa.rhsystem.finaluiproject.modules.LeaveType;
import com.ensa.rhsystem.finaluiproject.modules.VacationLeave;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

}
