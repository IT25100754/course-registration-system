package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Payment;
import com.example.courseregistrationsystem.service.PaymentService;
import com.example.courseregistrationsystem.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RegistrationService registrationService;

    @GetMapping
    public String listPayments(Model model) {
        List<Payment> payments = paymentService.getAllPayments();

        model.addAttribute("payments", payments);
        model.addAttribute("pageTitle", "All Payments");

        return "payment/list";
    }

    @GetMapping("/new")
    public String showPaymentForm(
            @RequestParam(value = "regId", required = false) String regId,
            @RequestParam(value = "studentId", required = false) String studentId,
            Model model) {

        model.addAttribute("prefilledRegId", regId != null ? regId : "");
        model.addAttribute("prefilledStudentId", studentId != null ? studentId : "");

        model.addAttribute("registrations", registrationService.getAllRegistrations());

        model.addAttribute("defaultAmount", 15000.00);

        model.addAttribute("pageTitle", "Make Payment");

        return "payment/form";
    }

    @PostMapping("/new")
    public String processPayment(
            @RequestParam("studentID") String studentID,
            @RequestParam("registrationID") String registrationID,
            @RequestParam("amount") double amount,
            @RequestParam("paymentMethod") String paymentMethod,
            RedirectAttributes redirectAttributes) {

        String[] resultMessage = new String[1];

        Payment payment = paymentService.processPayment(
                studentID,
                registrationID,
                amount,
                paymentMethod,
                resultMessage
        );

        if (payment != null) {

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Payment successful! Payment ID: "
                            + payment.getPaymentID()
                            + " | Status: "
                            + payment.getPaymentStatus()
            );

            return "redirect:/payments/view/" + payment.getPaymentID();

        } else {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    resultMessage[0].replace("ERROR: ", "")
            );

            return "redirect:/payments/new";
        }
    }

    @GetMapping("/view/{id}")
    public String viewPayment(
            @PathVariable("id") String paymentID,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Payment> opt = paymentService.getById(paymentID);

        if (opt.isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Payment not found: " + paymentID
            );

            return "redirect:/payments";
        }

        Payment payment = opt.get();

        model.addAttribute("payment", payment);

        model.addAttribute("receipt", payment.generateReceipt());

        registrationService.getById(payment.getRegistrationID())
                .ifPresent(r -> model.addAttribute("registration", r));

        model.addAttribute("pageTitle", "Payment Receipt: " + paymentID);

        return "payment/view";
    }

    @GetMapping("/student/{studentId}")
    public String viewByStudent(
            @PathVariable("studentId") String studentID,
            Model model,
            RedirectAttributes redirectAttributes) {

        List<Payment> payments = paymentService.getByStudentId(studentID);

        model.addAttribute("payments", payments);
        model.addAttribute("studentID", studentID);

        model.addAttribute("pageTitle", "Payments for: " + studentID);

        return "payment/list";
    }

    @GetMapping("/confirm/{id}")
    public String confirmCashPayment(
            @PathVariable("id") String paymentID,
            RedirectAttributes redirectAttributes) {

        String result = paymentService.confirmCashPayment(paymentID);

        if (result.startsWith("SUCCESS")) {

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Cash payment '" + paymentID + "' confirmed as COMPLETED."
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    result.replace("ERROR: ", "")
            );
        }

        return "redirect:/payments";
    }

    @GetMapping("/search")
    public String searchPayments(
            @RequestParam(value = "studentID", required = false) String studentID,
            Model model) {

        if (studentID != null && !studentID.trim().isEmpty()) {

            model.addAttribute(
                    "payments",
                    paymentService.getByStudentId(studentID.trim())
            );

            model.addAttribute("searchedStudent", studentID.trim());

        } else {

            model.addAttribute(
                    "payments",
                    paymentService.getAllPayments()
            );
        }

        model.addAttribute("pageTitle", "Search Payments");

        return "payment/list";
    }
}