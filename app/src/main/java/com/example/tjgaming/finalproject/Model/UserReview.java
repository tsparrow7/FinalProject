package com.example.tjgaming.finalproject.Model;

/**
 * Created by TJ on 11/10/2018.
 */
public class UserReview {

    private String author;
    private String show_name;
    private String user_review;

    public UserReview() {

    }

    public UserReview(String author, String show_name, String user_review) {
        this.author = author;
        this.show_name = show_name;
        this.user_review = user_review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getUser_review() {
        return user_review;
    }

    public void setUser_review(String user_review) {
        this.user_review = user_review;
    }
}
