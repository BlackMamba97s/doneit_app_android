package com.example.doneit.model;

public class Category {

    private Long id;
    private String name;
    private int cfuPrice;

    public Category(String name , int cfuPrice){
        this.name=name;
        this.cfuPrice = cfuPrice;
    }

    public int getCfuPrice() {
        return cfuPrice;
    }

    public void setCfuPrice(int cfuPrice) {
        this.cfuPrice = cfuPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cfuPrice=" + cfuPrice +
                '}';
    }
}
