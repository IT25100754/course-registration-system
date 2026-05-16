package com.example.courseregistrationsystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Registration extends BaseEntity {

    // ---- Status constants (prevents typo errors) ----
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_PENDING   = "PENDING";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ---- ENCAPSULATION: private fields ----
    private String    registrationID;
    private String    studentID;
    private String    courseID;
    private LocalDate registrationDate;
    private String    status;

    // ---- Constructors ----

    public Registration() {


    }

    public Registration(String registrationID, String studentID,
                        String courseID, LocalDate registrationDate, String status) {
        this.registrationID   = registrationID;
        this.studentID        = studentID;
        this.courseID         = courseID;
        this.registrationDate = registrationDate;
        this.status           = status;
        this.id               = registrationID; // BaseEntity.id
    }

    // ================================================================
    //  Business Methods (as required by specifications)
    // ================================================================

    public void confirmRegistration() {
        this.status = STATUS_CONFIRMED;
    }


    public void cancelRegistration() {
        this.status = STATUS_CANCELLED;
    }


    public String getDetails() {
        return String.format(
                "Registration[ID=%s | Student=%s | Course=%s | Date=%s | Status=%s]",
                registrationID, studentID, courseID,
                registrationDate != null ? registrationDate.format(DATE_FORMAT) : "N/A",
                status
        );
    }


    public boolean isActive() {
        return STATUS_CONFIRMED.equals(status) || STATUS_PENDING.equals(status);
    }


    public String toFileString() {
        String dateStr = (registrationDate != null)
                ? registrationDate.format(DATE_FORMAT) : "N/A";
        return registrationID + "|" + studentID + "|" + courseID + "|" + dateStr + "|" + status;
    }

    public static Registration fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 5) return null;
        try {
            LocalDate date = LocalDate.parse(parts[3].trim(), DATE_FORMAT);
            return new Registration(
                    parts[0].trim(), parts[1].trim(),
                    parts[2].trim(), date, parts[4].trim()
            );
        } catch (Exception e) {
            return null;
        }
    }


    public String    getRegistrationID()                         { return registrationID; }
    public void      setRegistrationID(String registrationID)    { this.registrationID = registrationID; this.id = registrationID; }

    public String    getStudentID()                              { return studentID; }
    public void      setStudentID(String studentID)              { this.studentID = studentID; }

    public String    getCourseID()                               { return courseID; }
    public void      setCourseID(String courseID)                { this.courseID = courseID; }

    public LocalDate getRegistrationDate()                       { return registrationDate; }
    public void      setRegistrationDate(LocalDate date)         { this.registrationDate = date; }

    public String    getFormattedDate() {
        return registrationDate != null ? registrationDate.format(DATE_FORMAT) : "N/A";
    }

    public String    getStatus()                                 { return status; }
    public void      setStatus(String status)                    { this.status = status; }
}