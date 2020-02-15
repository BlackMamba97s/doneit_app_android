package com.example.doneit.convalidation;


public class Convalidation {

    public Long id;

    public String proponent; //utente che ha portato a termine il todo
    public String key;
    public Long todo; // todo convalidato

    public Convalidation() {
    }

    public Convalidation(String proponent, String key, Long todo) {
        this.proponent = proponent;
        this.key = key;
        this.todo = todo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProponent() {
        return proponent;
    }

    public void setProponent(String proponent) {
        this.proponent = proponent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getTodo() {
        return todo;
    }

    public void setTodo(Long todo) {
        this.todo = todo;
    }

    @Override
    public String toString() {
        return "Convalidation{" +
                "id=" + id +
                ", proponent=" + proponent +
                ", key='" + key + '\'' +
                ", todo=" + todo +
                '}';
    }
}
