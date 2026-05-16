package com.example.courseregistrationsystem.model;

/**
 * OOP: INHERITANCE - extends BaseEntity (gets id + createdAt for free).
 *      POLYMORPHISM - provides its own toFileString() implementation.
 *      ENCAPSULATION - all fields private.
 */
public class Course extends BaseEntity {

    private String name;
    private String code;
    private int    credits;
    private String instructor;

    public Course() { super(); }

    public Course(String id, String name, String code, int credits,
                  String instructor, String createdAt) {
        super(id, createdAt);
        this.name       = name;
        this.code       = code;
        this.credits    = credits;
        this.instructor = instructor;
    }

    public String getName()                        { return name; }
    public void   setName(String name)             { this.name = name; }
    public String getCode()                        { return code; }
    public void   setCode(String code)             { this.code = code; }
    public int    getCredits()                     { return credits; }
    public void   setCredits(int credits)          { this.credits = credits; }
    public String getInstructor()                  { return instructor; }
    public void   setInstructor(String instructor) { this.instructor = instructor; }

    /** OOP: POLYMORPHISM - overrides abstract method from BaseEntity. */
    @Override
    public String toFileString() {
        return getId() + "|" + name + "|" + code + "|" + credits + "|" + instructor + "|" + getCreatedAt();
    }

    public static Course fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 6) return null;
        return new Course(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4], p[5]);
    }
}
