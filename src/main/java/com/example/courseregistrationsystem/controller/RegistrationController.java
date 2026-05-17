package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Registration;
import com.example.courseregistrationsystem.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registration")
@CrossOrigin(origins = "*")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    public String registerCourse(@RequestBody Registration registration) {

        registrationService.registerCourse(registration);
        return "Registration Request Sent";
    }

    @GetMapping
    public List<Registration> getAllRegistrations() {
        return registrationService.getAllRegistrations();
    }
}
