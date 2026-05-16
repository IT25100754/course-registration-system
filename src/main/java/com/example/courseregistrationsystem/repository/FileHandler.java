package com.example.courseregistrationsystem.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================================
 *  FileHandler.java - FILE HANDLING UTILITY (Repository Layer)
 *
 *  PURPOSE:
 *  - Replaces database with .txt file storage
 *  - All file read/write operations go through this class
 *  - Uses BufferedReader, BufferedWriter, FileReader, FileWriter
 *
 *  @Component - marks this as a Spring-managed bean
 *  @Value     - injects values from application.properties
 *
 *  FILE HANDLING CLASSES USED:
 *  ┌─────────────────────────────────────────────────────────────┐
 *  │  FileReader    - opens a file for character reading          │
 *  │  BufferedReader - wraps FileReader for efficient line reads  │
 *  │  FileWriter    - opens a file for character writing          │
 *  │  BufferedWriter - wraps FileWriter for efficient writes      │
 *  └─────────────────────────────────────────────────────────────┘
 * ================================================================
 */
@Component
public class FileHandler {

    // Injected from application.properties
    @Value("${app.data.students}")
    private String studentsFile;

    @Value("${app.data.courses}")
    private String coursesFile;

    @Value("${app.data.registrations}")
    private String registrationsFile;

    @Value("${app.data.payments}")
    private String paymentsFile;

    // ================================================================
    //  GENERIC READ METHOD
    //  Reads all lines from a .txt file into a List<String>
    // ================================================================

    /**
     * Reads all non-empty, non-comment lines from a file.
     *
     * HOW FILE HANDLING WORKS HERE:
     * 1. FileReader opens the file at the given path
     * 2. BufferedReader wraps it - reads entire lines efficiently
     * 3. readLine() returns null at end of file (EOF)
     * 4. try-with-resources auto-closes the stream (no memory leaks)
     *
     * @param filePath absolute or relative path to the .txt file
     * @return list of lines (strings), empty list if file not found
     */
    public List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();

        // try-with-resources: automatically calls br.close() when done
        try (
                FileReader   fr = new FileReader(filePath);       // Step 1: open file
                BufferedReader br = new BufferedReader(fr)        // Step 2: wrap for efficiency
        ) {
            String line;
            // Step 3: read line by line until null (EOF)
            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Skip empty lines and comment lines (lines starting with #)
                if (!line.isEmpty() && !line.startsWith("#")) {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet - return empty list (first run)
            System.out.println("INFO: File not found, will be created: " + filePath);
        } catch (IOException e) {
            System.err.println("ERROR reading file: " + filePath + " -> " + e.getMessage());
        }
        return lines;
    }

    // ================================================================
    //  GENERIC WRITE METHOD (OVERWRITE)
    //  Writes a complete list of lines to a .txt file
    // ================================================================

    /**
     * Writes all lines to a file, replacing existing content.
     *
     * HOW FILE HANDLING WORKS HERE:
     * 1. FileWriter opens file for writing (append=false → overwrite)
     * 2. BufferedWriter wraps it for efficient batch writing
     * 3. write() writes the string, newLine() adds OS-appropriate line break
     * 4. flush() ensures all buffered data is actually written to disk
     *
     * @param filePath path to the file
     * @param lines    list of strings to write
     */
    public void writeAllLines(String filePath, List<String> lines) {
        // Ensure the parent directory exists
        ensureDirectoryExists(filePath);

        try (
                FileWriter     fw = new FileWriter(filePath, false);  // false = overwrite mode
                BufferedWriter bw = new BufferedWriter(fw)
        ) {
            for (String line : lines) {
                bw.write(line);   // write the data line
                bw.newLine();     // add newline character (\n or \r\n on Windows)
            }
            bw.flush(); // ensure all data is written from buffer to disk
        } catch (IOException e) {
            System.err.println("ERROR writing file: " + filePath + " -> " + e.getMessage());
        }
    }

    // ================================================================
    //  GENERIC APPEND METHOD
    //  Adds a single new line to the end of a .txt file
    // ================================================================

    /**
     * Appends a single line to an existing file without erasing it.
     *
     * HOW FILE HANDLING WORKS HERE:
     * FileWriter(path, true) - the 'true' flag enables APPEND mode
     * This means existing content is preserved and new line is added at the end
     *
     * @param filePath path to the file
     * @param line     string to append
     */
    public void appendLine(String filePath, String line) {
        ensureDirectoryExists(filePath);

        try (
                FileWriter     fw = new FileWriter(filePath, true); // true = append mode
                BufferedWriter bw = new BufferedWriter(fw)
        ) {
            bw.write(line);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            System.err.println("ERROR appending to file: " + filePath + " -> " + e.getMessage());
        }
    }

    // ================================================================
    //  HELPER: Ensure parent directories exist before writing
    // ================================================================

    /**
     * Creates the directory structure if it doesn't exist yet.
     * Called before any write to prevent FileNotFoundException.
     */
    private void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                System.out.println("INFO: Created directory: " + parentDir.getAbsolutePath());
            }
        }
    }

    // ================================================================
    //  CONVENIENCE ACCESSORS - return the configured file paths
    // ================================================================

    public String getStudentsFile()       { return studentsFile; }
    public String getCoursesFile()        { return coursesFile; }
    public String getRegistrationsFile()  { return registrationsFile; }
    public String getPaymentsFile()       { return paymentsFile; }
}