package org.example.librarymanager.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.librarymanager.Model.Book;
import org.example.librarymanager.Model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

            // Check if there is at least one result
            return resultSet.next();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!studentList.isEmpty()) {
            System.out.println("Get sign up account success!");
        }
        return studentList;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }


}
