package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("pageTitle", "Available Courses");
        return "course/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("pageTitle", "Add New Course");
        model.addAttribute("editMode", false);
        return "course/form";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Course course,
                            RedirectAttributes redirectAttributes) {

        String result = courseService.addCourse(course);

        if (result.startsWith("SUCCESS")) {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Course '" + course.getCourseName() + "' added successfully."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    result.replace("ERROR: ", "")
            );
        }

        return "redirect:/courses";
    }

    @GetMapping("/view/{id}")
    public String viewCourse(@PathVariable("id") String courseID,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        Optional<Course> opt = courseService.getCourseById(courseID);

        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Course not found: " + courseID
            );
            return "redirect:/courses";
        }

        model.addAttribute("course", opt.get());
        model.addAttribute("pageTitle", "Course Details: " + courseID);

        return "course/view";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String courseID,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        Optional<Course> opt = courseService.getCourseById(courseID);

        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Course not found: " + courseID
            );
            return "redirect:/courses";
        }

        model.addAttribute("course", opt.get());
        model.addAttribute("pageTitle", "Edit Course: " + courseID);
        model.addAttribute("editMode", true);

        return "course/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable("id") String courseID,
                               @ModelAttribute Course course,
                               RedirectAttributes redirectAttributes) {

        course.setCourseID(courseID);

        String result = courseService.updateCourse(course);

        if (result.startsWith("SUCCESS")) {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Course updated successfully."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    result.replace("ERROR: ", "")
            );
        }

        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") String courseID,
                               RedirectAttributes redirectAttributes) {

        String result = courseService.deleteCourse(courseID);

        if (result.startsWith("SUCCESS")) {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Course '" + courseID + "' deleted."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    result.replace("ERROR: ", "")
            );
        }

        return "redirect:/courses";
    }
}