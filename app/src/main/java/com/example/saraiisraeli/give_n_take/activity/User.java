package com.example.saraiisraeli.give_n_take.activity;

public class User
{

    public String firstName;
    public String lastName;
    public String userName;
    public int score;

    public User() {}

    public User(String userName , String firstName , String lastName,int score)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.score=score;
    }
    public User(int score)
    {
        this.score=score;
    }


    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getUserName()
    {
        return userName;
    }

    public int getScore(){return score;}

    public void setScore(int score)
    {
        this.score+=score;
    }
}