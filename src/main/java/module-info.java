module com.ensa.rhsystem.finaluiproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.ensa.rhsystem.finaluiproject to javafx.fxml;
    exports com.ensa.rhsystem.finaluiproject;
    exports com.ensa.rhsystem.finaluiproject.employeeControllers;
    opens com.ensa.rhsystem.finaluiproject.employeeControllers to javafx.fxml;
}