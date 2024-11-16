package org.example.librarymanager.Controller;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DetailBookController implements Initializable {

    @FXML
    private AnchorPane anchor_detail_Books;

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

    private double x = 0;
    private double y = 0;

    public void backtoDashboard(javafx.event.ActionEvent actionEvent) {
        try {
            if (actionEvent.getSource() == back_to_Dashboard) {
                Parent root1 = back_to_Dashboard.getParent();

                // Tạo hiệu ứng mờ dần cho cửa sổ hiện tại
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), root1);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);

                // Bắt đầu hiệu ứng
                fadeOut.setOnFinished(event -> {
                    root1.getScene().getWindow().hide();
                });
                fadeOut.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
