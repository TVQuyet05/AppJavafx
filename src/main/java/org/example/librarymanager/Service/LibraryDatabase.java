package org.example.librarymanager.Service;

import org.example.librarymanager.Model.Book;

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

        int newBookId = 1; // Default to 1 if the table is empty
        String maxIdQuery = "SELECT MAX(book_id) FROM book";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(maxIdQuery)) {

            // Retrieve the maximum book_id
            if (rs.next()) {
                String maxBookIdStr = rs.getString(1); // Get the result from the first column

                // Convert the String to an int for arithmetic
                if (maxBookIdStr != null && !maxBookIdStr.isEmpty()) {
                    int maxBookId = Integer.parseInt(maxBookIdStr); // Convert String to int
                    newBookId = maxBookId + 1; // Increment
                } else {
                    newBookId = 1; // Default to 1 if no book_id exists
                }
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
            stmt.setString(6, String.valueOf(newBookId));

            // Execute the update
            stmt.executeUpdate();
            System.out.println("Add book success!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    //Example method for inserting a book
    public boolean addBook(String title, String author, String isbn) {
        String query = "INSERT INTO books (title, author, isbn) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, isbn);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
