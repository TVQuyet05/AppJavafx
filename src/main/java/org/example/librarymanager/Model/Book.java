package org.example.librarymanager.Model;

import java.sql.Date;
import java.time.LocalDate;

public class Book {

    private final String title;
    private final String author;
    private final String genre;
    private final String image;
    private final Date date;

    public Book(String title, String author, String genre, Date date, String image) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.image = image;
        this.date = date;
    }


    public String getTitle() {return this.title;}

    public String getAuthor() {return this.author;}

    public String getGenre() {return this.genre;}

    public String getImage() {return this.image;}

    public Date getDate() {return this.date;}

}
