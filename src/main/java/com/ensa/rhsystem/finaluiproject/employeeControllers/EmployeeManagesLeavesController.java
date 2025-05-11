package com.ensa.rhsystem.finaluiproject.employeeControllers;

import com.ensa.rhsystem.finaluiproject.Session;
import com.ensa.rhsystem.finaluiproject.dao.LeavesDAO;
import com.ensa.rhsystem.finaluiproject.dao.UserDAO;
import com.ensa.rhsystem.finaluiproject.modules.User;
import com.ensa.rhsystem.finaluiproject.modules.VacationLeave;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

import static com.ensa.rhsystem.finaluiproject.dao.CompteDAO.getUserIdByFullName;
import static com.ensa.rhsystem.finaluiproject.dao.LeavesDAO.getLeaveTypeIdFromComboBox;
import static com.ensa.rhsystem.finaluiproject.dao.UserDAO.showErrorDialog;

public class EmployeeManagesLeavesController {

    @FXML private TableView<VacationLeave> leaveReqTabView;

    @FXML private TableColumn<VacationLeave, Integer> leaveIdColumn;
    @FXML private TableColumn<VacationLeave, String> leaveTypeColumn;
    @FXML private TableColumn<VacationLeave, String> startDateColumn;
    @FXML private TableColumn<VacationLeave, String> endDateColumn;
    @FXML private TableColumn<VacationLeave, String> statusColumn;



    @FXML private ComboBox<String> leaveTypeComboBox;
    @FXML private DatePicker startDateField;
    @FXML private DatePicker endDateField;


    public void initialize() throws SQLException {
        loadLeaveTypes();
        displayLeavessData();
    }

    private void loadLeaveTypes() throws SQLException {
        String myQuery = "SELECT vacation_type FROM leave_type";
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
                String leaveTypes = rs.getString("vacation_type");
                leaveTypeComboBox.getItems().add(leaveTypes);
            }

        } catch (Exception e) {
            showErrorDialog("Database Error", "An error occurred while loading Leave types.", e);
        }
    }

    public void createCompte() {
        String createAccountQuery = "insert into vacation_leave(start_date, end_date, id_user, id_leave_type) " +
                "VALUES (?, ?, ?, ?);";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(createAccountQuery)) {

            pstmt.setDate(1, java.sql.Date.valueOf(startDateField.getValue()));
            pstmt.setDate(2, java.sql.Date.valueOf(endDateField.getValue()));
            pstmt.setInt(3, Session.loggedInUserId); // use logged in user id
            pstmt.setInt(4, getLeaveTypeIdFromComboBox(leaveTypeComboBox.getValue()));

            pstmt.executeUpdate();
            System.out.println("Leave request submitted successfully.");

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Leave request submitted successfully for user with ID = "+leaveTypeComboBox.getValue()+"!");
                alert.showAndWait();
                //System.out.println("Account created successfully.");
            }

            //clearCreateAccountsForm();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void displayLeavessData() {
        ObservableList<VacationLeave> myVacLeaveList = LeavesDAO.getAllLeaves();
        // Link table columns to User fields
        leaveIdColumn.setCellValueFactory(cellData -> {return new SimpleIntegerProperty(cellData.getValue().getIdVacationLeave()).asObject();});
        leaveTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLeaveReason())
        );
        startDateColumn.setCellValueFactory(cellData -> {
            Date hireDate = cellData.getValue().getStartDate();
            return new SimpleStringProperty(hireDate != null ? hireDate.toString() : "");
        });
        endDateColumn.setCellValueFactory(cellData -> {
            Date endDate = cellData.getValue().getEndDate();
            return new SimpleStringProperty(endDate != null ? endDate.toString() : "");
        });

        statusColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getStatusOkNo());
        });

        leaveReqTabView.setItems(myVacLeaveList);
    }
}
