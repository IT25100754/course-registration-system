package com.example.courseregistrationsystem.model;

/**
 * Student entity — from the UML class diagram.
 *
 * OOP Concepts:
 * - INHERITANCE: Extends BaseEntity
 * - ENCAPSULATION: All fields private, accessed via getters/setters
 * - POLYMORPHISM: Overrides toFileString() and getDisplayName()
 *
 * File format (pipe-delimited):
 *   id|studentId|name|email|password|phone|type|createdAt|updatedAt
 */
public class Student extends BaseEntity {

    // ─── Fields (Encapsulation) ─────────────────────────────────────────────────
    private String studentId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String type; // "undergraduate" | "graduate"

    // ─── Constructors ───────────────────────────────────────────────────────────

    public Student() {
        super();
    }

    public Student(String id, String studentId, String name, String email,
                   String password, String phone, String type) {
        super(id);
        this.studentId = studentId;
        this.name      = name;
        this.email     = email;
        this.password  = password;
        this.phone     = phone;
        this.type      = type;
    }

    // ─── Abstract method implementations (Polymorphism) ─────────────────────────

    @Override
    public String toFileString() {
        return String.join("|",
                getId(),
                studentId,
                name,
                email,
                password,
                phone != null ? phone : "Not provided",
                type != null ? type : "undergraduate",
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    @Override
    public String getDisplayName() {
        return name + " (" + studentId + ")";
    }

    /**
     * Parse a pipe-delimited file line back into a Student object.
     * Factory method — demonstrates static factory pattern.
     */
    public static Student fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 7) throw new IllegalArgumentException("Invalid student record: " + line);

        Student s = new Student();
        s.setId(parts[0]);
        s.setStudentId(parts[1]);
        s.setName(parts[2]);
        s.setEmail(parts[3]);
        s.setPassword(parts[4]);
        s.setPhone(parts[5]);
        s.setType(parts[6]);
        if (parts.length > 7) s.setCreatedAt(parts[7]);
        if (parts.length > 8) s.setUpdatedAt(parts[8]);
        return s;
    }

    // ─── Getters & Setters (Encapsulation) ───────────────────────────────────────

    public String getStudentId()                     { return studentId; }
    public void   setStudentId(String studentId)     { this.studentId = studentId; }

    public String getName()                          { return name; }
    public void   setName(String name)               { this.name = name; }

    public String getEmail()                         { return email; }
    public void   setEmail(String email)             { this.email = email; }

    public String getPassword()                      { return password; }
    public void   setPassword(String password)       { this.password = password; }

    public String getPhone()                         { return phone; }
    public void   setPhone(String phone)             { this.phone = phone; }

    public String getType()                          { return type; }
    public void   setType(String type)               { this.type = type; }
}
