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


    public String getId() {return this.id;}

    public String getTitle() {return this.title;}

    public String getAuthor() {return this.author;}

    public String getGenre() {return this.genre;}

    public String getImage() {return this.image;}

    public String getDate() {return this.date;}

    public String getDescription() {return this.description;}

    public int getQuantity() {return this.quantity;}


}
