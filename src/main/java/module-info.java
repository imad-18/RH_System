module com.ensa.rhsystem.finaluiproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.ensa.rhsystem.finaluiproject to javafx.fxml;
    exports com.ensa.rhsystem.finaluiproject;
}