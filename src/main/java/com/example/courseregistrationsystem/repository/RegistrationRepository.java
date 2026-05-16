package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class RegistrationRepository {

    @Autowired
    private FileHandler fileHandler;

    // ---- CREATE ----
    public void save(Registration registration) {
        fileHandler.appendLine(fileHandler.getRegistrationsFile(), registration.toFileString());
    }

    // ---- READ ALL ----
    public List<Registration> findAll() {
        List<String> lines = fileHandler.readAllLines(fileHandler.getRegistrationsFile());
        List<Registration> list = new ArrayList<>();
        for (String line : lines) {
            Registration r = Registration.fromFileString(line);
            if (r != null) list.add(r);
        }
        return list;
    }

    // ---- READ BY ID ----
    public Optional<Registration> findById(String registrationID) {
        return findAll().stream()
                .filter(r -> r.getRegistrationID().equalsIgnoreCase(registrationID))
                .findFirst();
    }

    // ---- READ BY STUDENT ----
    public List<Registration> findByStudentId(String studentID) {
        List<Registration> result = new ArrayList<>();
        for (Registration r : findAll()) {
            if (r.getStudentID().equalsIgnoreCase(studentID)) result.add(r);
        }
        return result;
    }

    // ---- UPDATE ----
    public boolean update(Registration updated) {
        List<Registration> all = findAll();
        boolean found = false;
        List<String> lines = new ArrayList<>();
        for (Registration r : all) {
            if (r.getRegistrationID().equals(updated.getRegistrationID())) {
                lines.add(updated.toFileString());
                found = true;
            } else {
                lines.add(r.toFileString());
            }
        }
        if (found) fileHandler.writeAllLines(fileHandler.getRegistrationsFile(), lines);
        return found;
    }

    // ---- DELETE ----
    public boolean deleteById(String registrationID) {
        List<Registration> all = findAll();
        List<String> remaining = new ArrayList<>();
        for (Registration r : all) {
            if (!r.getRegistrationID().equals(registrationID)) remaining.add(r.toFileString());
        }
        fileHandler.writeAllLines(fileHandler.getRegistrationsFile(), remaining);
        return remaining.size() < all.size();
    }

    // ---- DUPLICATE CHECK ----
    public boolean existsByStudentAndCourse(String studentID, String courseID) {
        return findAll().stream()
                .anyMatch(r -> r.getStudentID().equals(studentID)
                        && r.getCourseID().equals(courseID)
                        && r.isActive());
    }

    // ---- ID GENERATOR ----
    public String generateNextId() {
        List<Registration> all = findAll();
        int maxNum = 0;
        for (Registration r : all) {
            try {
                int num = Integer.parseInt(r.getRegistrationID().replaceAll("[^0-9]", ""));
                if (num > maxNum) maxNum = num;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("REG%03d", maxNum + 1);
    }
}