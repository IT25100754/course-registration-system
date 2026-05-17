package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private  FileStorageService fileStorageService;

    @Autowired
    private CourseRepository courseRepository;

    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public String deleteCourse(String id) {
        courseRepository.deleteById(id);
        return id;
    }

    public void saveCourse(Course course) {

        String data =course.getId() + "," + course.getCourseName() + "," + course.getFee();

        fileStorageService.writeToFile("data/courses.txt", data);
    }
}
