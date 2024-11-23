package org.example.librarymanager.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.librarymanager.Model.Book;
import org.example.librarymanager.Model.BorrowedBook;
import org.example.librarymanager.Model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.librarymanager.Util.getData.*;

// Class use Singleton pattern
// Code all function to access database in here

public class LibraryDatabase {
    private static LibraryDatabase instance;

    private final String url = "jdbc:mysql://localhost/librarydatabase";
    private final String user = "root";
    private final String password = "";
    private Connection connection;

    private LibraryDatabase() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database.");
        }
    }

    public static LibraryDatabase getInstance() {
        if (instance == null) {
            instance = new LibraryDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }


    public boolean authenticateStudent(String studentNumber, String password) {
        String query = "SELECT * FROM student WHERE studentNumber = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentNumber);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                typeOfUser = "STUDENT";
                numberOfUser = resultSet.getString("studentNumber");
                nameOfUser = resultSet.getString("name");
                pathImageOfStudent = resultSet.getString("image");

                System.out.println("Student log in successfully!");

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticateManager(String managerNumber, String password) {
        String query = "SELECT * FROM manager WHERE managerNumber = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, managerNumber);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                typeOfUser = "MANAGER";
                numberOfUser = resultSet.getString("managerNumber");
                nameOfUser = resultSet.getString("name");
                pathImageOfStudent = "";

                System.out.println("Manager log in successfully!");

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addBook(Book book) {

        int newBookId = 1;
        String maxIdQuery = "SELECT MAX(book_id) FROM book";

        // Lấy giá trị lớn nhất của book_id
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(maxIdQuery)) {

            if (rs.next()) {
                int maxBookId = rs.getInt(1); // Lấy giá trị từ cột đầu tiên
                newBookId = maxBookId + 1;   // Tăng book_id lên 1
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return; // Dừng hàm nếu truy vấn max ID bị lỗi
        }

        // Câu lệnh thêm sách với cột mới: description và quantity
        String query = "INSERT INTO book (book_title, author, genre, date, image, book_id, description, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setDate(4, new java.sql.Date(book.getDate().getTime()));
            stmt.setString(5, book.getImage());
            stmt.setInt(6, newBookId);
            stmt.setString(7, book.getDescription()); // Thêm cột description
            stmt.setInt(8, book.getQuantity());       // Thêm cột quantity

            // Thực thi câu lệnh
            stmt.executeUpdate();
            System.out.println("Add book success!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addStudentSignUp(Student student) {
        String query = "INSERT INTO signupaccount (studentNumber, password, name, class) VALUES (?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getPassword());
            stmt.setString(3, student.getName());
            stmt.setString(4, student.get_class());

            stmt.executeUpdate();
            System.out.println("Add sign up account to signupaccount success!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Student> getSignUpAccount() {
        ObservableList<Student> studentList = FXCollections.observableArrayList();

        String query = "SELECT * FROM signupaccount";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Student student = new Student(
                        result.getString("studentNumber"),
                        result.getString("password"),
                        "",
                        result.getString("name"),
                        result.getString("class")
                );

                studentList.add(student);
            }

            System.out.println("Get sign up account success!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentList;
    }


    public void deleteSignUpAccount(Student student) {
        String query = "DELETE FROM signupaccount WHERE studentNumber = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getStudentNumber());

            stmt.executeUpdate();
            System.out.println("Delete sign up account from signupaccount successfully!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addStudent(Student student) {
        String query = "INSERT INTO student (studentNumber, password, name, class, image) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getPassword());
            stmt.setString(3, student.getName());
            stmt.setString(4, student.get_class());
            stmt.setString(5, "");

            stmt.executeUpdate();

            System.out.println("Add student successfully!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT book_id, book_title, author, genre, date, description, quantity, image FROM book";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("book_title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getDate("date"),
                        resultSet.getString("description"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("image"));
                books.add(book);
            }

            System.out.println("Get book successfully!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }


    public ObservableList<BorrowedBook> getBorrowedBook() {
        ObservableList<BorrowedBook> borrowedBookList = FXCollections.observableArrayList();

        String query = "SELECT s.studentNumber as studentNumber, s.name as name, b.book_id as book_id, b.book_title as book_title," +
                        " b.author as author, b.genre as genre, b.date as date, b.image as image, bb.borrow_date as borrow_date," +
                        " bb.due_date as due_date, NULLIF(bb.return_date, '0000-00-00') AS return_date " +
                        "FROM borrowbook bb " +
                        "JOIN student s ON bb.studentNumber = s.studentNumber " +
                        "JOIN book b ON bb.book_id = b.book_id " +
                        "ORDER BY s.studentNumber;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                BorrowedBook borrowedBook = new BorrowedBook(
                        result.getString("studentNumber"),
                        result.getString("name"),
                        result.getInt("book_id"),
                        result.getString("book_title"),
                        result.getString("author"),
                        result.getString("genre"),
                        result.getDate("date"),
                        result.getString("image"),
                        result.getDate("borrow_date"),
                        result.getDate("due_date"),
                        result.getDate("return_date")

                );

                borrowedBookList.add(borrowedBook);
            }

            System.out.println("Get borrowed book successfully!");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return borrowedBookList;
    }











//    public void closeConnection() {
//        try {
//            if (connection != null && !connection.isClosed()) {
//                connection.close();
//                System.out.println("Database connection closed.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
