package com.example.courseregistrationsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private FileStorageService fileStorageService;

//    public String login(String email, String password) {
//
//        if(email.equals("admin@gmail.com")
//                && password.equals("admin123")) {
//
//            return "Admin Login Success";
//        }
//
//        return "Invalid Email or Password";
//    }


    public boolean login(
            String username,
            String password) {

        List<String> users = fileStorageService.readFromFile(Collections.singletonList("data/users.txt"));

        for (String user : users) {

            String[] data =
                    user.split(",");

            if (data[0].equals(username)
                    && data[1].equals(password)) {

                return true;
            }
        }

        return false;
    }
}
