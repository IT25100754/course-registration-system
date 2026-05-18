package com.example.courseregistrationsystem.model;

public class User {
    private int id ;
    private String name;
    private String email;
    private String password;

    private static int totalUsers = 1000;

    public User(){

    }

    public User(String name , String email , String password){
        this.name = name ;
        this.email = email;
        this.password=password;
        totalUsers++;
        this.id = totalUsers;
    }

    public int getId() {
        return id;
    }

   public String getName(){
        return name;
   }

   public void setName(String name){
        this.name = name;
   }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    // Extra fields
    private String studentId;
    private String phone;
    private String faculty;
    private String createdAt;

    // Getters & Setters
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
