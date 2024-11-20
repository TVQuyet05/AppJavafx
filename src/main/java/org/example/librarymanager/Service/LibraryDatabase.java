package org.example.librarymanager.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.librarymanager.Model.Book;
import org.example.librarymanager.Model.Student;

import java.sql.*;

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
        if(instance == null) {
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

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(maxIdQuery)) {

            // Retrieve the maximum book_id
            if (rs.next()) {
                int maxBookId = rs.getInt(1); // Get the result from the first column as an int

                // Increment the maximum book_id
                newBookId = maxBookId + 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return; // Exit if max ID retrieval fails
        }


        String query = "INSERT INTO book (book_title, author, genre, date, image, book_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setDate(4, new java.sql.Date(book.getDate().getTime()));
            stmt.setString(5, book.getImage());
            stmt.setInt(6, newBookId);

            // Execute the update
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

}
