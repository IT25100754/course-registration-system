package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseRepository {
    private final String FILE_PATH = "data/courses.txt";

    // 1. READ ALL COURSES FROM TEXT FILE
    public List<Course> readCourses() {
        List<Course> courses = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return courses;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    //Course course = new Course();
                   // courses.add(course);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading courses: " + e.getMessage());
        }
        return courses;
    }

    // 2. WRITE ALL COURSES TO TEXT FILE
    public void writeCourses(List<Course> courses) {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
//            for (Course course : courses) {
//                // Format: ID,Name,Credits
//                bw.write(course.getCourseId() + "," + course.getCourseName() + "," + course.getCredits());
//                bw.newLine();
//            }
//        } catch (IOException e) {
//            System.out.println("Error writing courses: " + e.getMessage());
//        }
    }

    public void add(Course course) {
        
    }

    public void deleteById(String id) {
    }

    public List<Course> findAll() {
        return readCourses();
    }

    public void update(Course updated) {
    }
}