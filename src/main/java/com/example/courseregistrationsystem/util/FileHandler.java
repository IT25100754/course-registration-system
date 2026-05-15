package com.example.courseregistrationsystem.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * FileHandler — Utility class for all file read/write operations.
 *
 * This class implements the file handling requirement:
 * - Reads data from plain text files (pipe-delimited)
 * - Writes/updates/deletes records using file I/O
 * - All CRUD operations are backed by file persistence
 *
 * OOP Concepts:
 * - ENCAPSULATION: All file paths and I/O logic are hidden here
 * - ABSTRACTION: Generic readAll/writeAll methods work for any file
 */
@Component
public class FileHandler {

    private static final Logger log = Logger.getLogger(FileHandler.class.getName());

    @Value("${app.data.directory}")
    private String dataDirectory;

    @Value("${app.data.students}")
    private String studentsFile;

    @Value("${app.data.courses}")
    private String coursesFile;

    @Value("${app.data.registrations}")
    private String registrationsFile;

    @Value("${app.data.grades}")
    private String gradesFile;

    @Value("${app.data.payments}")
    private String paymentsFile;

    // ─── Initialization ─────────────────────────────────────────────────────────

    @PostConstruct
    public void initDataDirectory() {
        try {
            Files.createDirectories(Paths.get(dataDirectory));
            // Create files if they don't exist
            createIfNotExists(studentsFile);
            createIfNotExists(coursesFile);
            createIfNotExists(registrationsFile);
            createIfNotExists(gradesFile);
            createIfNotExists(paymentsFile);
            // Seed demo data if files are empty
            seedDemoData();
            log.info("Data directory initialized: " + dataDirectory);
        } catch (IOException e) {
            log.severe("Failed to initialize data directory: " + e.getMessage());
        }
    }

    private void createIfNotExists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    // ─── Core READ Operation ────────────────────────────────────────────────────

    /**
     * READ: Read all non-empty, non-comment lines from a file.
     */
    public List<String> readAll(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            log.warning("Error reading file " + filePath + ": " + e.getMessage());
        }
        return lines;
    }

    // ─── Core WRITE (CREATE) Operation ──────────────────────────────────────────

    /**
     * CREATE: Append a new record to the file.
     */
    public void appendLine(String filePath, String line) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        }
    }

    // ─── Core UPDATE Operation ───────────────────────────────────────────────────

    /**
     * UPDATE: Replace all lines in a file with the given list.
     * Used after modifying a record in memory.
     */
    public void writeAll(String filePath, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // ─── Core DELETE Operation ───────────────────────────────────────────────────

    /**
     * DELETE: Remove the line that starts with the given ID from the file.
     */
    public boolean deleteLine(String filePath, String id) throws IOException {
        List<String> lines = readAll(filePath);
        List<String> filtered = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            if (line.startsWith(id + "|")) {
                found = true; // skip this line (delete it)
            } else {
                filtered.add(line);
            }
        }

        if (found) {
            writeAll(filePath, filtered);
        }
        return found;
    }

    /**
     * UPDATE: Update a specific line by ID.
     */
    public boolean updateLine(String filePath, String id, String newLine) throws IOException {
        List<String> lines = readAll(filePath);
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(id + "|")) {
                lines.set(i, newLine);
                found = true;
                break;
            }
        }

        if (found) {
            writeAll(filePath, lines);
        }
        return found;
    }

    /**
     * Check if a record with a given id exists.
     */
    public boolean exists(String filePath, String id) {
        return readAll(filePath).stream()
                .anyMatch(line -> line.startsWith(id + "|"));
    }

    // ─── File path accessors ────────────────────────────────────────────────────

    public String getStudentsFile()       { return studentsFile; }
    public String getCoursesFile()        { return coursesFile; }
    public String getRegistrationsFile()  { return registrationsFile; }
    public String getGradesFile()         { return gradesFile; }
    public String getPaymentsFile()       { return paymentsFile; }

    // ─── Seed Demo Data ─────────────────────────────────────────────────────────

    /**
     * Seeds sample courses and one admin student if files are empty.
     */
    private void seedDemoData() throws IOException {
        // Seed courses
        if (readAll(coursesFile).isEmpty()) {
            String now = java.time.LocalDateTime.now().toString();
            List<String> courses = List.of(
                "C001|C001|Introduction to Computer Science|CS101|3|Prof. Alan Turing|30|" + now + "|" + now,
                "C002|C002|Data Structures & Algorithms|CS201|4|Prof. Ada Lovelace|25|" + now + "|" + now,
                "C003|C003|Object-Oriented Programming|CS301|3|Prof. James Gosling|30|" + now + "|" + now,
                "C004|C004|Database Management Systems|CS401|3|Prof. Edgar Codd|28|" + now + "|" + now,
                "C005|C005|Web Development|CS501|3|Prof. Tim Berners-Lee|35|" + now + "|" + now,
                "C006|C006|Artificial Intelligence|CS601|4|Prof. John McCarthy|20|" + now + "|" + now
            );
            writeAll(coursesFile, new ArrayList<>(courses));
            log.info("Seeded " + courses.size() + " demo courses");
        }

        // Seed a demo student
        if (readAll(studentsFile).isEmpty()) {
            String now = java.time.LocalDateTime.now().toString();
            List<String> students = List.of(
                "S001|STU001|John Harvard|john@harvard.edu|password123|+1-617-495-1000|undergraduate|" + now + "|" + now
            );
            writeAll(studentsFile, new ArrayList<>(students));
            log.info("Seeded demo student: john@harvard.edu / password123");
        }

        // Seed a demo grade
        if (readAll(gradesFile).isEmpty()) {
            String now = java.time.LocalDateTime.now().toString();
            List<String> grades = List.of(
                "G001|G001|STU001|C001|88.0|B+|" + now + "|" + now,
                "G002|G002|STU001|C002|92.0|A|" + now + "|" + now
            );
            writeAll(gradesFile, new ArrayList<>(grades));
            log.info("Seeded demo grades");
        }
    }
}
