module com.ensa.rhsystem.finaluiproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ensa.rhsystem.finaluiproject to javafx.fxml;
    exports com.ensa.rhsystem.finaluiproject;
}