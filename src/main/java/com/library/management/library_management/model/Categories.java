package com.library.management.library_management.model;

public class Categories {
    private int id;
    private String name;

    public Categories(String name){
        this.name = name;
    }

    public int getId() {return id; }

    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
