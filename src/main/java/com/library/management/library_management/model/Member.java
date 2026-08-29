package com.library.management.library_management.model;

public class Member {
    int id;
    String name;

    public Member(){}

    public Member(int id, String name){
        this.id = id;
        this.name = name;

    }

    public int getId(){ return id;}
    public void setId(int id){this.id = id;}

    public String getName() { return name;}
    public void setName(String name){this.name = name;}

}
