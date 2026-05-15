package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Registration;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.repository.CourseRepository;
import com.example.courseregistrationsystem.repository.GradeRepository;
import com.example.courseregistrationsystem.repository.RegistrationRepository;
import com.example.courseregistrationsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * RegistrationService — business logic layer.
 *
 * Implements UML RegistrationManager:
 * - registerStudent()  → createRegistration()
 * - dropStudents()     → cancelRegistration()
 * - getStudentCourse() → getStudentRegistrations()
 *
 * Also handles CourseManager:
 * - addCourse(), removeCourse(), getAllCourse()
 *
 * OOP: ENCAPSULATION — all business rules (capacity check, duplicate check)
 * are hidden in this service, not scattered in controllers.
 */
@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final CourseRepository       courseRepository;
    private final StudentRepository      studentRepository;
    private final GradeRepository        gradeRepository;

    public RegistrationService(RegistrationRepository registrationRepository,
                               CourseRepository courseRepository,
                               StudentRepository studentRepository,
                               GradeRepository gradeRepository) {
        this.registrationRepository = registrationRepository;
        this.courseRepository       = courseRepository;
        this.studentRepository      = studentRepository;
        this.gradeRepository        = gradeRepository;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // REGISTRATION CRUD
    // ═══════════════════════════════════════════════════════════════════════════

    /** CREATE — registerStudent() from UML RegistrationManager. */
    public Registration createRegistration(String studentId, String courseId,
                                           String studentType) throws IOException {
        // Validate student exists
        studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

        // Check duplicate enrollment
        if (registrationRepository.existsByStudentAndCourse(studentId, courseId)) {
            throw new IllegalStateException("Student already enrolled in this course");
        }

        // Check capacity
        long enrolled = registrationRepository.countByCourseId(courseId);
        if (enrolled >= course.getCapacity()) {
            throw new IllegalStateException("Course is full (capacity: " + course.getCapacity() + ")");
        }

        Registration r = new Registration(
                null, null, studentId, courseId,
                "CONFIRMED", "Default", studentType
        );
        return registrationRepository.save(r);
    }

    /** READ — all registrations. */
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    /** READ — getStudentCourse() from UML. */
    public List<Registration> getStudentRegistrations(String studentId) {
        return registrationRepository.findByStudentId(studentId);
    }

    /** READ — course roster for admin/instructor. */
    public List<Map<String, Object>> getCourseRoster(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

        List<Registration> regs = registrationRepository.findByCourseId(courseId)
                .stream()
                .filter(r -> !"DROPPED".equals(r.getStatus()))
                .collect(Collectors.toList());

        long enrolled = regs.size();
        double availability = course.getCapacity() > 0
                ? ((double)(course.getCapacity() - enrolled) / course.getCapacity()) * 100
                : 0;

        List<Map<String, Object>> studentRows = regs.stream().map(r -> {
            Optional<Student> s = studentRepository.findByStudentId(r.getStudentId());
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("registrationId", r.getId());
            row.put("studentId", r.getStudentId());
            row.put("name",        s.map(Student::getName).orElse("Unknown"));
            row.put("email",       s.map(Student::getEmail).orElse("Unknown"));
            row.put("studentType", r.getStudentType());
            row.put("status",      r.getStatus());
            row.put("enrolledDate", r.getEnrolledDate());
            return row;
        }).collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("course", Map.of(
                "id",         course.getId(),
                "name",       course.getName(),
                "code",       course.getCode(),
                "instructor", course.getInstructor(),
                "credits",    course.getCredits(),
                "capacity",   course.getCapacity()
        ));
        result.put("enrolled",     enrolled);
        result.put("availability", String.format("%.1f", availability));
        result.put("students",     studentRows);
        // Flatten for simple frontend use
        return List.of(result);
    }

    /** UPDATE — change section. */
    public Registration updateSection(String studentId, String courseId,
                                      String newSectionId) throws IOException {
        Registration reg = registrationRepository.findByStudentId(studentId)
                .stream()
                .filter(r -> courseId.equals(r.getCourseId()) && !"DROPPED".equals(r.getStatus()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        reg.setSectionId(newSectionId);
        reg.updateTimestamp();
        return registrationRepository.update(reg);
    }

    /** DELETE — cancelRegistration() / dropStudents() from UML. */
    public boolean cancelRegistration(String studentId, String courseId) throws IOException {
        Registration reg = registrationRepository.findByStudentId(studentId)
                .stream()
                .filter(r -> courseId.equals(r.getCourseId()) && !"DROPPED".equals(r.getStatus()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        reg.cancelRegistration(); // uses the model's own method (OOP)
        registrationRepository.update(reg);
        return true;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // COURSE CRUD  (CourseManager from UML)
    // ═══════════════════════════════════════════════════════════════════════════

    /** addCourse() from UML CourseManager. */
    public Course addCourse(Course course) throws IOException {
        return courseRepository.save(course);
    }

    /** getAllCourse() from UML CourseManager. */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    /** removeCourse() from UML CourseManager. */
    public boolean removeCourse(String id) throws IOException {
        return courseRepository.deleteById(id);
    }

    public Course updateCourse(Course course) throws IOException {
        return courseRepository.update(course);
    }

    /** Get available courses (not yet enrolled by a student). */
    public List<Map<String, Object>> getAvailableCoursesForStudent(String studentId) {
        List<Registration> enrolled = registrationRepository.findByStudentId(studentId)
                .stream()
                .filter(r -> !"DROPPED".equals(r.getStatus()))
                .collect(Collectors.toList());

        Set<String> enrolledCourseIds = enrolled.stream()
                .map(Registration::getCourseId)
                .collect(Collectors.toSet());

        return courseRepository.findAll().stream()
                .map(c -> {
                    long count = registrationRepository.countByCourseId(c.getId());
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id",          c.getId());
                    map.put("courseId",    c.getCourseId());
                    map.put("name",        c.getName());
                    map.put("code",        c.getCode());
                    map.put("credits",     c.getCredits());
                    map.put("instructor",  c.getInstructor());
                    map.put("capacity",    c.getCapacity());
                    map.put("enrolled",    count);
                    map.put("available",   c.getCapacity() - count);
                    map.put("alreadyEnrolled", enrolledCourseIds.contains(c.getId()));
                    return map;
                })
                .collect(Collectors.toList());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // GRADE CRUD
    // ═══════════════════════════════════════════════════════════════════════════

    public com.example.courseregistrationsystem.model.Grade addGrade(String studentId, String courseId,
                                                                     double marks) throws IOException {
        com.example.courseregistrationsystem.model.Grade grade = new com.example.courseregistrationsystem.model.Grade(
                null, null, studentId, courseId, marks
        );
        return gradeRepository.save(grade);
    }

    public List<com.example.courseregistrationsystem.model.Grade> getGradesByStudentId(String studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<com.example.courseregistrationsystem.model.Grade> getAllGrades() {
        return gradeRepository.findAll();
    }
}
