package com.example.saraiisraeli.give_n_take.models;

public class User {

    private String name;
    private String phoneNumber;
    private String token;

    public User(){ }

    public  User (String i_name, String i_phoneNumber, String i_token)
    {
        this.name = i_name;
        this.phoneNumber = i_phoneNumber;
        this.token = i_token;
    }

    public  User (String i_name, String i_phoneNumber)
    {
        this.name = i_name;
        this.phoneNumber = i_phoneNumber;
    }

    public String getName() {
        return name;
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
}
