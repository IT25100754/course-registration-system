package com.example.courseregistrationsystem.model;


public class Course extends BaseEntity {

    // ---- ENCAPSULATION: private fields ----
    private String courseID;
    private String courseName;
    private int    credits;
    private int    availableSeats;

    // ---- Constructors ----

    public Course() {}

    public Course(String courseID, String courseName, int credits, int availableSeats) {
        this.courseID        = courseID;
        this.courseName      = courseName;
        this.credits         = credits;
        this.availableSeats  = availableSeats;
        this.id              = courseID; // BaseEntity.id
    }



    public String getCourseDetails() {
        return String.format("Course[ID=%s | Name=%s | Credits=%d | Seats=%d]",
                courseID, courseName, credits, availableSeats);
    }


    public boolean hasAvailableSeats() {
        return availableSeats > 0;
    }

    public void decreaseSeat() {
        if (availableSeats > 0) availableSeats--;
    }


    public void increaseSeat() {
        availableSeats++;
    }

    @Override
    public String getDetails() {
        return getCourseDetails();
    }


    public String toFileString() {
        return courseID + "|" + courseName + "|" + credits + "|" + availableSeats;
    }

    public static Course fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 4) return null;
        try {
            return new Course(
                    parts[0].trim(),
                    parts[1].trim(),
                    Integer.parseInt(parts[2].trim()),
                    Integer.parseInt(parts[3].trim())
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }



    public String getCourseID()                          { return courseID; }
    public void   setCourseID(String courseID)           { this.courseID = courseID; this.id = courseID; }

    public String getCourseName()                        { return courseName; }
    public void   setCourseName(String courseName)       { this.courseName = courseName; }

    public int    getCredits()                           { return credits; }
    public void   setCredits(int credits)                { this.credits = credits; }

    public int    getAvailableSeats()                    { return availableSeats; }
    public void   setAvailableSeats(int availableSeats)  { this.availableSeats = availableSeats; }
}