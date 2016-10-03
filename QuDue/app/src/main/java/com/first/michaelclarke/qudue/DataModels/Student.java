package com.first.michaelclarke.qudue.DataModels;

/**
 * Created by michaelclarke on 24/08/2016.
 * Data model for the object Student.
 */
public class Student {

    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String course;

    private boolean previousLogin;

    //default constructor
    public Student() {

    }

    //constructor with args
    public Student(int id, String firstName, String lastName, String email, String password, String course,
                   boolean previousLogin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.course = course;
        this.previousLogin = previousLogin;
    }

    //Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public boolean isPreviousLogin() {
        return previousLogin;
    }

    public void setPreviousLogin(boolean previousLogin) {
        this.previousLogin = previousLogin;
    }

}