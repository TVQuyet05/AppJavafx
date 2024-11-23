package org.example.librarymanager.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.librarymanager.Model.Student;
import org.example.librarymanager.Service.LibraryDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    @FXML
    private Button close;

    @FXML
    private Hyperlink forgotfield_password;

    @FXML
    private Button login;

    @FXML
    private Button loginwithFace;

    @FXML
    private Button minimize;

    @FXML
    private TextField field_password;

    @FXML
    private Button register;

    @FXML
    private TextField register_Users;

    @FXML
    private TextField register_Pass;

    @FXML
    private TextField register_Name;

    @FXML
    private TextField register_Class;

    @FXML
    private Hyperlink register_SignIn;

    @FXML
    private Button signUp;

    @FXML
    private TextField field_number;

    @FXML
    private AnchorPane signUp_Anchor;

    @FXML
    private AnchorPane signIn_Anchor;

    @FXML
    private AnchorPane forgot_Anchor;

    @FXML
    private AnchorPane mess_Success;

    @FXML
    private AnchorPane mess_Falied;

    @FXML
    private AnchorPane currentPane = null;




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

            LibraryDatabase database = LibraryDatabase.getInstance();


            boolean checkStudent = database.authenticateStudent(field_number.getText(), field_password.getText());

            if (checkStudent) {
                successful();
                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
                pauseTransition.setOnFinished((pauseEvent) -> {
                    Parent root1 = login.getParent().getParent();

                    FadeTransition fadeIn2 = new FadeTransition(Duration.millis(1000), root1);
                    fadeIn2.setFromValue(1.0);
                    fadeIn2.setToValue(0.0);
                    fadeIn2.setOnFinished(event -> {
                        login.getScene().getWindow().hide();

                        try {
                            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/librarymanager/DashBoard_Student.fxml")));

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
                });
                pauseTransition.play();
            } else {

                boolean checkManager = database.authenticateManager(field_number.getText(), field_password.getText());

                if(checkManager) {
                    successful();
                    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
                    pauseTransition.setOnFinished((pauseEvent) -> {
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
                    });
                    pauseTransition.play();
                }
                else {
                    GaussianBlur gaussianBlur = new GaussianBlur();
                    gaussianBlur.setRadius(25.0);
                    currentPane.setEffect(gaussianBlur);
                    mess_Falied.setVisible(true);

                    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));

                    pauseTransition.setOnFinished(event -> {
                        currentPane.setEffect(null);
                        mess_Falied.setVisible(false);
                    });

                    pauseTransition.play();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAccount() {
        LibraryDatabase database = LibraryDatabase.getInstance();

        String student_number = register_Users.getText();
        String name = register_Name.getText();
        String field_password = register_Pass.getText();
        String _class = register_Class.getText();

        Student student = new Student(student_number, field_password, "", name, _class);

        database.addStudentSignUp(student);

        register();

    }


    public void signUp() {
        switchPain(signUp_Anchor);
    }

    public void register() {
        successful();
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
        pauseTransition.setOnFinished(event -> {
            switchPain(signIn_Anchor);
        });
        pauseTransition.play();
    }

    public void register_signIn() {
        switchPain(signIn_Anchor);
    }

    public void forgotfield_password() {
        switchPain(forgot_Anchor);
    }

    public void forgotfield_password_SignIn() {
        switchPain(signIn_Anchor);
    }

    public void successful() {
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(25.0);
        currentPane.setEffect(gaussianBlur);
        mess_Success.setVisible(true);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));

        pauseTransition.setOnFinished(event -> {
            currentPane.setEffect(null);
            mess_Success.setVisible(false);
        });

        pauseTransition.play();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        currentPane = signIn_Anchor;


    }
}
