package com.library.management.library_management.model;

import jakarta.persistence.*;

/*
This is an Author.
*/

@Entity
@Table(name = "AUTHORS")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "bio")
    private String bio;


    public Author(){
    }

    public Author(String name, String phone, String email, String address, String bio){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.bio = bio;

    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    public String getBio() {return bio;}
    public void setBio(String bio) {this.bio = bio;}

}

