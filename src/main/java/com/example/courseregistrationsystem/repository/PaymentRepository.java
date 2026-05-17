package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class PaymentRepository {

    @Autowired
    private FileHandler fileHandler;

    // ---- CREATE ----
    public void save(Payment payment) {
        fileHandler.appendLine(fileHandler.getPaymentsFile(), payment.toFileString());
    }

    // ---- READ ALL ----
    public List<Payment> findAll() {
        List<String> lines = fileHandler.readAllLines(fileHandler.getPaymentsFile());
        List<Payment> list = new ArrayList<>();
        for (String line : lines) {
            Payment p = Payment.fromFileString(line);
            if (p != null) list.add(p);
        }
        return list;
    }

    // ---- READ BY ID ----
    public Optional<Payment> findById(String paymentID) {
        return findAll().stream()
                .filter(p -> p.getPaymentID().equalsIgnoreCase(paymentID))
                .findFirst();
    }

    // ---- READ BY STUDENT ----
    public List<Payment> findByStudentId(String studentID) {
        List<Payment> result = new ArrayList<>();
        for (Payment p : findAll()) {
            if (p.getStudentID().equalsIgnoreCase(studentID)) result.add(p);
        }
        return result;
    }

    // ---- READ BY REGISTRATION ----
    public Optional<Payment> findByRegistrationId(String registrationID) {
        return findAll().stream()
                .filter(p -> p.getRegistrationID().equals(registrationID))
                .findFirst();
    }

    // ---- UPDATE ----
    public boolean update(Payment updated) {
        List<Payment> all = findAll();
        boolean found = false;
        List<String> lines = new ArrayList<>();
        for (Payment p : all) {
            if (p.getPaymentID().equals(updated.getPaymentID())) {
                lines.add(updated.toFileString());
                found = true;
            } else {
                lines.add(p.toFileString());
            }
        }
        if (found) fileHandler.writeAllLines(fileHandler.getPaymentsFile(), lines);
        return found;
    }

    // ---- ID GENERATOR ----
    public String generateNextId() {
        List<Payment> all = findAll();
        int maxNum = 0;
        for (Payment p : all) {
            try {
                int num = Integer.parseInt(p.getPaymentID().replaceAll("[^0-9]", ""));
                if (num > maxNum) maxNum = num;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("PAY%03d", maxNum + 1);
    }
}
