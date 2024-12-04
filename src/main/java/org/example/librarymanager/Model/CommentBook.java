package org.example.librarymanager.Model;

public class CommentBook extends  Book{

    private int judge;
    private String comment;
    private String studentNumber;
    private String studentName;

    public CommentBook(String id, String title, String author, String genre, String date,
                       String description, int quantity, String image, String comment, Integer judge) {
        super(id, title, author, genre, date, description, quantity, image);  // Gọi constructor của lớp cha Book
        this.comment = comment;
        this.judge = judge != null ? judge : 0;  // Nếu judge null thì mặc định là 0
    }

    public CommentBook(String id, String title, String author, String comment, int judge) {
        super(id, title, author);
        this.comment =comment;
        this.judge = judge;
    }

    public CommentBook(String studentName, String comment, int judge) {
        this.studentName = studentName;
        this.comment = comment;
        this.judge = judge;
    }

    public int getJudge(){
        return this.judge;
    }
    public String getComment(){
        return  this.comment;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }
}
