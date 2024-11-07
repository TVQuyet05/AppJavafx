package org.example.librarymanager.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    @FXML
    private Button close;

    @FXML
    private Hyperlink forgotPassword;

    @FXML
    private Button login;

    @FXML
    private Button loginwithFace;

    @FXML
    private Button minimize;

    @FXML
    private PasswordField passWord;

    @FXML
    private Button register;

    @FXML
    private TextField register_Cfpass;

    @FXML
    private TextField register_Email;

    @FXML
    private Hyperlink register_SignIn;

    @FXML
    private TextField register_Users;

    @FXML
    private TextField register_pass;

    @FXML
    private Button signUp;

    @FXML
    private TextField studentNumber;

    @FXML
    private AnchorPane signUp_Anchor;

    @FXML
    private AnchorPane signIn_Anchor;

    @FXML
    private AnchorPane forgot_Anchor;

    @FXML
    private Button forgotpass_submit;

    private double x = 0;
    private double y = 0;

    private void addFadeOutEffect(Node node, Runnable onFinished) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(400));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> onFinished.run());
        fadeTransition.play();
    }

    public void switchPain(AnchorPane a, AnchorPane b) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), a);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(event -> {
            a.setVisible(false);
            b.setVisible(true);
            FadeTransition fade2 = new FadeTransition(Duration.millis(1000), b);
            fade2.setFromValue(0.0);
            fade2.setToValue(1.0);
            fade2.play();
        });
        fade.play();
    }

    public void close() {
        Stage stage = (Stage) close.getScene().getWindow();
        Parent root = stage.getScene().getRoot();
        addFadeOutEffect(root, () -> System.exit(0));
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

    public void login() throws IOException {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Admin Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Login!");
            alert.showAndWait();

            Parent root1 = login.getParent().getParent();

            FadeTransition fadeIn2 = new FadeTransition(Duration.millis(1000), root1);
            fadeIn2.setFromValue(1.0);
            fadeIn2.setToValue(0.0);

            fadeIn2.setOnFinished(event -> {
                login.getScene().getWindow().hide();

                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/librarymanager/DashBoard.fxml")));


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

                    FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), root);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void signUp() {
        switchPain(signIn_Anchor, signUp_Anchor);
    }

    public void register() {
        switchPain(signUp_Anchor, signIn_Anchor);
    }

    public void register_signIn() {
        switchPain(signUp_Anchor, signIn_Anchor);
    }

    public void forgotPassword() {
        switchPain(signIn_Anchor, forgot_Anchor);
    }
    public void forgotPassword_SignIn(){
        switchPain(forgot_Anchor, signIn_Anchor);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
