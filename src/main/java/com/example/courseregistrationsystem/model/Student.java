package com.example.courseregistrationsystem.model;

/**
 * OOP: INHERITANCE    - Extends BaseEntity to reuse id, createdAt fields.
 *      ENCAPSULATION  - All fields private; only exposed via getters/setters.
 *      INFO HIDING    - Password should not be exposed to frontend.
 */
public class Student extends BaseEntity {

    private String name;
    private String email;
    private String password;
    private String studentId;
    private String phone;
    private String faculty;
    private String department;

    public Student() {
        super();
    }

    public Student(String id, String name, String email, String password, String studentId, String phone, String faculty, String createdAt) {
        super(id, createdAt);
        this.name = name;
        this.email = email;
        this.password = password;
        this.studentId = studentId;
        this.phone = phone;
        this.faculty = faculty;
    }

    // ── Getters & Setters ─────────────────────────────────────

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // FIX: must be public for service layer access
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getStudentID() {
        return studentId;
    }
    public void setStudentID(String studentID) {
        this.studentId = studentID;
    }


    @Override
    public String toFileString() {
        return getId() + "|" +
                name + "|" +
                email + "|" +
                password + "|" +
                studentId + "|" +
                phone + "|" +
                faculty + "|" +
                getCreatedAt();
    }

    public static Student fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 8) return null;

        return new Student(
                p[0], p[1], p[2], p[3],
                p[4], p[5], p[6], p[7]
        );
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}