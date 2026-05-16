package com.example.courseregistrationsystem.model;

public abstract class BaseEntity {

    protected String id;

    public abstract String getDetails();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}