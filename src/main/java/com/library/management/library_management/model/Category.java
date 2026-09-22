package com.library.management.library_management.model;

import jakarta.persistence.*;

@Entity
@Table(name= "Categories" )
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    public Category(String name){
        this.name = name;
    }

    public int getId() {return id; }
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
