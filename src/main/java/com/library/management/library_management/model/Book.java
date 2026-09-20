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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author authorEntity;

    @Column(name = "publisher")
    private String publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisherEntity;

    private String coverImageKey;



    public Book (){
    }
    public Book(String book_name, String category, String author, Author authorEntity, String publisher, Publisher publisherEntity) {
        this.title = book_name;
        this.category = category;
        this.author = author;
        this.authorEntity = authorEntity;
        this.publisher = publisher;
        this.publisherEntity = publisherEntity;

    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}

    public Author getAuthorEntity() {return authorEntity;}
    public void setAuthorEntity(Author authorEntity) {this.authorEntity = authorEntity;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public Publisher getPublisherEntity() {return publisherEntity;}
    public void setPublisherEntity(Publisher publisherEntity) {this.publisherEntity = publisherEntity;}

    public String getCoverImageKey() {return coverImageKey;}
    public void setCoverImageKey(String coverImageKey) {this.coverImageKey = coverImageKey;}
}
