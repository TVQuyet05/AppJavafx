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

    public void setBookDetails(String title, String author, String isbn, String publicationDate, String status, String category, String description, String imageUrl) {
        name_Book.setText(title);
        name_Book.setMaxHeight(80);
        author_Book.setText(author);
        isbn_books.setText(isbn);
        publication_date_Book.setText(publicationDate);
        if (status != null && !status.isEmpty() && Integer.parseInt(status) > 0) {
            status_book.setText("Available");
        } else {
            status_book.setText("Unavailable");
            status_book.setStyle("-fx-background-color: #ff5959;");
        }
        category_book.setText(category);
//        category_book.layoutYProperty().bind(author_Book.layoutYProperty().add(author_Book.heightProperty()).add(10));
        description_book.setText(description);

        // Cập nhật hình ảnh sách
        try {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                image_book.setImage(new javafx.scene.image.Image("file:" + imageUrl, true));
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải ảnh: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
