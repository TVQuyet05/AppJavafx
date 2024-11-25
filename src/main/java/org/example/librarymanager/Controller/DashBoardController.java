package org.example.librarymanager.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.librarymanager.Model.Book;
import org.example.librarymanager.Model.Student;
import org.example.librarymanager.Service.LibraryDatabase;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.example.librarymanager.Util.getData.nameOfUser;

public class DashBoardController implements Initializable {

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
    private Button borrowedBook;

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
    private TextField textField_add_ImageBook;

    @FXML
    private TextField textField_add_BookName;

    @FXML
    private TextField textField_add_Genre;

    @FXML
    private TextField textField_add_Date;

    @FXML
    private TextField textField_add_ISBN;

    @FXML
    private TextField textField_add_Description;

    @FXML
    private TextField textField_add_Quantity;

    @FXML
    private Label managerName;

    @FXML
    private AnchorPane anchor_AddBooks;

    @FXML
    private AnchorPane anchor_FindBooks;

    @FXML
    private AnchorPane anchor_HomeScreen;

    @FXML
    private AnchorPane anchor_addMember;

    @FXML
    private AnchorPane anchor_deleteBooks;

    @FXML
    private AnchorPane anchor_borrowedBooks;

    @FXML
    private AnchorPane mess_Success;

    private AnchorPane currentPane = null;

    @FXML
    private AnchorPane anchor_DashBoard;

    @FXML
    private AnchorPane anchor_memberInformation;

    @FXML
    private PieChart pie_chart_1;

    @FXML
    private PieChart pie_chart_2;

    @FXML
    private TableView<Student> SignUpAccount_TableView;

    @FXML
    private TableColumn<Student, String> col_signup_studentNumber;

    @FXML
    private TableColumn<Student, String> col_signup_password;

    @FXML
    private TableColumn<Student, String> col_signup_name;

    @FXML
    private TableColumn<Student, String> col_signup_class;

    @FXML
    private TableView<Student> Member_Information_TV;

    @FXML
    private TableColumn<?, ?> col_listAcc_Stn;

    @FXML
    private TableColumn<?, ?> col_listAcc_class;

    @FXML
    private TableColumn<?, ?> col_listAcc_name;

    @FXML
    private TableColumn<?, ?> col_listAcc_pass;

    //SingleTon Pattern
//    private FXMLLoader dashBoardLoader;
//    private static DashBoardController dashBoardController;
//
//    public void initializeDashBoard() throws IOException {
//        if (dashBoardLoader == null) {
//            // Load the FXML file once
//            dashBoardLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanager/DashBoard.fxml"));
//            dashBoardLoader.load();
//            dashBoardController = dashBoardLoader.getController();
//        }
//    }
//
//    public static DashBoardController getDashBoardController() {
//        dashBoardLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanager/DashBoard.fxml"));
//        dashBoardLoader.load();
//        dashBoardController = dashBoardLoader.getController();
//        return dashBoardController;
//    }




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
        SignUpAccount_TableView.getSelectionModel().clearSelection();
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

    public void addBooks() {
        System.out.println("Go to addBook AnchorPane successfully!");

        switchPain(anchor_AddBooks);
    }

    public void findBooks() {
        switchPain(anchor_FindBooks);
    }

    public void showInfoStudent() {
        switchPain(anchor_memberInformation);
    }


    public void deleteBooks() {
        switchPain(anchor_deleteBooks);
    }

    public void borrowedBooks() {
        switchPain(anchor_borrowedBooks);
    }

    public void addMembers() {
        switchPain(anchor_addMember);
    }

    public void backHome() {
        switchPain(anchor_HomeScreen);
    }

