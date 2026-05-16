package com.example.courseregistrationsystem.model;

public class CashPayment extends Payment {

    // Extra field specific to cash payments
    private String receiptNumber;

    // ---- Constructors ----

    public CashPayment() {}

    public CashPayment(String paymentID, String studentID, String registrationID,
                       double amount, String paymentMethod, String paymentStatus) {
        super(paymentID, studentID, registrationID, amount, paymentMethod, paymentStatus);
        this.receiptNumber = "RCP-" + paymentID;
    }


    @Override
    public boolean processPayment() {
        // Cash payment needs manual confirmation at finance office
        this.setPaymentStatus(STATUS_PENDING);
        System.out.println("Cash payment initiated. Please visit Finance Office. Receipt: " + receiptNumber);
        return true; // initiated successfully
    }


    @Override
    public String generateReceipt() {
        return super.generateReceipt() +
                "\n  Receipt Number: " + receiptNumber +
                "\n  NOTE: Please present this at the Finance Office to complete payment.";
    }

    public String getReceiptNumber()                     { return receiptNumber; }
    public void   setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
}