package org.example.librarymanager.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewAllBookController implements Initializable {

    @FXML
    private ComboBox<String> combobox_search;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combobox_search.getItems().addAll("Local Library", "API");
    }
}
