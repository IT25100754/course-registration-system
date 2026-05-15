package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Payment;
import com.example.courseregistrationsystem.model.Registration;
import com.example.courseregistrationsystem.repository.CourseRepository;
import com.example.courseregistrationsystem.repository.PaymentRepository;
import com.example.courseregistrationsystem.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * PaymentService — business logic for payments.
 *
 * OOP: Demonstrates POLYMORPHISM — processPayment() delegates to
 * the Payment model's own processPayment() method, which can be
 * overridden by subclasses in the future (e.g. OnlinePayment).
 */
@Service
public class PaymentService {

    private final PaymentRepository      paymentRepository;
    private final RegistrationRepository registrationRepository;
    private final CourseRepository       courseRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          RegistrationRepository registrationRepository,
                          CourseRepository courseRepository) {
        this.paymentRepository      = paymentRepository;
        this.registrationRepository = registrationRepository;
        this.courseRepository       = courseRepository;
    }

    // ─── CREATE ─────────────────────────────────────────────────────────────────

    public Payment processPayment(String studentId, double amount,
                                  String methodStr, String description,
                                  String accountNumber) throws IOException {
        Payment.PaymentMethod method;
        try {
            method = Payment.PaymentMethod.valueOf(methodStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            method = Payment.PaymentMethod.CASH;
        }

        Payment payment = new Payment(
                null, null, studentId, amount, method, description, accountNumber
        );

        // Use the model's own processPayment() method (OOP - object responsibility)
        payment.processPayment();

        return paymentRepository.save(payment);
    }

    // ─── READ ────────────────────────────────────────────────────────────────────

    public List<Payment> getPaymentsByStudentId(String studentId) {
        return paymentRepository.findByStudentId(studentId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Map<String, Object> getPaymentSummary(String studentId) {
        // Calculate total fees from enrollments
        List<Registration> enrollments = registrationRepository.findByStudentId(studentId)
                .stream()
                .filter(r -> !"DROPPED".equals(r.getStatus()))
                .toList();

        double totalFees = enrollments.stream()
                .mapToDouble(r -> {
                    return courseRepository.findById(r.getCourseId())
                            .map(c -> (double) c.getCredits() * 100)
                            .orElse(0.0);
                })
                .sum();

        double totalPaid = paymentRepository.getTotalPaidByStudentId(studentId);
        double balance   = Math.max(0, totalFees - totalPaid);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("studentId", studentId);
        summary.put("totalFees", totalFees);
        summary.put("totalPaid", totalPaid);
        summary.put("balance",   balance);
        return summary;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    public Payment refundPayment(String paymentId) throws IOException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.updateTimestamp();
        return paymentRepository.update(payment);
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    public boolean deletePayment(String paymentId) throws IOException {
        return paymentRepository.deleteById(paymentId);
    }
}
