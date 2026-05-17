package com.example.courseregistrationsystem.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileStorageService {

    public void writeToFile(String fileName, String data) {

        try {

            FileWriter writer = new FileWriter(fileName, true);
            writer.write(data + "\n");
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> readFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(fileName);

        // Check if file exists, if not, return empty list instead of crashing
        if (!file.exists()) {
            System.out.println("File not found: " + fileName + ". Creating empty file...");
            try {
                file.getParentFile().mkdirs(); // Create the 'data' folder if it doesn't exist
                file.createNewFile();          // Create the 'users.txt' file
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    public Optional<Object> readLines(String studentsFile) {
        return null;
    }

    public void writeLines(String studentsFile, List<String> lines) {
    }
}