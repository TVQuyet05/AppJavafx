package org.example.librarymanager.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class ProfileController implements Initializable {
    @FXML
    private Button close;

    @FXML
    private Button minimize;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
