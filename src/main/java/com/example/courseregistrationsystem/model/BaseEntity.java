package com.example.courseregistrationsystem.model;

/**
 * Abstract base class for all entities in the system.
 *
 * OOP Concepts Applied:
 * - ABSTRACTION: Defines common structure and contract for all entities
 * - ENCAPSULATION: Private fields with getters/setters
 * - INHERITANCE: All entities extend this class
 */
public abstract class BaseEntity {

    private String id;
    private String createdAt;
    private String updatedAt;

    // ─── Constructors ───────────────────────────────────────────────────────────

    public BaseEntity() {}

    public BaseEntity(String id) {
        this.id = id;
        this.createdAt = java.time.LocalDateTime.now().toString();
        this.updatedAt = java.time.LocalDateTime.now().toString();
    }

    // ─── Abstract Methods (Polymorphism via method overriding) ──────────────────

    /**
     * Serialize the entity to a pipe-delimited string for file storage.
     * Each subclass MUST implement its own serialization format.
     */
    public abstract String toFileString();

    /**
     * Returns a human-readable display string for the entity.
     */
    public abstract String getDisplayName();

    // ─── Common Method (Encapsulation) ──────────────────────────────────────────

    public void updateTimestamp() {
        this.updatedAt = java.time.LocalDateTime.now().toString();
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
