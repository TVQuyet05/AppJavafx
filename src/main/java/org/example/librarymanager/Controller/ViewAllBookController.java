package org.example.librarymanager.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.librarymanager.Model.Book;
import org.example.librarymanager.Service.LibraryDatabase;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAllBookController implements Initializable {

    @FXML
    private ComboBox<String> combobox_search;

    @FXML
    private FlowPane flow_pane;

    @FXML
    private AnchorPane anchor;

    public HBox createBookCard(String title, String author, int year, int quantity, String image) {
        // Tạo HBox chính để chia sách và thông tin
        HBox bookCard = new HBox();
        bookCard.setSpacing(15);
        bookCard.setPrefHeight(200);
        bookCard.setPrefWidth(400);
        bookCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-padding: 10;");
        bookCard.setAlignment(Pos.CENTER_LEFT);

        // Phần ảnh sách
        ImageView imageView = new ImageView();
        try {
            String uri = "file:" + image;
            Image img = new Image(uri, true);
            imageView.setImage(img);
            imageView.setFitWidth(150);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
        } catch (IllegalArgumentException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        // VBox để chứa thông tin (tiêu đề, tác giả, năm, trạng thái)
        VBox infoBox = new VBox();
        infoBox.setSpacing(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(200); // Đặt chiều rộng tối đa để Label có thể xuống dòng
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");

        Label authorLabel = new Label(author);
        authorLabel.setFont(Font.font("System", 14));
        authorLabel.setTextFill(Color.BLACK);

        Label yearLabel = new Label(String.valueOf(year));
        yearLabel.setFont(Font.font("System", 14));
        yearLabel.setTextFill(Color.GRAY);

        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("System", 13));


        if (quantity > 0) {
            statusLabel.setText("Available");
            statusLabel.setTextFill(Color.GREEN);
        } else {
            statusLabel.setText("Unavailable");
            statusLabel.setTextFill(Color.RED);
        }
        infoBox.getChildren().addAll(titleLabel, authorLabel, yearLabel, statusLabel);
        bookCard.getChildren().addAll(imageView, infoBox);

        return bookCard;
    }


    public void ViewAllBook() {
        LibraryDatabase libraryDatabase = LibraryDatabase.getInstance();
        List<Book> books = libraryDatabase.getBooks();


        flow_pane.setHgap(35);
        flow_pane.setVgap(20); // Khoảng cách dọc giữa các card
        flow_pane.setMaxWidth(400);
        flow_pane.setStyle("-fx-padding: 10;"); // Căn chỉnh padding tổng thể

        for (Book book : books) {
            Date date = book.getDate();
            int year = date.getYear() + 1900;

            HBox bookCard = createBookCard(
                    book.getTitle(),
                    book.getAuthor(),
                    year,
                    book.getQuantity(),
                    book.getImage()
            );

            flow_pane.getChildren().add(bookCard);
        }

        anchor.getChildren().clear();
        anchor.getChildren().addAll(flow_pane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combobox_search.getItems().addAll("Local Library", "API");
        ViewAllBook();
    }
}
