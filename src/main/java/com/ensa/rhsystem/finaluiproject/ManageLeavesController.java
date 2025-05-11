package com.ensa.rhsystem.finaluiproject;

import com.ensa.rhsystem.finaluiproject.dao.LeavesDAO;
import com.ensa.rhsystem.finaluiproject.modules.VacationLeave;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

import static com.ensa.rhsystem.finaluiproject.dao.LeavesDAO.*;
import static com.ensa.rhsystem.finaluiproject.dao.UserDAO.showErrorDialog;

public class ManageLeavesController {

    @FXML private TableView<VacationLeave> leavesTableView;

    @FXML private TableColumn<VacationLeave, Integer> leaveIdColumn;

    @FXML private TableColumn<VacationLeave, Integer> userIdColumn;

    @FXML private TableColumn<VacationLeave, String> userNameColumn;

    @FXML private TableColumn<VacationLeave, String> leaveTypeColumn;

    @FXML private TableColumn<VacationLeave, String> startDateColumn;

    @FXML private TableColumn<VacationLeave, String> endDateColumn;



    @FXML private TextField userIdField;

    @FXML private TextField userNameField;

    @FXML private ComboBox<String> leaveTypeComboBox;

    @FXML private DatePicker endDateField;

    @FXML private DatePicker startDateField;


    @FXML private Button approveButton;
    @FXML private Button denyButton;

    public void initialize() throws SQLException {
        loadLeaveTypes();
        displayLeavesRequest();

        leavesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    populateFieldsWithSelectedLeaveRequest(newSelection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        leavesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean disable = (newSelection == null);
            approveButton.setDisable(disable);
            denyButton.setDisable(disable);
        });

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

    public void displayLeavesRequest() throws SQLException {

        ObservableList<VacationLeave> vacationLeavesList = getLeavesRequest();

        leaveIdColumn.setCellValueFactory(cellData -> {
            return new SimpleIntegerProperty(cellData.getValue().getIdVacationLeave()).asObject();
        });
        userIdColumn.setCellValueFactory(cellData -> {
            return new SimpleIntegerProperty(cellData.getValue().getIdUser()).asObject();
        });
        startDateColumn.setCellValueFactory(cellData -> {
                Date start_date = cellData.getValue().getStartDate();
                return new SimpleStringProperty(start_date.toString());
        });
        endDateColumn.setCellValueFactory(cellData -> {
                Date end_date = cellData.getValue().getEndDate();
                return new SimpleStringProperty(end_date.toString());
        });
        leaveTypeColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLeaveReason());
        });
        userNameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName());
        });


        leavesTableView.setItems(vacationLeavesList);
    }

    public void populateFieldsWithSelectedLeaveRequest(VacationLeave selectedLeave) throws SQLException {
        userIdField.setText(String.valueOf(selectedLeave.getIdUser()));
        userNameField.setText(selectedLeave.getFirstName() + " " + selectedLeave.getLastName());
        leaveTypeComboBox.setValue(selectedLeave.getLeaveReason());
        startDateField.setValue(selectedLeave.getStartDate().toLocalDate());
        endDateField.setValue(selectedLeave.getEndDate().toLocalDate());
    }


    @FXML
    public void approveLeaveRequest(ActionEvent event) {
        VacationLeave selectedLeave = leavesTableView.getSelectionModel().getSelectedItem();
        if (selectedLeave != null) {
            try {
                selectedLeave.setStatusOkNo("Approved");

                // Update it in the database
                updateLeaveStatusInDatabase(selectedLeave);

                // Refresh the table to show new status/color
                leavesTableView.refresh();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Success");
                alert.setHeaderText("Leave with id= "+selectedLeave.getIdVacationLeave()+" has been approved.");
                alert.showAndWait();

                clearLeaveForm();
            } catch (SQLException e) {
                showError("Database Error", e.getMessage());
            }
        } else {
            showError("No Selection", "Please select a leave request to approve.");
        }
    }


    @FXML
    public void denyLeaveRequest(ActionEvent event) {
        VacationLeave selectedLeave = leavesTableView.getSelectionModel().getSelectedItem();
        if (selectedLeave != null) {
            try {
                selectedLeave.setStatusOkNo("Denied");
                updateLeaveStatusInDatabase(selectedLeave);
                leavesTableView.refresh();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Success");
                alert.setHeaderText("Leave with id= "+selectedLeave.getIdVacationLeave()+" has been denied!!");
                alert.showAndWait();

                clearLeaveForm();
            } catch (SQLException e) {
                showError("Database Error", e.getMessage());
            }
        } else {
            showError("No Selection", "Please select a leave request to deny.");
        }
    }


    public void clearLeaveForm(){
        userIdField.clear();
        userNameField.clear();
        leaveTypeComboBox.setValue(null);
        startDateField.setValue(null);
        endDateField.setValue(null);
    }




}
