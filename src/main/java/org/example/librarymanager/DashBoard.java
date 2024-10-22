package org.example.librarymanager;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class DashBoard implements Initializable {

    @FXML
    private Button In4;

    @FXML
    private Button addBooks;

    @FXML
    private Button addMembers;

    @FXML
    private Button deleteBooks;

    @FXML
    private Button home_Screen;

    @FXML
    private Button information;

    @FXML
    private Button issueBooks;

    @FXML
    private Button logOut;

    @FXML
    private Label numberStudent;

    @FXML
    private Button returnBooks;

    @FXML
    private Button searchBooks;

    @FXML
    private Button add_right;

    @FXML
    private Button issue_right;

    @FXML
    private ImageView search_Image;

    @FXML
    private TextField search_TextField;

    @FXML
    private Button setting;

    @FXML
    private TextField textField_add_AuthorName;

    @FXML
    private TextField textField_add_BookId;

    @FXML
    private TextField textField_add_BookName;

    @FXML
    private TextField textField_add_Descriptopn;

    @FXML
    private AnchorPane anchor_AddBooks;

    @FXML
    private AnchorPane anchor_FindBooks;

    @FXML
    private AnchorPane anchor_HomeScreen;

    @FXML
    private AnchorPane anchor_IssueBook;

    @FXML
    private AnchorPane anchor_deleteBooks;

    @FXML
    private AnchorPane anchor_ReturnBooks;

    @FXML
    private AnchorPane anchor_AddMemebers;

    @FXML
    private AnchorPane mess_Success;

    private AnchorPane currentPane = null;




    private double x = 0;
    private double y = 0;

    public void logout(javafx.event.ActionEvent actionEvent) {
        try {
            if (actionEvent.getSource() == logOut) {

                Parent root1 = logOut.getParent();

                FadeTransition fadeIn2 = new FadeTransition(Duration.millis(1000), root1);
                fadeIn2.setFromValue(1.0);
                fadeIn2.setToValue(0.0);

                fadeIn2.setOnFinished(event -> {
                    logOut.getScene().getWindow().hide();

                    try {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);

                        root.setOnMousePressed((MouseEvent mouseEvent) -> {
                            x = mouseEvent.getSceneX();
                            y = mouseEvent.getSceneY();
                        });

                        root.setOnMouseDragged((MouseEvent mouseEvent) -> {
                            stage.setX(mouseEvent.getScreenX() - x);
                            stage.setY(mouseEvent.getScreenY() - y);
                        });

                        FadeTransition fadeIn = new FadeTransition(Duration.millis(3000), root);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);

                        stage.initStyle(StageStyle.TRANSPARENT);
                        stage.setScene(scene);
                        stage.show();
                        fadeIn.play();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                fadeIn2.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchPain(AnchorPane nextPane) {

        FadeTransition fade = new FadeTransition(Duration.millis(500), currentPane);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(event -> {
            currentPane.setVisible(false);
            nextPane.setVisible(true);

            FadeTransition fade2 = new FadeTransition(Duration.millis(1000), nextPane);
            fade2.setFromValue(0.0);
            fade2.setToValue(1.0);
            fade2.play();

            currentPane = nextPane;
        });
        fade.play();
    }

    public void mess_Success() {
        FadeTransition fade = new FadeTransition(Duration.millis(500), mess_Success);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setOnFinished(event -> {
            mess_Success.setVisible(true);
        });
        fade.play();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), mess_Success);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(event -> {
            mess_Success.setVisible(false);
        });

        fade.setOnFinished(event -> pause.play());
        pause.setOnFinished(event -> fadeOut.play());
    }

    public void addBooks() {
        switchPain(anchor_AddBooks);
    }

    public void findBooks() {
        switchPain(anchor_FindBooks);
    }

    public void issueBooks() {
        switchPain(anchor_IssueBook);
    }

    public void deleteBooks() {
        switchPain(anchor_deleteBooks);
    }

    public void returnBooks() {
        switchPain(anchor_ReturnBooks);
    }

    public void addMembers() {
        switchPain(anchor_AddMemebers);
    }

    public void backHome() {
        switchPain(anchor_HomeScreen);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPane = anchor_HomeScreen;
    }
}
