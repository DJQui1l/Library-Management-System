package com.library.management.library_management.model;

import jakarta.persistence.*;


@Entity
@Table(name = "PUBLISHERS")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")

    private String address;

    @Column(name = "phone")
    private String phone;

    public Publisher(){
    }

    public Publisher(String name, String address, String phone){
        this.name = name;
        this.address = address;
        this.phone = phone;

    }
    public int getId(){return id;}
    public void setId(int id) {this.id = id;}

    public String getName(){ return name;}
    public void setName(String name){this.name = name;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
}
