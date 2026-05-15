package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Payment;
import com.example.courseregistrationsystem.util.FileHandler;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * PaymentRepository — CRUD for Payment via file handling.
 */
@Repository
public class PaymentRepository implements FileRepository<Payment, String> {

    private final FileHandler fileHandler;

    public PaymentRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public Payment save(Payment payment) throws IOException {
        if (payment.getId() == null || payment.getId().isBlank()) {
            payment.setId("P" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            payment.setPaymentId(payment.getId());
        }
        payment.setCreatedAt(java.time.LocalDateTime.now().toString());
        payment.setUpdatedAt(java.time.LocalDateTime.now().toString());
        fileHandler.appendLine(fileHandler.getPaymentsFile(), payment.toFileString());
        return payment;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return fileHandler.readAll(fileHandler.getPaymentsFile())
                .stream()
                .filter(line -> line.startsWith(id + "|"))
                .map(Payment::fromFileString)
                .findFirst();
    }

    @Override
    public List<Payment> findAll() {
        return fileHandler.readAll(fileHandler.getPaymentsFile())
                .stream()
                .map(Payment::fromFileString)
                .collect(Collectors.toList());
    }

    public List<Payment> findByStudentId(String studentId) {
        return findAll().stream()
                .filter(p -> studentId.equals(p.getStudentId()))
                .collect(Collectors.toList());
    }

    @Override
    public Payment update(Payment payment) throws IOException {
        payment.updateTimestamp();
        boolean updated = fileHandler.updateLine(
                fileHandler.getPaymentsFile(),
                payment.getId(),
                payment.toFileString()
        );
        if (!updated) throw new RuntimeException("Payment not found: " + payment.getId());
        return payment;
    }

    @Override
    public boolean deleteById(String id) throws IOException {
        return fileHandler.deleteLine(fileHandler.getPaymentsFile(), id);
    }

    @Override
    public boolean existsById(String id) {
        return fileHandler.exists(fileHandler.getPaymentsFile(), id);
    }

    @Override
    public long count() {
        return fileHandler.readAll(fileHandler.getPaymentsFile()).size();
    }

    public double getTotalPaidByStudentId(String studentId) {
        return findByStudentId(studentId).stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
