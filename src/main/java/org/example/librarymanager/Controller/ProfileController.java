package org.example.librarymanager.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.librarymanager.Model.Student;
import org.example.librarymanager.Service.LibraryDatabase;

import java.net.URL;
import java.util.ResourceBundle;

import static org.example.librarymanager.Util.getData.nameOfUser;
import static org.example.librarymanager.Util.getData.numberOfUser;


public class ProfileController implements Initializable {
    @FXML
    private Button close;

    @FXML
    private Button minimize;
    @FXML
    private TextField fullNameProfile;
    @FXML
    private TextField classProfile;
    @FXML
    private TextField studentNumberProfile;
    @FXML
    private TextField passwordProfile;
    @FXML
    private Button updateProfileButton;

    @FXML
    private void onUpdateProfileButtonClick() {
        // call update function
        boolean isUpdated = updateProfile();

        // Hiện thông báo nếu cập nhật thành công
        if (isUpdated) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Success");
            alert.setHeaderText(null); // dont have sub title
            alert.setContentText("Information updated successfully!");
            alert.showAndWait(); //show alert
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update information. Please try again.");
            alert.showAndWait();
        }
    }

    // Phương thức cập nhật dữ liệu vào cơ sở dữ liệu
    private boolean updateProfile() {
        String studentNumber = studentNumberProfile.getText();
        String password = passwordProfile.getText();
        String fullName = fullNameProfile.getText();
        String className = classProfile.getText();

        // Gọi hàm cập nhật trong lớp LibraryDatabase
        return LibraryDatabase.getInstance().updateStudentProfileController(studentNumber, password, fullName, className);
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
        studentNumberProfile.setText(numberOfUser);
        fullNameProfile.setText(nameOfUser);
        LibraryDatabase database = LibraryDatabase.getInstance();
        Student student = database.getStudentByStudentNumber(numberOfUser);
        passwordProfile.setText(student.getPassword());
        classProfile.setText(student.get_class());
    }
}
