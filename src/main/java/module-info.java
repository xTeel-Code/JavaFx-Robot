module com.example.helloapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.helloapp to javafx.fxml;
    exports com.example.helloapp;
}