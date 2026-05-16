package com.example.courseregistrationsystem.model;

import java.util.UUID;

public class OnlinePayment extends Payment {

    private String transactionReference;

    // ---- Constructor ----

    public OnlinePayment(String paymentID,
                         String studentID,
                         String registrationID,
                         double amount,
                         String paymentMethod,
                         String paymentStatus) {

        super(paymentID, null, paymentID,
                studentID, registrationID,
                null, amount, paymentMethod, paymentStatus);

        // generate transaction reference safely
        this.transactionReference = UUID.randomUUID().toString();
    }

    @Override
    public boolean processPayment() {
        this.setPaymentStatus(STATUS_COMPLETED);
        System.out.println("Online payment processed. Ref: " + transactionReference);
        return true;
    }

    @Override
    public String generateReceipt() {
        return super.generateReceipt() +
                "\n  Transaction Ref: " + transactionReference;
    }

    // ---- Getter / Setter ----

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }
}