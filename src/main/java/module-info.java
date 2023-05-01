module AppointmentManagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires java.desktop;
    opens AppointmentManagementSystem to javafx.fxml;
    exports AppointmentManagementSystem;
    exports AppointmentManagementSystem.database;
    opens AppointmentManagementSystem.database to javafx.fxml;
    exports AppointmentManagementSystem.models;
    opens AppointmentManagementSystem.models to javafx.fxml;
}