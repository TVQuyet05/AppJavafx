package org.example.librarymanager.Model;

import java.sql.Date;

public class BorrowedBook extends Book {

    private String studentNumber;
    private String studentName;

    private Date borrow_date;
    private Date due_date;
    private Date return_date;

    public BorrowedBook(String studentNumber, String studentName,
                        String book_id, String title, String author,
                        String genre, String date, String image,
                        Date borrow_date, Date due_date, Date return_date) {

        super(book_id, title, author, genre, date, image);

        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.borrow_date = borrow_date;
        this.due_date = due_date;
        this.return_date = return_date;

    }

    public String getStudentNumber() {return this.studentNumber;}

    public String getStudentName() {return this.studentName;}

    public Date getBorrow_date() {return this.borrow_date;}

    public Date getDue_date() {return this.due_date;}

    public Date getReturn_date() {return this.return_date;}

}
