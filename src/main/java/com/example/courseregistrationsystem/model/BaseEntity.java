package com.example.courseregistrationsystem.model;
public abstract class BaseEntity {
    private String id;
    private String createAt;


    public BaseEntity(String id, String createdAt) {
        this.id =id;
        this.createAt=createdAt;
    }

    public abstract String getDetails();

    public abstract String toFileString();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
