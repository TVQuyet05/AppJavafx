package org.example.librarymanager.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.librarymanager.Model.Book;
import org.example.librarymanager.Model.BorrowedBook;
import org.example.librarymanager.Model.CommentBook;
import org.example.librarymanager.Model.Student;

import java.sql.*;
import java.time.LocalDate;
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
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Re-establishing database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    public List<Object[]> setDataTopBorrowTable() {
        List<Object[]> topBorrowedBooks = new ArrayList<>();
        String query = """
        SELECT book.book_title, book.author, COUNT(borrowbook.book_id) AS borrow_count
        FROM book
        JOIN borrowbook ON book.book_id = borrowbook.book_id
        GROUP BY book.book_title, book.author
        ORDER BY borrow_count DESC
        LIMIT 5
    """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            int rank = 1; // Bắt đầu từ thứ hạng 1
            while (rs.next()) {
                Object[] row = { rank++, rs.getString("book_title"), rs.getString("author"), rs.getInt("borrow_count") };
                topBorrowedBooks.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topBorrowedBooks;
    }

    public boolean saveBook(String studentNumber, String isbn) {
        // Kiểm tra xem sinh viên đã lưu sách này chưa
        String checkExistSQL = """
    SELECT COUNT(*) FROM savebook 
    WHERE studentNumber = ? AND book_id = ?
    """;

        // Chèn thông tin lưu sách nếu sinh viên chưa lưu sách này
        String insertSaveSQL = """
    INSERT INTO savebook (studentNumber, book_id) 
    VALUES (?, ?)
    """;

        try (Connection conn = getConnection()) {
            // Kiểm tra xem sinh viên đã lưu cuốn sách này chưa
            try (PreparedStatement checkStmt = conn.prepareStatement(checkExistSQL)) {
                checkStmt.setString(1, studentNumber);
                checkStmt.setString(2, isbn); // Kiểm tra theo studentNumber và book_id

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {

                        return false;
                    }
                }
            }

            // Nếu sinh viên chưa lưu sách này, tiến hành lưu sách mới
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSaveSQL)) {
                insertStmt.setString(1, studentNumber);
                insertStmt.setString(2, isbn);

                int rowsInserted = insertStmt.executeUpdate();
                return rowsInserted > 0; // Nếu chèn thành công, trả về true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Nếu có lỗi xảy ra, trả về false
    }




    public boolean borrowBook(String studentNumber, String isbn) {
        String insertBorrowSQL = """
    INSERT INTO borrowbook (studentNumber, book_id, borrow_date, due_date, return_date) 
    VALUES (?, ?, ?, ?, NULL)  
    """;

        String updateQuantitySQL = """
    UPDATE book 
    SET quantity = quantity - 1 
    WHERE book_id = ? AND quantity > 0
    """;

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            try (PreparedStatement insertStmt = conn.prepareStatement(insertBorrowSQL);
                 PreparedStatement updateStmt = conn.prepareStatement(updateQuantitySQL)) {

                // Tính toán ngày hiện tại và ngày hết hạn
                LocalDate borrowDate = LocalDate.now();
                LocalDate dueDate = borrowDate.plusDays(14);

                // Chèn thông tin mượn sách
                insertStmt.setString(1, studentNumber);
                insertStmt.setString(2, isbn); // Sử dụng isbn trực tiếp làm book_id
                insertStmt.setDate(3, Date.valueOf(borrowDate));
                insertStmt.setDate(4, Date.valueOf(dueDate));

                // Thực thi giảm số lượng sách
                updateStmt.setString(1, isbn);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    int rowsInserted = insertStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        conn.commit(); // Nếu cả hai hành động thành công, commit giao dịch
                        return true;  // Mượn sách thành công
                    }
                }

                conn.rollback(); // Nếu có lỗi xảy ra, rollback giao dịch
            } catch (SQLException e) {
                conn.rollback(); // Rollback nếu có bất kỳ lỗi nào xảy ra trong block try
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Mượn sách thất bại
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

    public List<Object[]> getTopFavBook(){
        List<Object[]> topFavBooks = new ArrayList<>();
        String query = "SELECT book_id, book_title, author, date\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        savebook.book_id, \n" +
                "        COUNT(savebook.book_id) AS tong, \n" +
                "        book.book_title, \n" +
                "        book.author, \n" +
                "        book.date\n" +
                "    FROM savebook \n" +
                "    JOIN book \n" +
                "    ON savebook.book_id = book.book_id \n" +
                "    GROUP BY savebook.book_id \n" +
                "    ORDER BY tong DESC \n" +
                "    LIMIT 5\n" +
                ") AS subquery;";
        try(Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery()){
            while (rs.next()){
                String id = rs.getString("book_id");
                String title = rs.getString("book_title");
                String author = rs.getString("author");
                String date = rs.getString("date");
                topFavBooks.add(new Object[]{id,title,author,date});
            }
        }
        catch(SQLException e){
            e.printStackTrace();;
        }
        return topFavBooks;
    }
    // xử lý th nếu add trùng sách (isbn) thì cộng vào quantity.
    public void addBook(Book book) {

        String query = "INSERT INTO book (book_id, book_title, author, genre, date, description, quantity, image) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getGenre());
            stmt.setString(5, book.getDate());
            stmt.setString(6, book.getDescription());
            stmt.setInt(7, book.getQuantity());
            stmt.setString(8, book.getImage());

            // Thực thi câu lệnh
            stmt.executeUpdate();
            System.out.println("Add book to database success!");

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

    public ObservableList<Student> getAccStudent() {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        String query = "SELECT * FROM student";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Student student = new Student(
                        resultSet.getString("studentNumber"),
                        resultSet.getString("password"),
                        "",
                        resultSet.getString("name"),
                        resultSet.getString("class")
                );
                studentList.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(!studentList.isEmpty()) {
            System.out.println("Get student success!");
        }
        return studentList;
    }

    public void deleteStudent(Student student) {
        String query = "DELETE FROM student WHERE studentNumber = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getStudentNumber());
            statement.executeUpdate();

            System.out.println("Delete student success!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getString("book_id"),
                        resultSet.getString("book_title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getString("date"),
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

    public ObservableList<CommentBook> getCommentBook(String studentNumber) {
        ObservableList<CommentBook> commentBookList = FXCollections.observableArrayList();


        String query = "SELECT book.book_id AS id, book.book_title AS title, book.author AS author, " +
                "reviewbook.comment AS comment, reviewbook.judge AS judge " +
                "FROM book " +
                "JOIN reviewbook ON book.book_id = reviewbook.book_id " +
                "WHERE reviewbook.studentNumber = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentNumber);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                // Tạo đối tượng CommentBook từ dữ liệu truy vấn
                CommentBook commentBook = new CommentBook(
                        result.getString("id"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("comment"),
                        result.getInt("judge")
                );

                // Thêm đối tượng vào danh sách
                commentBookList.add(commentBook);
            }

            System.out.println("Get comment books successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting comment books", e);
        }

        return commentBookList;
    }


    public ObservableList<Book> getFavBook(String studentNumber) {
        ObservableList<Book> favBookList = FXCollections.observableArrayList();

        // Cập nhật câu lệnh SQL để lấy sách yêu thích của học sinh hiện tại
        String query = "SELECT savebook.book_id, book.book_title, book.author, book.genre, book.date " +
                "FROM savebook " +
                "JOIN book ON savebook.book_id = book.book_id " +
                "WHERE savebook.studentNumber = ?";  // Thêm điều kiện để lọc sách yêu thích của học sinh

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentNumber);  // Sử dụng studentNumber của học sinh hiện tại

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                // Tạo đối tượng Book từ dữ liệu truy vấn
                Book favBook = new Book(
                        result.getString("book_id"),
                        result.getString("book_title"),
                        result.getString("author"),
                        result.getString("genre"),
                        result.getString("date"),
                        null, // Description không có trong truy vấn
                        0,    // Quantity không có trong truy vấn
                        null  // Image không có trong truy vấn
                );

                // Thêm đối tượng vào danh sách
                favBookList.add(favBook);
            }

            System.out.println("Get favorite books successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting favorite books", e);
        }

        return favBookList;
    }

    public boolean deleteFavBook(String bookId) {
        String query = "DELETE FROM savebook WHERE book_id = ? AND studentnumber = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, bookId);
            stmt.setString(2, numberOfUser);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có dòng bị ảnh hưởng
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu xảy ra lỗi
        }
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
                        result.getString("book_id"),
                        result.getString("book_title"),
                        result.getString("author"),
                        result.getString("genre"),
                        result.getString("date"),
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
