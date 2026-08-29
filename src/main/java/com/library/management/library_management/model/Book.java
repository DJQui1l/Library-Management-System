package com.library.management.library_management.model;

/*
This is a book.
*/

public class Book {
    private Integer id;
    private String title;
    private String category;

    private String author;
    private Integer author_id; // not needed for initialization

    private String publisher;
    private Integer publisher_id; // not needed for initialization



    public Book (){

    }
    public Book(String book_name, String category, String author,  String publisher) {
        this.title = book_name;
        this.category = category;
        this.author = author;
        this.publisher = publisher;

    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getBook_name() {return title;}
    public void setBook_name(String book_name) {this.title = book_name;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}

    public Integer getAuthor_id() {return author_id;}
    public void setAuthor_id(Integer author_id) {this.author_id = author_id;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public Integer getPublisher_id() {return publisher_id;}
    public void setPublisher_id(Integer publisher_id) {this.publisher_id = publisher_id;}
}
