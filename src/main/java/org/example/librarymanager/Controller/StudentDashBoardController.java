package org.example.librarymanager.Controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudentDashBoardController implements Initializable {

    @FXML
    private Button CommentBooks;

    @FXML
    private Button FavouriteBooks;

    @FXML
    private Button In4;

    @FXML
    private AnchorPane anchor_HomeScreen;

    @FXML
    private Button home_Screen;

    @FXML
    private Button information;

    @FXML
    private Button logOut;

    @FXML
    private Label numberStudent;

    @FXML
    private Button returnBooks;

    @FXML
    private Button setting;

    @FXML
    private Button viewAllBooks;

    @FXML
    private AnchorPane commentBook_std;

    @FXML
    private AnchorPane favouriteBook_std;

    @FXML
    private AnchorPane homeScreen_std;

    @FXML
    private AnchorPane returnBooks_std;

    private AnchorPane currentPane = null;

    double x = 0;
    double y = 0;

    @FXML
    public void backHome(ActionEvent event) {
        switchPain(homeScreen_std);
    }


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
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/librarymanager/Login.fxml")));

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

        FadeTransition fade = new FadeTransition(Duration.millis(300), currentPane);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(event -> {
            currentPane.setVisible(false);
            nextPane.setVisible(true);

            FadeTransition fade2 = new FadeTransition(Duration.millis(800), nextPane);
            fade2.setFromValue(0.0);
            fade2.setToValue(1.0);
            fade2.play();

            currentPane = nextPane;
        });
        fade.play();
    }

    public void openViewAllBooks() {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/librarymanager/ViewAllBooks.fxml")));

            // Create a new Scene
            Scene scene = new Scene(root);

            Stage stage = new Stage();

            // Add mouse event handlers for dragging the window
            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });

            // Set the stage to be transparent
            stage.initStyle(StageStyle.TRANSPARENT);

            // Set the scene and display the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading ViewAllBooks.fxml");
        }
    }

    public void returnBook() {
        switchPain(returnBooks_std);
    }

    public void commentBook() {
        switchPain(commentBook_std);
    }

    public void favouriteBook() {
        switchPain(favouriteBook_std);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPane = homeScreen_std;
    }
}
