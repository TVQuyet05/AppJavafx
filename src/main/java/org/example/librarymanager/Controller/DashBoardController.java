package org.example.librarymanager.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.librarymanager.Model.Book;
import org.example.librarymanager.Model.BorrowedBook;
import org.example.librarymanager.Model.Student;
import org.example.librarymanager.Service.LibraryDatabase;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TextField textField_searchStudent;

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

    @FXML
    private TableView<BorrowedBook> borrowedBookManager_TableView;

    @FXML
    private TableColumn<?, ?> col_stdNumber_mng;

    @FXML
    private TableColumn<?, ?> col_stdName_mng;

    @FXML
    private TableColumn<?, ?> col_bookTitle_mng;

    @FXML
    private TableColumn<?, ?> col_genre_mng;

    @FXML
    private TableColumn<?, ?> col_borrowedDate_mng;

    @FXML
    private TableColumn<?, ?> col_dueDate_mng;

    @FXML
    private TableColumn<?, ?> col_returnDate_mng;

    @FXML
    private Button btn_showUnreturnedBook;

    @FXML
    private Button btn_searchStudent;

    @FXML
    private  Label totalBookVal;

    @FXML
    private Label totalMemberVal;

    @FXML
    private Label totalBorrowedBookVal;

    @FXML
    private PieChart genrePieChart;

    @FXML
    private VBox chartContainer;

    private double x = 0;
    private double y = 0;

    private void setGenrePieChart() {
        // Query để lấy tổng số lượng sách theo thể loại, sắp xếp giảm dần
        String sql = "SELECT genre, SUM(quantity) AS total_quantity FROM book GROUP BY genre ORDER BY total_quantity DESC";
        Connection connection = LibraryDatabase.getInstance().getConnection();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            int totalQuantity = 0;
            int count = 0;
            int otherQuantity = 0;

            // Tính tổng số lượng (quantity) toàn bộ
            while (resultSet.next()) {
                totalQuantity += resultSet.getInt("total_quantity");
            }

            // Đặt lại con trỏ ResultSet
            resultSet.beforeFirst();

            // Duyệt qua từng thể loại
            while (resultSet.next()) {
                String genre = resultSet.getString("genre");
                int genreQuantity = resultSet.getInt("total_quantity");

                if (count < 5) {
                    // Tính phần trăm cho 5 thể loại đầu tiên
                    double percentage = (double) genreQuantity / totalQuantity * 100;

                    // Thêm dữ liệu vào PieChart
                    pieChartData.add(new PieChart.Data(genre + " (" + String.format("%.1f", percentage) + "%)", genreQuantity));

                    count++;
                } else {
                    // Cộng dồn các thể loại còn lại vào "Other"
                    otherQuantity += genreQuantity;
                }
            }

            // Thêm dữ liệu mục "Other" nếu có
            if (otherQuantity > 0) {
                double otherPercentage = (double) otherQuantity / totalQuantity * 100;
                pieChartData.add(new PieChart.Data("Other (" + String.format("%.1f", otherPercentage) + "%)", otherQuantity));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Cập nhật PieChart với dữ liệu mới
        genrePieChart.setData(pieChartData);

        // Xóa các thành phần cũ (PieChart và nhãn)
        chartContainer.getChildren().clear();

        // Thêm PieChart vào VBox
        chartContainer.getChildren().add(genrePieChart);
    }

    public void showNumberOfBorrowedBook() {
        LibraryDatabase database = LibraryDatabase.getInstance();
        int totalBorrowedBooks = database.getNumberOfBorrowedBook(); // Lấy số lượng sách mượn

        totalBorrowedBookVal.setText(String.valueOf(totalBorrowedBooks)); // Hiển thị lên Label
    }


    public void showTotalBooks() {
        LibraryDatabase database = LibraryDatabase.getInstance();
        int totalBooks = database.getTotalBooks(); // Lấy tổng số sách từ database

        totalBookVal.setText(String.valueOf(totalBooks)); // Hiển thị lên Label
    }

    public void showNumberOfMembers() {
        LibraryDatabase database = LibraryDatabase.getInstance();
        int totalMembers = database.getNumberOfMembers(); // Lấy số lượng thành viên

        totalMemberVal.setText(String.valueOf(totalMembers)); // Hiển thị lên Label
    }

    public void showHomeScreenManager(){
        showTotalBooks();
        showNumberOfBorrowedBook();
        showNumberOfMembers();
        setGenrePieChart();
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
        textField_add_Quantity.setText("0");
        textField_add_ImageBook.setText(bookFromAPI.getImage());

        System.out.println("Set info of book from API successfully!");

    }

    public void addBookToDatabase() {
        LibraryDatabase database = LibraryDatabase.getInstance();
        Connection connect = database.getConnection();

        String isbn = textField_add_ISBN.getText();
        String book_title = textField_add_BookName.getText();
        String author = textField_add_AuthorName.getText();
        String genre = textField_add_Genre.getText();
        String date_string = textField_add_Date.getText();
        String description = textField_add_Description.getText();

        String text_quantity = textField_add_Quantity.getText();
        int quantity = 0;

        if(!text_quantity.matches("\\d+")) {
            //Please fill number
            return;
        }

        quantity = Integer.parseInt(text_quantity);

        if(quantity <= 0) return; //Please fill quantity


        String path_image = textField_add_ImageBook.getText();

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
        if (SignUpAccount_TableView.getColumns().stream().noneMatch(column -> column.getText().equals("Action"))) {
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
                        getTableView().getSelectionModel().select(getIndex());
                        Student student = getTableView().getItems().get(getIndex());
                        if (student != null) {
                            acceptSignUp(student);
                        }
                    });

                    refuseButton.setOnAction(event -> {
                        getTableView().getSelectionModel().select(getIndex());
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

        showMemberInformation();

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

        // Wrap the original list in a FilteredList
        FilteredList<Student> filteredList = new FilteredList<>(listAccStd, student -> true);

        // Set the filtered list as the items for the TableView
        Member_Information_TV.setItems(filteredList);

        // Add a listener to the search button
        btn_searchStudent.setOnAction(event -> {
            String num_search = textField_searchStudent.getText();

            // Update the filter predicate based on the search term
            filteredList.setPredicate(student -> {
                if (num_search == null || num_search.isEmpty()) {
                    return true; // Show all students if the search term is empty
                }
                return student.getStudentNumber().equals(num_search);
            });
        });

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

    public void showBorrowedBookForManager() {
        LibraryDatabase database = LibraryDatabase.getInstance();

        ObservableList<BorrowedBook> listBorrowedBook = database.getBorrowedBook();

        btn_showUnreturnedBook.setOnAction(event -> {
            listBorrowedBook.removeIf(borrowedBook ->
                    borrowedBook.getReturn_date() != null);
        });

        col_stdNumber_mng.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        col_stdName_mng.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        col_bookTitle_mng.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_genre_mng.setCellValueFactory(new PropertyValueFactory<>("genre"));
        col_borrowedDate_mng.setCellValueFactory(new PropertyValueFactory<>("borrow_date"));
        col_dueDate_mng.setCellValueFactory(new PropertyValueFactory<>("due_date"));
        col_returnDate_mng.setCellValueFactory(new PropertyValueFactory<>("return_date"));

        borrowedBookManager_TableView.setItems(listBorrowedBook);
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

        showHomeScreenManager();

        showMemberInformation();

        showSignUpAccount();

        showBorrowedBookForManager();
    }
}