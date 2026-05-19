package com.example.courseregistrationsystem.model;

public abstract class BaseEntity {

    private String id;
    private String createdAt;

    public BaseEntity() {}

    public BaseEntity(String id, String createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public abstract String toFileString();
}

