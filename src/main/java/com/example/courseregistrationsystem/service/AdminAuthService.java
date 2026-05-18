package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Admin;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminAuthService {
    private final String FILE_PATH = "data/admins.txt";
    private List<Admin> admins;

    public AdminAuthService() {
        this.admins = new ArrayList<>();
        loadAdminUsers();
    }



    public void loadAdminUsers() {
        List<Admin> adminList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(","); // Split by comma
                if (credentials.length == 3) {
                    String username = credentials[0].trim();
                    String email = credentials[1].trim();
                    String password = credentials[2].trim();
                    adminList.add(new Admin(username, email, password));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading admin file: " + e.getMessage());
        }

        admins.addAll(adminList);
        System.out.println("admin users are added from file");
    }

    public boolean authenticate(String username, String password) {
        boolean authenticate = false;
        System.out.println(admins);

        for(Admin admin: admins) {

            if(admin.getName().equals(username) && admin.getPassword().equals(password)){
                authenticate = true;
                break;
            }
        }
        return authenticate;
    }

}
