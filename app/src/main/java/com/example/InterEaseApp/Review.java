package com.example.InterEaseApp;

public class Review {
    private String product;
    private String review;
    private String uid;
    private String username;

    // Constructor for Firestore serialization
    public Review() {
        // Empty constructor required by Firestore
    }

    // Constructor with username and review content
    public Review(String username, String review) {
        this.username = username;
        this.review = review;
    }

    // Constructor with all fields
    public Review(String product, String review, String uid, String username) {
        this.product = product;
        this.review = review;
        this.uid = uid;
        this.username = username;
    }

    // Getter and setter methods for all fields
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
