package com.example.InterEaseApp;

public class Order {
    private String uid;
    private String product;
    private int price;
    private String paymentMethod;
    private String email;
    private String username;

    // No-argument constructor required for Firestore deserialization
    public Order() {}

    // Constructor with parameters
    public Order(String uid, String product, int price, String paymentMethod, String email, String username) {
        this.uid = uid;
        this.product = product;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.email = email;
        this.username = username;
    }

    // Getter and setter methods
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }
    public String toString() {
        return "Name: " + username + " \n " + "Product: " + product + " \n " +"Price: " + price + "\n" + "payment" +paymentMethod + "\n" ;
    }
}

