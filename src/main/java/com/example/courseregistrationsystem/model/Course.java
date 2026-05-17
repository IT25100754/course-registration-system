package com.example.courseregistrationsystem.model;

public class Course extends BaseEntity {

    private String name;
    private String code;
    private int credits;
    private String instructor;

    //  ADD THIS (needed by CourseService)
    private int availableSeats;

    public Course() {
        super();
    }

    public Course(String id, String name, String code, int credits,
                  String instructor, String createdAt) {
        super(id, createdAt);
        this.name = name;
        this.code = code;
        this.credits = credits;
        this.instructor = instructor;
    }

    // ===== EXISTING GETTERS =====

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    // ===== ADD COMPATIBILITY METHODS (FOR SERVICE) =====

    public String getCourseID() {
        return getId();
    }

    public void setCourseID(String id) {
        setId(id);
    }

    public String getCourseName() {
        return name;
    }

    // ===== SEAT MANAGEMENT (REQUIRED BY SERVICE) =====

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public boolean hasAvailableSeats() {
        return availableSeats > 0;
    }

    public void decreaseSeat() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    public void increaseSeat() {
        availableSeats++;
    }

    // ===== FILE STORAGE =====

    @Override
    public String toFileString() {
        return getId() + "|" + name + "|" + code + "|" + credits + "|" +
                instructor + "|" + availableSeats + "|" + getCreatedAt();
    }

    public static Course fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 7) return null;

        Course c = new Course(
                p[0], p[1], p[2],
                Integer.parseInt(p[3]),
                p[4],
                p[6]
        );

        c.setAvailableSeats(Integer.parseInt(p[5]));
        return c;
    }
}