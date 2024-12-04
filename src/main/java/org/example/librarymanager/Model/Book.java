package org.example.librarymanager.Model;

import java.sql.Date;
import java.time.LocalDate;

public class Book {

    private String id;
    private String title;
    private String author;
    private String genre;
    private String date;
    private String description;
    private int quantity;
    private String image;
    private double avgJudge;
    public Book(String id, String title, String author, String genre, String date, String description, int quantity, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.date = date;
        this.description = description;
        this.quantity = quantity;
        this.image = image;
    }

    public Book(String id, String title, String author, String genre, String date, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.image = image;
        this.date = date;
    }

    public Book() {

    }

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Book(String id, String title, String image, double avgJudge) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.avgJudge = avgJudge;
    }
    public String getId() {return this.id;}

    public String getTitle() {return this.title;}

    public String getAuthor() {return this.author;}

    public String getGenre() {return this.genre;}

    public String getImage() {return this.image;}

    public String getDate() {return this.date;}

    public String getDescription() {return this.description;}

    public int getQuantity() {return this.quantity;}
    public double getAvgJudge() { return avgJudge; }

}