    public void successful() {
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(15.0);
        currentPane.setEffect(gaussianBlur);
        mess_Success.setVisible(true);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));

        pauseTransition.setOnFinished(event -> {
            currentPane.setEffect(null);
            mess_Success.setVisible(false);
        });

        pauseTransition.play();
    }

    public void detailBooks() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanager/BookDetail.fxml"));
            Parent root = fxmlLoader.load();

            Stage  stage = (Stage) anchor_DashBoard.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setInfoForBook(Book bookFromAPI) {

        textField_add_ISBN.setText(bookFromAPI.getId());
        textField_add_BookName.setText(bookFromAPI.getTitle());
        textField_add_AuthorName.setText(bookFromAPI.getAuthor());
        textField_add_Genre.setText(bookFromAPI.getGenre());
        textField_add_Date.setText(bookFromAPI.getDate());
        textField_add_Description.setText(bookFromAPI.getDescription());
        textField_add_Quantity.setText("Enter quantity of books");
        textField_add_ImageBook.setText(bookFromAPI.getImage());

        System.out.println("Set info of book from API successfully!");

    }

    public void addBookToDatabase() {
        LibraryDatabase database = LibraryDatabase.getInstance();

        String isbn = textField_add_ISBN.getText();
        String book_title = textField_add_BookName.getText();
        String author = textField_add_AuthorName.getText();
        String genre = textField_add_Genre.getText();
        String date_string = textField_add_Date.getText();
        String description = textField_add_Description.getText();

        int quantity = 0;
        if(!textField_add_Quantity.getText().equals("Enter quantity of books")) {
            quantity = Integer.parseInt(textField_add_Quantity.getText());
        }

        String path_image = textField_add_ImageBook.getText();

        if(quantity == 0) return; //Please fill quantity

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate date_sql = LocalDate.parse(date_string, formatter);
//        Date sqlDate = java.sql.Date.valueOf(date_sql);

        Book newbook = new Book(isbn, book_title, author, genre, date_string, description, quantity, path_image);

        database.addBook(newbook);

    }

    public void showSignUpAccount() {
        LibraryDatabase database = LibraryDatabase.getInstance();

        ObservableList<Student> listSignUpAccount = database.getSignUpAccount();
        SignUpAccount_TableView.setItems(listSignUpAccount);

        // Đặt chiều cao cố định cho từng dòng trong TableView
        SignUpAccount_TableView.setFixedCellSize(60);
        SignUpAccount_TableView.prefHeightProperty().bind(
                Bindings.size(SignUpAccount_TableView.getItems()).multiply(SignUpAccount_TableView.getFixedCellSize()).add(30)
        );

        // Đặt giá trị cho các cột
        col_signup_studentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        col_signup_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_signup_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_signup_class.setCellValueFactory(new PropertyValueFactory<>("_class"));

        // Kiểm tra nếu cột "Action" chưa được thêm
        if (Member_Information_TV.getColumns().stream().noneMatch(column -> column.getText().equals("Action"))) {
            TableColumn<Student, Void> actionColumn = new TableColumn<>("Action");

            actionColumn.setCellFactory(column -> new TableCell<>() {
                private final Button acceptButton = new Button("Accept");
                private final Button refuseButton = new Button("Refuse");

                {
                    acceptButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-cursor: hand");
                    refuseButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-cursor: hand");
                    acceptButton.setPrefWidth(70);
                    refuseButton.setPrefWidth(70);

                    acceptButton.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());
                        if (student != null) {
                            acceptSignUp(student);
                        }
                    });

                    refuseButton.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());
                        if (student != null) {
                            refuseSignUp(student);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox buttonsBox = new HBox(10, acceptButton, refuseButton);
                        buttonsBox.setAlignment(Pos.CENTER);
                        setGraphic(buttonsBox);
                    }
                }
            });

            SignUpAccount_TableView.getColumns().add(actionColumn);
        }
    }

    public void acceptSignUp(Student selectedStudent) {
        if(selectedStudent == null) {
            return;
        }

        LibraryDatabase database = LibraryDatabase.getInstance();

        Student selected_student = SignUpAccount_TableView.getSelectionModel().getSelectedItem();

        int num = SignUpAccount_TableView.getSelectionModel().getFocusedIndex();

        if(num < 0) {
            return;
        }

        //add selected_student to table student in database
        database.addStudent(selected_student);

        //delete selected_student from table signupaccount in database
        database.deleteSignUpAccount(selected_student);

        //show table sign up account again
        showSignUpAccount();

    }

    public void refuseSignUp(Student student_selected) {
        if(student_selected == null) return;

        LibraryDatabase database = LibraryDatabase.getInstance();

        Student selected_student = SignUpAccount_TableView.getSelectionModel().getSelectedItem();

        int num = SignUpAccount_TableView.getSelectionModel().getFocusedIndex();

        if(num < 0) {
            return;
        }

        //delete selected_student from table signupaccount in database
        database.deleteSignUpAccount(selected_student);

        //show table sign up account again
        showSignUpAccount();

    }

    public void openViewAllBooks() {
        // Create a new thread to open the second stage
        Thread viewAllBooksThread = new Thread(() -> {
            try {

                // Load the FXML file
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/librarymanager/ViewAllBooks.fxml")));

                // Create a new Scene
                Scene scene = new Scene(root);

                // Use Platform.runLater to update the JavaFX UI
                Platform.runLater(() -> {
                    try {
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
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("Error displaying ViewAllBooks stage.");
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading ViewAllBooks.fxml");
            }
        });

        // Start the thread to open the second stage
        viewAllBooksThread.start();

    }


    public void showMemberInformation() {
        LibraryDatabase database = LibraryDatabase.getInstance();
        ObservableList<Student> listAccStd = database.getAccStudent();
        Member_Information_TV.setItems(listAccStd);

        // Đặt chiều cao cố định cho từng dòng trong TableView
        Member_Information_TV.setFixedCellSize(60);
        Member_Information_TV.prefHeightProperty().bind(
                Bindings.size(Member_Information_TV.getItems()).multiply(Member_Information_TV.getFixedCellSize()).add(30)
        );


        // Đặt giá trị cho các cột
        col_listAcc_Stn.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        col_listAcc_pass.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_listAcc_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_listAcc_class.setCellValueFactory(new PropertyValueFactory<>("_class"));

        // Kiểm tra nếu cột "Action" chưa được thêm
        if (Member_Information_TV.getColumns().stream().noneMatch(column -> column.getText().equals("Action"))) {
            TableColumn<Student, Void> actionColumn = new TableColumn<>("Action");

            actionColumn.setCellFactory(column -> new TableCell<>() {
                private final Button viewButton = new Button("View");
                private final Button deleteButton = new Button("Delete");

                {
                    viewButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-cursor: hand");
                    deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-cursor: hand");
                    viewButton.setPrefWidth(70);
                    deleteButton.setPrefWidth(80);

                    viewButton.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());
                        if (student != null) {
                        }
                    });

                    deleteButton.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());
                        if (student != null) {
                            deleteStudent(student);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox buttonsBox = new HBox(10, viewButton, deleteButton);
                        buttonsBox.setAlignment(Pos.CENTER);
                        setGraphic(buttonsBox);
                    }
                }
            });

            Member_Information_TV.getColumns().add(actionColumn);
        }
    }

    public void deleteStudent(Student selectedStudent) {
        if(selectedStudent == null) {
            return;
        }
        LibraryDatabase database = LibraryDatabase.getInstance();


        int num = Member_Information_TV.getSelectionModel().getFocusedIndex();

        if(num < 0) {
            return;
        }

        //delete selected_student from table signupaccount in database
        database.deleteStudent(selectedStudent);

        //show table sign up account again
        showMemberInformation();

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        try {
//            initializeDashBoard();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        SignUpAccount_TableView.getSelectionModel().clearSelection();

        currentPane = anchor_HomeScreen;

        managerName.setText(nameOfUser);

        pie_chart_1.getData().addAll(new PieChart.Data("Borrowed Books", 40), new PieChart.Data("Available Books", 60));
        pie_chart_2.getData().addAll(new PieChart.Data("Fiction", 40), new PieChart.Data("Non-Fiction", 30), new PieChart.Data("History", 20), new PieChart.Data("Science", 10));

        showSignUpAccount();
        showMemberInformation();
    }
}
