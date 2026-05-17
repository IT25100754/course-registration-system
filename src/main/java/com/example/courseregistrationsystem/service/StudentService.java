package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.model.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

    /**
     * OOP: ENCAPSULATION  - All business rules (validation, password check, etc.)
     *                        are locked inside this class. Controllers only call
     *                        public methods and get back clean DTOs.
     *      ABSTRACTION     - Callers don't know about file formats or storage details.
     *      INFORMATION HIDING - password is never exposed outside this service.
     *      POLYMORPHISM    - Uses Student.toFileString() (runtime polymorphism) when
     *                        persisting; uses Student.fromFileString() on reading.
     */
    @Service
    public class StudentService {

        // OOP: ENCAPSULATION - file name is a hidden constant
        private static final String STUDENTS_FILE = "students.txt";

        private final FileStorageService fileStorage;

        @Autowired
        public StudentService(FileStorageService fileStorage) {
            this.fileStorage = fileStorage;
            seedDefaultStudents();   // populate sample data on first run
        }

        // ── PRIVATE HELPERS (Information Hiding) ──────────────────────────────────

        /** Load all students from the text file. */
        private List<Student> loadAll() {
            return fileStorage.readLines(STUDENTS_FILE)
                    .stream()
                    .map(line -> Student.fromFileString((String) line))
                    .filter(s -> s != null)
                    .collect(Collectors.toList());
        }

        /** Persist the full list back to the text file. */
        private void saveAll(List<Student> students) {
            List<String> lines = students.stream()
                    .map(Student::toFileString)   // POLYMORPHISM: calls Student's override
                    .collect(Collectors.toList());
            fileStorage.writeLines(STUDENTS_FILE, lines);
        }

        /** Lookup helper – returns Optional so callers handle absence explicitly. */
        private Optional<Student> findByEmail(String email) {
            return loadAll().stream()
                    .filter(s -> s.getEmail().equalsIgnoreCase(email))
                    .findFirst();
        }

        private Optional<Student> findByStudentId(String studentId) {
            return loadAll().stream()
                    .filter(s -> s.getStudentId().equalsIgnoreCase(studentId))
                    .findFirst();
        }

        private String now() {
            return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        /**
         * Convert Student → ProfileResponse.
         * OOP: INFORMATION HIDING – password is intentionally excluded.
         */
        private StudentDTO.ProfileResponse toProfile(Student s) {
            StudentDTO.ProfileResponse dto = new StudentDTO.ProfileResponse();
            dto.setId(s.getId());
            dto.setName(s.getName());
            dto.setEmail(s.getEmail());
            dto.setStudentId(s.getStudentId());
            dto.setPhone(s.getPhone());
            dto.setFaculty(s.getFaculty());
            dto.setCreatedAt(s.getCreatedAt());
            dto.setSuccess(true);
            dto.setMessage("Profile loaded successfully");
            return dto;
        }

        /** Seed two default students when the file is empty (mirrors frontend mock data). */
        private void seedDefaultStudents() {
            List<Student> existing = loadAll();
            if (existing.isEmpty()) {
                Student s1 = new Student(
                        UUID.randomUUID().toString(),
                        "Alex Johnson", "alex@harvard.edu", "pass123",
                        "HARVARD001", "+1 234 567 8900",
                        "Harvard Faculty of Computing", now()
                );
                Student s2 = new Student(
                        UUID.randomUUID().toString(),
                        "Sarah Williams", "sarah@harvard.edu", "pass456",
                        "HARVARD002", "+1 987 654 3210",
                        "Harvard Faculty of Computing", now()
                );
                fileStorage.appendLine(STUDENTS_FILE, s1.toFileString());
                fileStorage.appendLine(STUDENTS_FILE, s2.toFileString());
            }
        }

        // ── PUBLIC API ─────────────────────────────────────────────────────────────

        /**
         * LOGIN
         * OOP: ABSTRACTION - controller just calls login(); all validation is hidden here.
         */
        public StudentDTO.ApiResponse login(StudentDTO.LoginRequest req) {
            if (req.getEmail() == null || req.getEmail().isBlank() ||
                    req.getPassword() == null || req.getPassword().isBlank()) {
                return new StudentDTO.ApiResponse(false, "Email and password are required");
            }

            Optional<Student> found = findByEmail(req.getEmail());
            if (found.isEmpty()) {
                return new StudentDTO.ApiResponse(false, "Invalid email or password");
            }

            Student s = found.get();
            // OOP: INFORMATION HIDING – password check stays inside the service layer
            if (!s.getPassword().equals(req.getPassword())) {
                return new StudentDTO.ApiResponse(false, "Invalid email or password");
            }

            return new StudentDTO.ApiResponse(true,
                    "Welcome back " + s.getName() + "! You are now logged in.",
                    toProfile(s));
        }

        /**
         * REGISTER
         */
        public StudentDTO.ApiResponse register(StudentDTO.RegisterRequest req) {
            if (req.getName() == null || req.getName().isBlank() ||
                    req.getEmail() == null || req.getEmail().isBlank() ||
                    req.getStudentId() == null || req.getStudentId().isBlank() ||
                    req.getPassword() == null || req.getPassword().isBlank()) {
                return new StudentDTO.ApiResponse(false, "Please fill all required fields");
            }

            if (findByEmail(req.getEmail()).isPresent()) {
                return new StudentDTO.ApiResponse(false, "Email already exists");
            }

            Student newStudent = new Student(
                    UUID.randomUUID().toString(),
                    req.getName(),
                    req.getEmail(),
                    req.getPassword(),
                    req.getStudentId(),
                    req.getPhone() != null ? req.getPhone() : "Not provided",
                    "Harvard Faculty of Computing",
                    now()
            );

            fileStorage.appendLine(STUDENTS_FILE, newStudent.toFileString());
            return new StudentDTO.ApiResponse(true, "Account created! Please login.");
        }

        /**
         * FORGOT / RESET PASSWORD
         */
        public StudentDTO.ApiResponse resetPassword(StudentDTO.ResetPasswordRequest req) {
            if (req.getEmail() == null || req.getEmail().isBlank() ||
                    req.getNewPassword() == null || req.getNewPassword().isBlank() ||
                    req.getConfirmPassword() == null || req.getConfirmPassword().isBlank()) {
                return new StudentDTO.ApiResponse(false, "Please fill all fields");
            }
            if (!req.getNewPassword().equals(req.getConfirmPassword())) {
                return new StudentDTO.ApiResponse(false, "Passwords do not match");
            }
            if (req.getNewPassword().length() < 4) {
                return new StudentDTO.ApiResponse(false, "Password must be at least 4 characters");
            }

            List<Student> all = loadAll();
            Optional<Student> found = all.stream()
                    .filter(s -> s.getEmail().equalsIgnoreCase(req.getEmail()))
                    .findFirst();

            if (found.isEmpty()) {
                return new StudentDTO.ApiResponse(false, "Email not found");
            }

            found.get().setPassword(req.getNewPassword());
            saveAll(all);
            return new StudentDTO.ApiResponse(true, "Password reset successfully! Please login.");
        }

        /**
         * GET PROFILE
         */
        public StudentDTO.ProfileResponse getProfile(String studentId) {
            Optional<Student> found = findByStudentId(studentId);
            if (found.isEmpty()) {
                StudentDTO.ProfileResponse err = new StudentDTO.ProfileResponse();
                err.setSuccess(false);
                err.setMessage("Student not found");
                return err;
            }
            return toProfile(found.get());
        }

        /**
         * UPDATE PHONE
         */
        public StudentDTO.ApiResponse updatePhone(StudentDTO.UpdatePhoneRequest req) {
            if (req.getPhone() == null || req.getPhone().isBlank()) {
                return new StudentDTO.ApiResponse(false, "Enter phone number");
            }

            List<Student> all = loadAll();
            Optional<Student> found = all.stream()
                    .filter(s -> s.getStudentId().equalsIgnoreCase(req.getStudentId()))
                    .findFirst();

            if (found.isEmpty()) {
                return new StudentDTO.ApiResponse(false, "Student not found");
            }

            found.get().setPhone(req.getPhone());
            saveAll(all);
            return new StudentDTO.ApiResponse(true, "Phone updated!", toProfile(found.get()));
        }

        /**
         * CHANGE PASSWORD
         */
        public StudentDTO.ApiResponse changePassword(StudentDTO.ChangePasswordRequest req) {
            if (req.getCurrentPassword() == null || req.getNewPassword() == null ||
                    req.getConfirmPassword() == null ||
                    req.getCurrentPassword().isBlank() || req.getNewPassword().isBlank() ||
                    req.getConfirmPassword().isBlank()) {
                return new StudentDTO.ApiResponse(false, "Fill all fields");
            }
            if (!req.getNewPassword().equals(req.getConfirmPassword())) {
                return new StudentDTO.ApiResponse(false, "Passwords do not match");
            }
            if (req.getNewPassword().length() < 4) {
                return new StudentDTO.ApiResponse(false, "Password too short");
            }

            List<Student> all = loadAll();
            Optional<Student> found = all.stream()
                    .filter(s -> s.getStudentId().equalsIgnoreCase(req.getStudentId()))
                    .findFirst();

            if (found.isEmpty()) {
                return new StudentDTO.ApiResponse(false, "Student not found");
            }

            // OOP: INFORMATION HIDING – only the service can verify the old password
            if (!found.get().getPassword().equals(req.getCurrentPassword())) {
                return new StudentDTO.ApiResponse(false, "Current password incorrect");
            }

            found.get().setPassword(req.getNewPassword());
            saveAll(all);
            return new StudentDTO.ApiResponse(true, "Password changed!");
        }
    }

