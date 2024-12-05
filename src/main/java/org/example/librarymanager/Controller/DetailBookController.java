package org.example.librarymanager.Controller;

import com.google.zxing.WriterException;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.librarymanager.Model.Book;
import org.example.librarymanager.Model.CommentBook;
import org.example.librarymanager.Service.LibraryDatabase;
import org.example.librarymanager.Service.QRCodeBook;
import org.example.librarymanager.Util.StageManager;
import org.example.librarymanager.Util.getData;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.example.librarymanager.Util.getData.typeOfUser;

public class DetailBookController implements Initializable {

    @FXML
    private AnchorPane anchor_detail_Books;

    @FXML
    private Label author_Book;

    @FXML
    private Label lable_typeOfBook;

    @FXML
    private Button back_to_Dashboard;

    @FXML
    private Button button_borrow_book;

    @FXML
    private Button btn_addBookToLib;

    @FXML
    private Button save_book_button;

    @FXML
    private Label category_book;

    @FXML
    private Label description_book;

    @FXML
    private ImageView image_book;

    @FXML
    private ImageView image_QRCode;

    @FXML
    private Label isbn_books;

    @FXML
    private Label name_Book;

    @FXML
    private Label publication_date_Book;

    @FXML
    private Label path_ImageBook;

    @FXML
    private Label label_quantityBook;

    @FXML
    private ImageView qr_book;

    @FXML
    private Label status_book;

    @FXML
    private TableView<CommentBook> tableView_commentForBook;

    @FXML
    private TableColumn<?, ?> col_stdName_cmtBook;

    @FXML
    private TableColumn<?, ?> col_comment_cmtBook;

    @FXML
    private TableColumn<?, ?> col_judge_cmtBook;

//    @FXML
//    private Button btn_addBookToLib;
//    @FXML
//    private Button save_book_button;

    private double x = 0;
    private double y = 0;

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void setActionSaveButton() {

        String studentNumber = getData.numberOfUser;
        String isbn = isbn_books.getText();


        boolean isSaved = LibraryDatabase.getInstance().saveBook(studentNumber, isbn);

        if (isSaved) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "The book has been saved successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "This book has already been saved!");
        }

    }



    @FXML
    private void setActionBorrowButton() {
        button_borrow_book.setOnAction(event -> {

            if(status_book.getText().equals("Unavailable")) {
                showAlert(Alert.AlertType.ERROR, "Error", "The book is not in the library or has been borrowed out.");
                return;
            }

            String studentNumber = getData.numberOfUser;
            String isbn = isbn_books.getText();

            if (isbn != null && !isbn.isEmpty()) {
                boolean isSuccess = LibraryDatabase.getInstance().borrowBook(studentNumber, isbn);

                if (isSuccess) {
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Mượn sách thành công!");

                } else {
                    showAlert(Alert.AlertType.ERROR, "Thất bại", "Mượn sách thất bại! Bạn cần trả cuốn sách này trước khi mượn tiếp.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Lỗi", "Không tìm thấy mã ISBN của sách!");
            }
        });
    }

    public void backtoDashboard(javafx.event.ActionEvent actionEvent) {
        try {
            if (actionEvent.getSource() == back_to_Dashboard) {
                Parent root1 = back_to_Dashboard.getParent();

                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), root1);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);

                fadeOut.setOnFinished(event -> {
                    root1.getScene().getWindow().hide();
                });
                fadeOut.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBookDetails(String title, String author, String isbn, String publicationDate, String status,
                               String category, String description, String imageUrl, int quantityBook, String preLink,
                               String typeBook) throws IOException, WriterException {
        name_Book.setText(title);
        name_Book.setMaxHeight(80);
        author_Book.setText(author);
        isbn_books.setText(isbn);
        publication_date_Book.setText(publicationDate);
        path_ImageBook.setText(imageUrl);
        label_quantityBook.setText(String.valueOf(quantityBook));
        lable_typeOfBook.setText(typeBook);

        if(lable_typeOfBook.getText().equals("Local")) {
            tableView_commentForBook.setVisible(true);

            LibraryDatabase database = LibraryDatabase.getInstance();
            ObservableList<CommentBook> listComment = database.getCommentsByISBN(isbn_books.getText());

            col_stdName_cmtBook.setCellValueFactory(new PropertyValueFactory<>("studentName"));
            col_comment_cmtBook.setCellValueFactory(new PropertyValueFactory<>("comment"));
            col_judge_cmtBook.setCellValueFactory(new PropertyValueFactory<>("judge"));
            tableView_commentForBook.setItems(listComment);

        }

        //String preLink = "https://news.khangz.com/wp-content/uploads/2024/12/Chill-guy-la-gi-6.jpg";
        if(!preLink.equals("")) {
            QRCodeBook QRcode = new QRCodeBook();
            Image imageQR = QRcode.generateQRCodeImage(preLink, 225, 225);

            image_QRCode.setImage(imageQR);
        }


        if(typeOfUser.equals("STUDENT")) {
            btn_addBookToLib.setVisible(false);
        } else {
            button_borrow_book.setVisible(false);
            save_book_button.setVisible(false);
        }


        if (status != null && !status.isEmpty() && Integer.parseInt(status) > 0) {
            status_book.setText("Available");
        } else {
            status_book.setText("Unavailable");
            status_book.setStyle("-fx-background-color: #ff5959;");
        }
        category_book.setText(category);
//        category_book.layoutYProperty().bind(author_Book.layoutYProperty().add(author_Book.heightProperty()).add(10));
        description_book.setText(description);

        try {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                image_book.setImage(new javafx.scene.image.Image(imageUrl, true));
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải ảnh: " + e.getMessage());
        }
    }

    public void addBookToLibrary() throws IOException {

        if ("STUDENT".equals(typeOfUser)) {
            return;
        }

        Book newbook = new Book(
                isbn_books.getText(),
                name_Book.getText(),
                author_Book.getText(),
                category_book.getText(),
                publication_date_Book.getText(),
                description_book.getText(),
                Integer.parseInt(label_quantityBook.getText()),
                path_ImageBook.getText()
        );

        Stage existingStage = StageManager.getStage("DashBoard");
        if (existingStage != null) {
            existingStage.close();
            StageManager.removeStage("DashBoard");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanager/DashBoard.fxml"));
        Parent root = loader.load();

        DashBoardController controller = loader.getController();
        controller.addBooks();
        controller.setInfoForBook(newbook);

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("DashBoard");
        stage.show();

        StageManager.addStage("DashBoard", stage);

        btn_addBookToLib.getScene().getWindow().hide();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
