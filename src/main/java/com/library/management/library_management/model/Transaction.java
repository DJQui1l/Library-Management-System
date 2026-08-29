package com.library.management.library_management.model;

public class Transaction {
    private int id;
    private int member_id;
    private String borrow_date;
    private String return_date;

    public Transaction(){

    }
    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public int getMember_id(){return member_id;}
    public void setMember_id(int member_id){this.member_id = member_id;}

    public String getBorrow_date(){return borrow_date;}
    public void setBorrow_date(String borrow_date){this.borrow_date = borrow_date;}

    public String getReturn_date(){return return_date;}
    public void setReturn_date(String return_date){this.return_date = return_date;}



   

}
