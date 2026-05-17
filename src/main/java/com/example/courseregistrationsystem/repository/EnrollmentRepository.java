package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Enrollment;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class EnrollmentRepository {
    private final String FILE_PATH = "data/enrollments.txt";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectMapper mapper = new ObjectMapper();
    // READ ALL ENROLLMENTS FROM JSON

    public List<Enrollment> readEnrollment() {
        List<Enrollment> enrollment = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return enrollment;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    Enrollment enrollment1 = new Enrollment(data[0].trim(), data[1].trim(), Integer.parseInt(data[2].trim()));
                    enrollment.add(enrollment1);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading courses: " + e.getMessage());
        }
        return enrollment;
    }

    public List<Enrollment> readEnrollments() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        return mapper.readValue(file, new TypeReference<List<Enrollment>>() {});
    }

    public void writeEnrollments(List<Enrollment> enrollments) {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), enrollments);
    }

}


