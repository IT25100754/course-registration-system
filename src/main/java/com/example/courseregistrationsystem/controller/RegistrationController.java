package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Registration;
import com.example.courseregistrationsystem.service.CourseService;
import com.example.courseregistrationsystem.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listRegistrations(Model model) {
        List<Registration> registrations = registrationService.getAllRegistrations();
        model.addAttribute("registrations", registrations);
        model.addAttribute("pageTitle", "All Registrations");
        return "registration/list";
    }

    @GetMapping("/new")
    public String showRegistrationForm(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("pageTitle", "New Course Registration");
        return "registration/form";
    }

    @PostMapping("/new")
    public String createRegistration(
            @RequestParam("studentID") String studentID,
            @RequestParam("courseID") String courseID,
            RedirectAttributes redirectAttributes) {

        String[] resultMessage = new String[1];
        Registration reg = registrationService.registerStudent(studentID, courseID, resultMessage);

        if (reg != null) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful! ID: " + reg.getRegistrationID() +
                            ". Please complete payment below.");
            redirectAttributes.addFlashAttribute("newRegId", reg.getRegistrationID());
            redirectAttributes.addFlashAttribute("newStudentId", studentID);
            return "redirect:/payments/new?regId=" + reg.getRegistrationID() + "&studentId=" + studentID;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    resultMessage[0].replace("ERROR: ", ""));
            return "redirect:/registrations/new";
        }
    }

    @GetMapping("/view/{id}")
    public String viewRegistration(@PathVariable("id") String registrationID,
                                   Model model, RedirectAttributes redirectAttributes) {

        Optional<Registration> opt = registrationService.getById(registrationID);

        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Registration ID '" + registrationID + "' not found.");
            return "redirect:/registrations";
        }

        Registration reg = opt.get();
        model.addAttribute("registration", reg);

        courseService.getCourseById(reg.getCourseID())
                .ifPresent(c -> model.addAttribute("course", c));

        model.addAttribute("pageTitle", "Registration Details: " + registrationID);
        return "registration/view";
    }

    @GetMapping("/student/{studentId}")
    public String viewByStudent(@PathVariable("studentId") String studentID,
                                Model model, RedirectAttributes redirectAttributes) {

        List<Registration> regs = registrationService.getByStudentId(studentID);
        model.addAttribute("registrations", regs);
        model.addAttribute("studentID", studentID);

        model.addAttribute("pageTitle", "Registrations for: " + studentID);
        return "registration/student-list";
    }

    @GetMapping("/switch/{id}")
    public String showSwitchForm(@PathVariable("id") String registrationID,
                                 Model model, RedirectAttributes redirectAttributes) {

        Optional<Registration> opt = registrationService.getById(registrationID);

        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Registration not found: " + registrationID);
            return "redirect:/registrations";
        }

        Registration reg = opt.get();

        if (!reg.isActive()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot switch — registration is already cancelled.");
            return "redirect:/registrations";
        }

        model.addAttribute("registration", reg);
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("pageTitle", "Switch Course for: " + registrationID);
        return "registration/switch";
    }

    @PostMapping("/switch/{id}")
    public String switchCourse(@PathVariable("id") String registrationID,
                               @RequestParam("newCourseID") String newCourseID,
                               RedirectAttributes redirectAttributes) {

        String[] resultMessage = new String[1];
        Registration newReg = registrationService.switchCourse(registrationID, newCourseID, resultMessage);

        if (newReg != null) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Course switched successfully! New Registration ID: " + newReg.getRegistrationID());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    resultMessage[0].replace("ERROR: ", ""));
        }
        return "redirect:/registrations";
    }

    @GetMapping("/cancel/{id}")
    public String cancelRegistration(@PathVariable("id") String registrationID,
                                     RedirectAttributes redirectAttributes) {

        String result = registrationService.cancelRegistration(registrationID);

        if (result.startsWith("SUCCESS")) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration '" + registrationID + "' cancelled. Seat restored.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    result.replace("ERROR: ", ""));
        }
        return "redirect:/registrations";
    }

    @GetMapping("/search")
    public String searchRegistrations(
            @RequestParam(value = "studentID", required = false) String studentID,
            Model model) {

        if (studentID != null && !studentID.trim().isEmpty()) {
            model.addAttribute("registrations", registrationService.getByStudentId(studentID.trim()));
            model.addAttribute("searchedStudent", studentID.trim());
        } else {
            model.addAttribute("registrations", registrationService.getAllRegistrations());
        }

        model.addAttribute("pageTitle", "Search Registrations");
        return "registration/list";
    }
}