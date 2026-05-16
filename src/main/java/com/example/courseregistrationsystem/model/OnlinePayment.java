package com.example.courseregistrationsystem.model;



public class OnlinePayment extends Payment {

    // Extra field specific to online payments
    private String transactionReference;

    // ---- Constructors ----



    public OnlinePayment(String id, String createdAt, String paymentID, double amount, String paymentMethod, String paymentStatus) {
        super(paymentID, studentID, registrationID, paymentMethod , studentID,registrationID,amount,paymentMethod,paymentStatus);
        // Auto-generate a transaction reference
        this.transactionReference = transactionReference;
    }


    @Override
    public boolean processPayment() {
        // Simulate online payment gateway processing
        this.setPaymentStatus(STATUS_COMPLETED);
        System.out.println("Online payment processed. Ref: " + transactionReference);
        return true;
    }


    @Override
    public String generateReceipt() {
        return super.generateReceipt() +
                "\n  Transaction Ref: " + transactionReference;
    }

    public String getTransactionReference()                        { return transactionReference; }
    public void   setTransactionReference(String transactionRef)   { this.transactionReference = transactionRef; }
}