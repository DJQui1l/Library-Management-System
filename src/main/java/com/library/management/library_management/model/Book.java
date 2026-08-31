package com.library.management.library_management.model;

import jakarta.persistence.*;
/*
This is a book.
*/

@Entity
@Table(name = "BOOKS")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "category")
    private String category;

    @Column(name = "author")
    private String author;

    @Column(name = "author_id", nullable = true)
    private Integer author_id;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publisher_id",nullable = true)
    private Integer publisher_id;



    public Book (){
    }
    public Book(String book_name, String category, String author, Integer author_id, String publisher, Integer publisher_id) {
        this.title = book_name;
        this.category = category;
        this.author = author;
        this.author_id = author_id;
        this.publisher = publisher;
        this.publisher_id = publisher_id;

    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

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
