package com.example.doneit.model;

import java.util.Date;

public class Todo {

    private Long id;
    private String title;
    private String description;
    private Date publishedDate;
    private Date expirationDate;
    private Category category;
    private User user;
    private String state ="published";

    public Todo(){

    }

    public Todo(String title, String description, Category category){
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publishedDate=" +
                ", user=" + user +
                '}';
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date endingDate) {
        this.expirationDate = endingDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
