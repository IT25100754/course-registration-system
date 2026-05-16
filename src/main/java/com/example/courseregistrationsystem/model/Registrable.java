package com.example.courseregistrationsystem.model;

import java.util.List;


public interface Registrable {


    Registration registerStudent(String studentID, String courseID);


    boolean dropStudent(String registrationID);


    List<Registration> getStudentCourses(String studentID);
}