package com.example.InterEaseApp;

public class User {

    private String id = "";
    private String name = "";
    private String password = "";
    private String email = "";

    public User() {}

    public User(String id) {
        this.id = id;
    }

    public User(String id, String password, String name, String email) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Override the toString() method to return user details in the format "name email"
    @Override
    public String toString() {
        return "Name: " + name + " \n " + "Email: " + email;
    }
}
