package com.example.courseregistrationsystem.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/grades")
public class GradeController {
    @GetMapping
    public List<Map<String, Object>> getGrades() {
        // Return an empty list for now so the frontend doesn't error 404
        return new ArrayList<>();
    }
}