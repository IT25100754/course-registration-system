package com.example.courseregistrationsystem.model;


public abstract class Payment extends BaseEntity {

    // ---- Payment Status constants ----
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_PENDING   = "PENDING";
    public static final String STATUS_FAILED    = "FAILED";

    // ---- ENCAPSULATION: private fields ----
    private String paymentID;
    protected String studentID;
    private String registrationID;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;





    public Payment(String id, String createdAt, String paymentID, String studentID, String registrationID, String s, double amount, String paymentMethod, String paymentStatus) {
        super(id ,createdAt);
        this.paymentID      = paymentID;
        this.studentID      = studentID;
        this.registrationID = registrationID;
        this.amount         = amount;
        this.paymentMethod  = paymentMethod;
        this.paymentStatus  = paymentStatus;

    }

    // ================================================================
    //  ABSTRACTION: Abstract methods subclasses MUST implement
    // ================================================================

    public abstract boolean processPayment();

    // ================================================================
    //  Concrete business methods (shared by all subclasses)
    // ================================================================


    public String generateReceipt() {
        return String.format(
                "========== PAYMENT RECEIPT ==========\n" +
                        "  Payment ID    : %s\n" +
                        "  Student ID    : %s\n" +
                        "  Registration  : %s\n" +
                        "  Amount (LKR)  : %.2f\n" +
                        "  Method        : %s\n" +
                        "  Status        : %s\n" +
                        "=====================================",
                paymentID, studentID, registrationID,
                amount, paymentMethod, paymentStatus
        );
    }


    public String viewPaymentDetails() {
        return String.format("Payment[ID=%s | Student=%s | Amount=LKR %.2f | Method=%s | Status=%s]",
                paymentID, studentID, amount, paymentMethod, paymentStatus);
    }


    public String getDetails() {
        return viewPaymentDetails();
    }

    public String toFileString() {
        return paymentID + "|" + studentID + "|" + registrationID + "|"
                + amount + "|" + paymentMethod + "|" + paymentStatus;
    }

    public static Payment fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;
        try {
            String id        = parts[0].trim();
            String stuId     = parts[1].trim();
            String regId     = parts[2].trim();
            double amount    = Double.parseDouble(parts[3].trim());
            String method    = parts[4].trim();
            String status    = parts[5].trim();

            // POLYMORPHISM: factory decides which subclass to create
            if ("ONLINE".equalsIgnoreCase(method) || "CARD".equalsIgnoreCase(method)) {
                return new OnlinePayment(id, stuId, regId, amount, method, status);
            } else {
                return new CashPayment(id, stuId, regId, amount, method, status);
            }
        } catch (Exception e) {
            return null;
        }
    }

    // ================================================================
    //  ENCAPSULATION: Getters and Setters
    // ================================================================

    public String getPaymentID()                             { return paymentID; }
    public void   setPaymentID(String paymentID)             { this.paymentID = paymentID; this.paymentID = paymentID; }

    public String getStudentID()                             { return studentID; }
    public void   setStudentID(String studentID)             { this.studentID = studentID; }

    public String getRegistrationID()                        { return registrationID; }
    public void   setRegistrationID(String registrationID)   { this.registrationID = registrationID; }

    public double getAmount()                                { return amount; }
    public void   setAmount(double amount)                   { this.amount = amount; }

    public String getPaymentMethod()                         { return paymentMethod; }
    public void   setPaymentMethod(String paymentMethod)     { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus()                         { return paymentStatus; }
    public void   setPaymentStatus(String paymentStatus)     { this.paymentStatus = paymentStatus; }
}