package com.example.courseregistrationsystem.model;

/**
 * Course entity — from the UML class diagram.
 *
 * OOP Concepts:
 * - INHERITANCE: Extends BaseEntity
 * - ENCAPSULATION: Private fields
 * - POLYMORPHISM: Overrides toFileString() and getDisplayName()
 *
 * File format:
 *   id|courseId|name|code|credits|instructor|capacity|createdAt|updatedAt
 */
public class Course extends BaseEntity {

    private String courseId;
    private String name;
    private String code;
    private int    credits;
    private String instructor;
    private int    capacity;

    // ─── Constructors ───────────────────────────────────────────────────────────

    public Course() { super(); }

    public Course(String id, String courseId, String name, String code,
                  int credits, String instructor, int capacity) {
        super(id);
        this.courseId   = courseId;
        this.name       = name;
        this.code       = code;
        this.credits    = credits;
        this.instructor = instructor;
        this.capacity   = capacity;
    }

    // ─── Polymorphism ───────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        return String.join("|",
                getId(),
                courseId,
                name,
                code,
                String.valueOf(credits),
                instructor,
                String.valueOf(capacity),
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    @Override
    public String getDisplayName() {
        return name + " (" + code + ") — " + credits + " credits";
    }

    public static Course fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 7) throw new IllegalArgumentException("Invalid course record: " + line);

        Course c = new Course();
        c.setId(parts[0]);
        c.setCourseId(parts[1]);
        c.setName(parts[2]);
        c.setCode(parts[3]);
        c.setCredits(Integer.parseInt(parts[4]));
        c.setInstructor(parts[5]);
        c.setCapacity(Integer.parseInt(parts[6]));
        if (parts.length > 7) c.setCreatedAt(parts[7]);
        if (parts.length > 8) c.setUpdatedAt(parts[8]);
        return c;
    }

    // ─── getCourseDetails() — from UML class diagram ────────────────────────────

    public String getCourseDetails() {
        return String.format("Course: %s | Code: %s | Credits: %d | Instructor: %s | Capacity: %d",
                name, code, credits, instructor, capacity);
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public String getCourseId()                    { return courseId; }
    public void   setCourseId(String courseId)     { this.courseId = courseId; }

    public String getName()                        { return name; }
    public void   setName(String name)             { this.name = name; }

    public String getCode()                        { return code; }
    public void   setCode(String code)             { this.code = code; }

    public int    getCredits()                     { return credits; }
    public void   setCredits(int credits)          { this.credits = credits; }

    public String getInstructor()                  { return instructor; }
    public void   setInstructor(String instructor) { this.instructor = instructor; }

    public int    getCapacity()                    { return capacity; }
    public void   setCapacity(int capacity)        { this.capacity = capacity; }
}
