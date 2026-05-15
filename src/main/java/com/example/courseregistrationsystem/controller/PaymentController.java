package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * PaymentController — REST endpoints matching the frontend payment section.
 *
 * POST   /api/payments                     — process a payment
 * GET    /api/payments/student/{studentId} — payment history
 * GET    /api/payments/summary/{studentId} — total fees, paid, balance
 * PUT    /api/payments/{paymentId}/refund  — refund
 * DELETE /api/payments/{paymentId}         — delete (admin)
 */
@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ─── CREATE: Process a payment ────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<?> processPayment(@RequestBody Map<String, Object> body) {
        try {
            String studentId    = (String) body.get("studentId");
            double amount       = Double.parseDouble(body.get("amount").toString());
            String method       = (String) body.getOrDefault("paymentMethodType", "CASH");
            String description  = (String) body.getOrDefault("description", "Course fee payment");
            String accountNum   = (String) body.getOrDefault("accountNumber", null);

            if (studentId == null || studentId.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "studentId is required"));
            }
            if (amount <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Amount must be positive"));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(paymentService.processPayment(
                            studentId, amount, method, description, accountNum));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment failed: " + e.getMessage()));
        }
    }

    // ─── READ: Payment history ────────────────────────────────────────────────────

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getPaymentHistory(@PathVariable String studentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByStudentId(studentId));
    }

    // ─── READ: Payment summary ────────────────────────────────────────────────────

    @GetMapping("/summary/{studentId}")
    public ResponseEntity<?> getPaymentSummary(@PathVariable String studentId) {
        return ResponseEntity.ok(paymentService.getPaymentSummary(studentId));
    }

    // ─── READ: All payments (admin) ───────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // ─── UPDATE: Refund ───────────────────────────────────────────────────────────

    @PutMapping("/{paymentId}/refund")
    public ResponseEntity<?> refundPayment(@PathVariable String paymentId) {
        try {
            return ResponseEntity.ok(paymentService.refundPayment(paymentId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────────

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable String paymentId) {
        try {
            boolean deleted = paymentService.deletePayment(paymentId);
            return deleted
                    ? ResponseEntity.ok(Map.of("message", "Payment record deleted"))
                    : ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
