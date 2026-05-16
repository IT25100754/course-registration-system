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
}
