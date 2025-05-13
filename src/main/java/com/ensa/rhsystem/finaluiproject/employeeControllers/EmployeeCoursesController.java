package com.ensa.rhsystem.finaluiproject.employeeControllers;

import com.ensa.rhsystem.finaluiproject.Session;
import com.ensa.rhsystem.finaluiproject.modules.Event;
import com.ensa.rhsystem.finaluiproject.modules.TrainingEnrollment;
import com.ensa.rhsystem.finaluiproject.utils.DbConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class EmployeeCoursesController {

    // Explicit static-like connection initialization
    private final Connection conn = DbConnection.getConnection();

    private int userId = Session.loggedInUserId; // Default or hardcoded; replace with dynamic value as needed

    // FXML elements
    @FXML
    private TableView<TrainingEnrollment> courseTable;
    @FXML
    private TableColumn<TrainingEnrollment, String> colCourseName;
    @FXML
    private TableColumn<TrainingEnrollment, String> colCourseStart;
    @FXML
    private TableColumn<TrainingEnrollment, String> colCourseEnd;

    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, String> colEventName;
    @FXML
    private TableColumn<Event, String> colEventDate;

    @FXML
    private Button enrollButton;
    @FXML
    private Button participateButton;

    public EmployeeCoursesController() throws SQLException {
    }

    // Called automatically by JavaFX
    @FXML
    public void initialize() {
        // Setup columns
        colCourseName.setCellValueFactory(cellData -> {return new SimpleStringProperty(cellData.getValue().getCourseName());});
        colCourseStart.setCellValueFactory(cellData -> {
            Date course_start_date = cellData.getValue().getStartDate();
            return new SimpleStringProperty(course_start_date.toString());
        });
        colCourseEnd.setCellValueFactory(cellData -> {
            Date course_end_date = cellData.getValue().getEndDate();
            return new SimpleStringProperty(course_end_date.toString());
        });

        colEventName.setCellValueFactory(cellData -> { return new SimpleStringProperty(cellData.getValue().getEventDescription());});
        //colEventDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));

        colEventDate.setCellValueFactory(cellData -> {
            Date start_date = cellData.getValue().getEventDate();
            return new SimpleStringProperty(start_date.toString());
        });
        // Load data
        loadCourseData();
        loadEventData();
    }

    private void loadCourseData() {
        ObservableList<TrainingEnrollment> courseList = FXCollections.observableArrayList();

        // Sample static data
        courseList.add(new TrainingEnrollment("Advanced Java Development", Date.valueOf("2025-06-01"), Date.valueOf("2025-06-30")));
        courseList.add(new TrainingEnrollment("Digital Marketing Basics", Date.valueOf("2025-06-05"), Date.valueOf("2025-06-25")));
        courseList.add(new TrainingEnrollment("Sales Strategies for 2025", Date.valueOf("2025-07-01"), Date.valueOf("2025-07-15")));

        courseTable.setItems(courseList);
    }

    private void loadEventData() {
        ObservableList<Event> eventList = FXCollections.observableArrayList();
        String query = "SELECT * FROM eventt";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                event.setIdEvent(rs.getInt("id_event"));
                event.setEventDescription(rs.getString("event_description"));
                event.setEventDate(rs.getDate("eventDate"));
                System.out.println(rs.getDate("eventDate"));

                eventList.add(event);
            }
            eventTable.setItems(eventList);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load event data.");
        }
    }

    @FXML
    private void enrollInCourse() {
        TrainingEnrollment selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("No Course Selected", "Please select a course to enroll in.");
            return;
        }

        String query = "INSERT INTO training_enrollment (course_name, start_date, end_date, status, id_user) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedCourse.getCourseName());
            pstmt.setDate(2, selectedCourse.getStartDate());
            pstmt.setDate(3, selectedCourse.getEndDate());
            pstmt.setString(4, "");
            pstmt.setInt(5, userId);
            pstmt.executeUpdate();
            showAlert("Enrollment Successful", "You have successfully enrolled in " + selectedCourse.getCourseName() + ".");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to enroll in the course.");
        }
    }

    @FXML
    private void participateInEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("No Event Selected", "Please select an event to participate in.");
            return;
        }

        String query = "INSERT INTO user_event_calendar (id_user, id_event) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, selectedEvent.getIdEvent());
            pstmt.executeUpdate();
            showAlert("Participation Successful", "You have successfully registered for the event.");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to register for the event.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Optional: method to set user ID dynamically
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
