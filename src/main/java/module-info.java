module com.example.myecosystemjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.myecosystemjavafx to javafx.fxml;
    exports com.example.myecosystemjavafx;
}