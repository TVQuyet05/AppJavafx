package org.example.librarymanager.Controller;

import com.google.zxing.WriterException;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.librarymanager.Model.Book;
import org.example.librarymanager.Service.GoogleBooksAPI;
import org.example.librarymanager.Service.LibraryDatabase;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @FXML
    private Button minimize;

    @FXML
    private Button close;

    @FXML
    private TextField field_searchBook;

    @FXML
    private Button btn_searchBook;


    private void openBookDetail(String title, String author, String isbn,
                                String publicationDate, String status,
                                String category, String description, String imageUrl, int quantity, String preLink) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanager/DetailBook.fxml"));
            Parent root = loader.load();

            DetailBookController controller = loader.getController();

            String typeBook = "API";
            if(preLink.isEmpty()) {
                typeBook = "Local";
            }

            controller.setBookDetails(title, author, isbn, publicationDate, status, category, description, imageUrl, quantity, preLink, typeBook);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root);

            // Thêm hiệu ứng FadeIn cho DetailBook khi hiển thị
            root.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }


    public HBox createBookCard(String title, String author, String isbn, String publicationDate , int quantity, String category, String description, String image, String preLink) {

        // Tạo HBox chính để chia sách và thông tin
        HBox bookCard = new HBox();
        bookCard.setSpacing(15);
        bookCard.setPrefHeight(200);
        bookCard.setPrefWidth(400);
        bookCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-padding: 10; -fx-cursor: hand");
        bookCard.setAlignment(Pos.CENTER_LEFT);


        // Phần ảnh sách
        ImageView imageView = new ImageView();
        try {

            Image img = new Image(image, true);
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

        Label yearLabel = new Label(publicationDate);
        yearLabel.setFont(Font.font("System", 14));
        yearLabel.setTextFill(Color.GRAY);

        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("System", 13));


        if (quantity > 0) {
            statusLabel.setText("Available");
            statusLabel.setPrefHeight(25);
            statusLabel.setPrefWidth(70);
            statusLabel.setAlignment(Pos.CENTER);
            statusLabel.setStyle("-fx-background-color: #e0fae3;\n" +
                    "    -fx-background-radius: 15px 15px 15px;\n" +
                    "    -fx-text-fill: #000000;");
        } else {
            statusLabel.setText("Unavailable");
            statusLabel.setPrefHeight(25);
            statusLabel.setPrefWidth(80);
            statusLabel.setAlignment(Pos.CENTER);
            statusLabel.setStyle("-fx-background-color: #ff5959;\n" +
                    "    -fx-background-radius: 15px 15px 15px;\n" +
                    "    -fx-text-fill: #000000;");
        }
        infoBox.getChildren().addAll(titleLabel, authorLabel, yearLabel, statusLabel);
        bookCard.getChildren().addAll(imageView, infoBox);

        bookCard.setOnMouseClicked(event -> {
            // Thực hiện hiệu ứng FadeOut cho bookCard hoặc container
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(400), anchor); // Hoặc flow_pane
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished(event1 -> {
                // Sau khi hiệu ứng kết thúc, mở giao diện chi tiết sách
                openBookDetail(title, author, isbn, publicationDate,
                        String.valueOf(quantity), category, description, image, quantity, preLink);

                // Khôi phục lại hiệu ứng FadeIn cho anchor hoặc container
                FadeTransition fadeIn = new FadeTransition(Duration.millis(400), anchor); // Hoặc flow_pane
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeTransition.play();
        });

        return bookCard;
    }


    public void ViewAllBook() {
        LibraryDatabase libraryDatabase = LibraryDatabase.getInstance();

        List<Book> books = libraryDatabase.getBooks();

        showListBooks(books);

    }

    public void searchBookFromAPI() {
        String bookTitle = field_searchBook.getText();

        if(bookTitle == null) return;

        GoogleBooksAPI booksAPI = new GoogleBooksAPI();

        List<Book> listBookFromAPI = booksAPI.searchBooks(bookTitle);

        showListBooks(listBookFromAPI);
    }

    public void searchBookFromLibrary() {
        String text_search = field_searchBook.getText();

        //if(text_search == null) return;

        LibraryDatabase database = LibraryDatabase.getInstance();

        List<Book> searchedBook = database.searchBookInDatabase(text_search);

        showListBooks(searchedBook);
    }

    public void searchBook() {



        String option_search = combobox_search.getSelectionModel().getSelectedItem();

        if(option_search == null) return;

        if(option_search.equals("API")) {
            searchBookFromAPI();
        }

        if(option_search.equals("Local Library")) {
            searchBookFromLibrary();
        }

    }

    private void addFadeOutEffect(Node node, Runnable onFinished) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(400));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> onFinished.run());
        fadeTransition.play();
    }

    public void close() {
        Stage stage = (Stage) close.getScene().getWindow();
        Parent root = stage.getScene().getRoot();
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(400), root);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> root.getScene().getWindow().hide());
        fadeTransition.play();
    }

    public void minimize() {
        Stage stage = (Stage) minimize.getScene().getWindow();
        Parent root = stage.getScene().getRoot();

        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.seconds(0.5));
        scaleTransition.setNode(root);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(0);
        scaleTransition.setToY(0);

        scaleTransition.setOnFinished(event -> {
            stage.setIconified(true);
        });

        scaleTransition.play();

        stage.iconifiedProperty().addListener((obs, wasMinimized, isNowMinimized) -> {
            if (!isNowMinimized) {
                ScaleTransition restoreTransition = new ScaleTransition();
                restoreTransition.setDuration(Duration.seconds(0.5));
                restoreTransition.setNode(root);
                restoreTransition.setFromX(0);
                restoreTransition.setFromY(0);
                restoreTransition.setToX(1.0);
                restoreTransition.setToY(1.0);
                restoreTransition.play();
            }
        });
    }


    public void showListBooks(List<Book> books) {

        flow_pane.getChildren().clear();

        flow_pane.setHgap(35);
        flow_pane.setVgap(20); // Khoảng cách dọc giữa các card
        flow_pane.setMaxWidth(400);
        flow_pane.setStyle("-fx-padding: 10;"); // Căn chỉnh padding tổng thể

        for (Book book : books) {

            HBox bookCard = createBookCard(
                    book.getTitle(),
                    book.getAuthor(),
                    book.getId(),
                    book.getDate(),
                    book.getQuantity(),
                    book.getGenre(),
                    book.getDescription(),
                    book.getImage(),
                    book.getPreviewBookLink()
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