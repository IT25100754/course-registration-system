package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Registration;
import com.example.courseregistrationsystem.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;



    @Autowired
    private CourseService courseService;

    // ================================================================
    //  CREATE - Register a student for a course
    // ================================================================


    public Registration registerStudent(String studentID, String courseID, String[] resultMessage) {



        // Rule 2: Course must exist
        if (!courseService.existsById(courseID)) {
            resultMessage[0] = "ERROR: Course ID '" + courseID + "' does not exist.";
            return null;
        }

        // Rule 3: Prevent duplicate registration
        if (registrationRepository.existsByStudentAndCourse(studentID, courseID)) {
            resultMessage[0] = "ERROR: Student '" + studentID + "' is already registered for course '" + courseID + "'.";
            return null;
        }

        // Rule 4: Course must have available seats
        boolean seatDecreased = courseService.decreaseSeat(courseID);
        if (!seatDecreased) {
            resultMessage[0] = "ERROR: Course '" + courseID + "' has no available seats.";
            return null;
        }

        // Create the registration record
        Registration reg = new Registration(
                registrationRepository.generateNextId(),
                studentID,
                courseID,
                LocalDate.now(),
                Registration.STATUS_CONFIRMED
        );
        registrationRepository.save(reg);
        resultMessage[0] = "SUCCESS: Registered " + studentID + " for " + courseID + ". ID: " + reg.getRegistrationID();
        return reg;
    }


    //  READ


    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public Optional<Registration> getById(String registrationID) {
        return registrationRepository.findById(registrationID);
    }

    public List<Registration> getByStudentId(String studentID) {
        return registrationRepository.findByStudentId(studentID);
    }


    //  UPDATE - Switch a student's course



    public Registration switchCourse(String registrationID, String newCourseID, String[] resultMessage) {
        Optional<Registration> optReg = registrationRepository.findById(registrationID);

        if (optReg.isEmpty()) {
            resultMessage[0] = "ERROR: Registration ID '" + registrationID + "' not found.";
            return null;
        }

        Registration old = optReg.get();

        if (!old.isActive()) {
            resultMessage[0] = "ERROR: This registration is already cancelled.";
            return null;
        }

        // Cancel old registration and restore seat
        old.cancelRegistration();
        registrationRepository.update(old);
        courseService.increaseSeat(old.getCourseID());

        // Register for new course
        return registerStudent(old.getStudentID(), newCourseID, resultMessage);
    }


    //  DELETE - Cancel a registration



    public String cancelRegistration(String registrationID) {
        Optional<Registration> opt = registrationRepository.findById(registrationID);

        if (opt.isEmpty()) {
            return "ERROR: Registration ID '" + registrationID + "' not found.";
        }

        Registration reg = opt.get();
        if (!reg.isActive()) {
            return "ERROR: Registration '" + registrationID + "' is already cancelled.";
        }

        // Cancel and restore seat
        reg.cancelRegistration();
        registrationRepository.update(reg);
        courseService.increaseSeat(reg.getCourseID());

        return "SUCCESS: Registration '" + registrationID + "' cancelled. Seat restored for course '" + reg.getCourseID() + "'.";
    }
}