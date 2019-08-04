package com.example.saraiisraeli.give_n_take.models;

public class User {

    private String name;
    private String phoneNumber;
    private String token;
    private String role; // 1 = seller , 0 = buyer , 2 = both

    public User(){ }

    public  User (String i_name, String i_phoneNumber, String i_token,String i_role)
    {
        this.name = i_name;
        this.phoneNumber = i_phoneNumber;
        this.token = i_token;
        this.role = i_role;
    }

    public  User (String i_name, String i_phoneNumber,String i_role)
    {
        this.name = i_name;
        this.phoneNumber = i_phoneNumber;
        this.role = i_role;
    }


    public String getName() {
        return name;
    }
    public String getUserByToken() {
        return token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {return role;}
    public void setRole() {this.role = role;}


}
