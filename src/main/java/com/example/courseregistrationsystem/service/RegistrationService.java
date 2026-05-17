package com.example.courseregistrationsystem.service;



import com.example.courseregistrationsystem.model.Registration;
import com.example.courseregistrationsystem.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {
    @Autowired
    private  FileStorageService fileStorageService;

    @Autowired
    private RegistrationRepository registrationRepository;

    public void registerCourse(Registration registration) {
        registrationRepository.save(registration);
    }

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public void saveRegistration(
            Registration registration) {

        String data =
                registration.getId() + "," +
                        registration.getStudentId() + "," +
                        registration.getCourseId() + "," +
                        registration.getStatus();

        fileStorageService.writeToFile(
                "data/registrations.txt",
                data
        );
    }

    public Optional<Object> getById(String registrationID) {
        return null;
    }
}
