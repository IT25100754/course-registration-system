package com.example.courseregistrationsystem.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

    /**
     * OOP: ABSTRACTION   - Hides all file I/O (BufferedReader, PrintWriter, Paths)
     *                      behind simple readLines() / writeLines() / appendLine() methods.
     *      ENCAPSULATION  - The DATA_DIR constant and internal helpers are private.
     *      INFORMATION HIDING - Callers (other services) never touch java.io directly.
     *
     * Storage format: plain .txt files, one record per line, pipe-delimited.
     * Files are stored in  data/  directory at project root (auto-created).
     */
    @Service
    public class FileStorageService {

        // OOP: ENCAPSULATION - storage root is a hidden implementation detail
        private static final String DATA_DIR = "data";

        public FileStorageService() {
            // Ensure the data directory exists on startup
            ensureDirectoryExists();
        }

        // ── Private helpers (INFORMATION HIDING) ──────────────────────────────────

        private void ensureDirectoryExists() {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        private Path resolvePath(String filename) {
            return Paths.get(DATA_DIR, filename);
        }

        // ── Public API (ABSTRACTION) ───────────────────────────────────────────────

        /**
         * Reads every non-blank line from the given file.
         * Creates the file automatically if it does not exist.
         */
        public List<String> readLines(String filename) {
            List<String> lines = new ArrayList<>();
            Path path = resolvePath(filename);
            try {
                if (!Files.exists(path)) {
                    Files.createFile(path);
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) {
                            lines.add(line.trim());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("[FileStorageService] Error reading " + filename + ": " + e.getMessage());
            }
            return lines;
        }

        /**
         * Overwrites the entire file with the provided list of lines.
         * Used for update / delete operations.
         */
        public boolean writeLines(String filename, List<String> lines) {
            Path path = resolvePath(filename);
            try {
                try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile(), false))) {
                    for (String line : lines) {
                        writer.println(line);
                    }
                }
                return true;
            } catch (IOException e) {
                System.err.println("[FileStorageService] Error writing " + filename + ": " + e.getMessage());
                return false;
            }
        }

        /**
         * Appends a single line to the end of a file.
         * Used for create (insert) operations.
         */
        public boolean appendLine(String filename, String line) {
            Path path = resolvePath(filename);
            try {
                if (!Files.exists(path)) {
                    Files.createFile(path);
                }
                try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile(), true))) {
                    writer.println(line);
                }
                return true;
            } catch (IOException e) {
                System.err.println("[FileStorageService] Error appending to " + filename + ": " + e.getMessage());
                return false;
            }
        }
    }

