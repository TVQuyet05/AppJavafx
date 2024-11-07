module org.example.librarymanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.librarymanager to javafx.fxml;
    exports org.example.librarymanager;
    exports org.example.librarymanager.Controller;
    opens org.example.librarymanager.Controller to javafx.fxml;
}