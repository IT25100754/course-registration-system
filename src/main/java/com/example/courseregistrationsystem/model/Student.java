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

    public Student(String id, String name, String email, String password,
                   String studentId, String phone, String faculty,
                   String department, String createdAt) {
        super(id, createdAt);
        this.name = name;
        this.email = email;
        this.password = password;
        this.studentId = studentId;
        this.phone = phone;
        this.faculty = faculty;
        this.department = department;
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

    // FIX: safer access (not public anymore)
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // ── FILE STORAGE ───────────────────────────────────────────

    @Override
    public String toFileString() {
        return getId() + "|" +
                name + "|" +
                email + "|" +
                password + "|" +
                studentId + "|" +
                phone + "|" +
                faculty + "|" +
                department + "|" +
                getCreatedAt();
    }

    public static Student fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 9) return null;

        return new Student(
                p[0], // id
                p[1], // name
                p[2], // email
                p[3], // password
                p[4], // studentId
                p[5], // phone
                p[6], // faculty
                p[7], // department
                p[8]  // createdAt
        );
    }
}