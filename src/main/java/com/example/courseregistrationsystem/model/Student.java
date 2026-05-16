package com.example.courseregistrationsystem.model;

import java.util.ArrayList;
import java.util.List;


public class Student extends BaseEntity {

    // ---- ENCAPSULATION: private fields ----
    private String studentID;
    private String name;
    private String email;

    // Tracks which course IDs this student is registered for (in-memory)
    private List<String> registeredCourseIds = new ArrayList<>();

    // ---- Constructors ----

    public Student() {}

    public Student(String studentID, String name, String email) {
        this.studentID = studentID;
        this.name      = name;
        this.email     = email;
        this.id        = studentID; // BaseEntity.id
    }



    public boolean registerCourse(String courseID) {
        if (!registeredCourseIds.contains(courseID)) {
            registeredCourseIds.add(courseID);
            return true;
        }
        return false; // duplicate registration prevention
    }


    public boolean dropCourse(String courseID) {
        return registeredCourseIds.remove(courseID);
    }


    public String viewRegisteredCourse() {
        if (registeredCourseIds.isEmpty()) {
            return "No courses registered yet.";
        }
        return "Registered Courses: " + String.join(", ", registeredCourseIds);
    }


    @Override
    public String getDetails() {
        return String.format("Student[ID=%s | Name=%s | Email=%s]",
                studentID, name, email);
    }


    public String toFileString() {
        return studentID + "|" + name + "|" + email;
    }


    public static Student fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return null;
        return new Student(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }



    public String getStudentID()                        { return studentID; }
    public void   setStudentID(String studentID)        { this.studentID = studentID; this.id = studentID; }

    public String getName()                             { return name; }
    public void   setName(String name)                  { this.name = name; }

    public String getEmail()                            { return email; }
    public void   setEmail(String email)                { this.email = email; }

    public List<String> getRegisteredCourseIds()        { return registeredCourseIds; }
    public void         setRegisteredCourseIds(List<String> ids) { this.registeredCourseIds = ids; }
}