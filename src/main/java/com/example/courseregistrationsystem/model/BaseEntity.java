package com.example.courseregistrationsystem.model;
/**
 * OOP: ABSTRACTION - Abstract base class hiding common entity logic.
 *      ENCAPSULATION - All fields private with protected getters/setters.
 *      INHERITANCE   - All models will extend this class.
 */
public abstract class BaseEntity {

    private String id;
    private String createdAt;

    public BaseEntity() {}

    public BaseEntity(String id, String createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    // OOP: ENCAPSULATION - controlled access via getters/setters
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

    /**
     * OOP: ABSTRACTION - Abstract method forces all subclasses to define their own
     * CSV/text serialization format for File Read/Write storage.
     */
    public abstract String toFileString();
}

