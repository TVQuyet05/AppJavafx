module org.example.librarymanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires com.google.gson;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;


    opens org.example.librarymanager to javafx.fxml;
    opens org.example.librarymanager.Model to javafx.base;

    exports org.example.librarymanager;
    exports org.example.librarymanager.Controller;
    opens org.example.librarymanager.Controller to javafx.fxml;
}