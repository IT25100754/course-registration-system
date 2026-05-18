package com.example.courseregistrationsystem.controller;

import org.apache.catalina.connector.Request;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private RequestController requestService;

    @GetMapping
    public List<Request> getAllRequests() {
        
        return requestService.getAllRequests();
    }
}
