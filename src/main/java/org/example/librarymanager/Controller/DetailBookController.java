package org.example.librarymanager.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DetailBookController implements Initializable {

    @FXML
    private Label author_Book;

    @FXML
    private Button back_to_Dashboard;

    @FXML
    private Button button_borrow_book;

    @FXML
    private Label category_book;

    @FXML
    private Label description_book;

    @FXML
    private ImageView image_book;

    @FXML
    private Label isbn_books;

    @FXML
    private Label name_Book;

    @FXML
    private Label page_book;

    @FXML
    private Label publication_date_Book;

    @FXML
    private ImageView qr_book;

    @FXML
    private Label status_book;

    public void backtoDashboard() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) back_to_Dashboard.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
