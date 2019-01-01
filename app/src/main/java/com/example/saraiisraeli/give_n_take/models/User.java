package com.example.saraiisraeli.give_n_take.models;

public class User {

    private String name;
    private String phoneNumber;
    private String city;
    private String token;

    public User(){ }

    public  User (String i_name, String i_phoneNumber, String i_city, String i_token)
    {
        this.city = i_city;
        this.name = i_name;
        this.phoneNumber = i_phoneNumber;
        this.token = i_token;
    }

    public  User (String i_name, String i_phoneNumber, String i_city)
    {
        this.city = i_city;
        this.name = i_name;
        this.phoneNumber = i_phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
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

    public void setCity(String city) {
        this.city = city;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
