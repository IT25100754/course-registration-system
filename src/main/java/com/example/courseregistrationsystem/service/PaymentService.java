package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.*;
import com.example.courseregistrationsystem.repository.PaymentRepository;
import com.example.courseregistrationsystem.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    // Inject RegistrationRepository directly for payment-registration link
    @Autowired
    private RegistrationRepository regRepo;

    // ---- CREATE ----


    public Payment processPayment(String studentID, String registrationID,
                                  double amount, String paymentMethod, String[] resultMessage) {

        // Validate registration exists
        Optional<Registration> regOpt = regRepo.findById(registrationID);
        if (regOpt.isEmpty()) {
            resultMessage[0] = "ERROR: Registration ID '" + registrationID + "' not found.";
            return null;
        }

        // Validate amount
        if (amount <= 0) {
            resultMessage[0] = "ERROR: Payment amount must be greater than 0.";
            return null;
        }

        String paymentID = paymentRepository.generateNextId();

        // POLYMORPHISM: create the correct Payment subclass based on method
        Payment payment;
        String method = paymentMethod.toUpperCase();

        if ("ONLINE".equals(method) || "CARD".equals(method)) {
            payment = new OnlinePayment(paymentID, studentID, registrationID, amount, method, Payment.STATUS_PENDING);
        } else {
            payment = new CashPayment(paymentID, studentID, registrationID, amount, "CASH", Payment.STATUS_PENDING);
        }

        // POLYMORPHISM: each subclass has its own processPayment() implementation
        payment.processPayment();

        // Save to payments.txt
        paymentRepository.save(payment);
        resultMessage[0] = "SUCCESS: Payment " + paymentID + " processed. Status: " + payment.getPaymentStatus();
        return payment;
    }

    // ---- READ ----

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getById(String paymentID) {
        return paymentRepository.findById(paymentID);
    }

    public List<Payment> getByStudentId(String studentID) {
        return paymentRepository.findByStudentId(studentID);
    }

    public Optional<Payment> getByRegistrationId(String registrationID) {
        return paymentRepository.findByRegistrationId(registrationID);
    }

    // ---- UPDATE - Confirm a cash payment ----

    public String confirmCashPayment(String paymentID) {
        Optional<Payment> opt = paymentRepository.findById(paymentID);
        if (opt.isEmpty()) return "ERROR: Payment not found.";

        Payment p = opt.get();
        p.setPaymentStatus(Payment.STATUS_COMPLETED);
        paymentRepository.update(p);
        return "SUCCESS: Cash payment '" + paymentID + "' confirmed as COMPLETED.";
    }
}