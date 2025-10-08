package com.library.management.library_management.model;

/*
This is a book.
*/
public class Book {
    private int id;
    private String title;
    private String category;
    private String author;
    private String publisher;

    public Book(String book_name, String category, String author, String publisher){
        this.title = book_name;
        this.category = category;
        this.author = author;
        this.publisher = publisher;

    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getBook_name() {return title;}
    public void setBook_name(String book_name) {this.title = book_name;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}
}
