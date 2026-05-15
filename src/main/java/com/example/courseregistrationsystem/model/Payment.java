package com.example.courseregistrationsystem.model;

/**
 * Payment entity.
 *
 * OOP Concepts:
 * - INHERITANCE: Extends BaseEntity
 * - ENCAPSULATION: All payment details are private
 * - POLYMORPHISM: processPayment() can be overridden by subclasses
 *   (e.g. OnlinePayment, CashPayment)
 *
 * File format:
 *   id|paymentId|studentId|amount|paymentMethod|status|description|paymentDate|createdAt|updatedAt
 */
public class Payment extends BaseEntity {

    // ─── Enum for payment methods (type safety) ──────────────────────────────────
    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, PAYPAL, CASH
    }

    // ─── Enum for payment status ─────────────────────────────────────────────────
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    private String        paymentId;
    private String        studentId;
    private double        amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String        description;
    private String        paymentDate;
    private String        accountNumber; // card/bank account (masked)

    // ─── Constructors ───────────────────────────────────────────────────────────

    public Payment() { super(); }

    public Payment(String id, String paymentId, String studentId,
                   double amount, PaymentMethod paymentMethod,
                   String description, String accountNumber) {
        super(id);
        this.paymentId     = paymentId;
        this.studentId     = studentId;
        this.amount        = amount;
        this.paymentMethod = paymentMethod;
        this.status        = PaymentStatus.PENDING;
        this.description   = description;
        this.paymentDate   = java.time.LocalDateTime.now().toString();
        this.accountNumber = maskAccountNumber(accountNumber);
    }

    // ─── Polymorphism ───────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        return String.join("|",
                getId(),
                paymentId,
                studentId,
                String.valueOf(amount),
                paymentMethod != null ? paymentMethod.name() : "CASH",
                status != null ? status.name() : "PENDING",
                description != null ? description : "",
                paymentDate != null ? paymentDate : "",
                accountNumber != null ? accountNumber : "",
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    @Override
    public String getDisplayName() {
        return "Payment[" + paymentId + "] " + studentId + " LKR " + amount + " via " + paymentMethod;
    }

    /**
     * Process the payment — can be overridden by subclasses for different
     * payment gateway integrations (demonstrates polymorphism).
     */
    public boolean processPayment() {
        // Base implementation: mark as completed
        this.status = PaymentStatus.COMPLETED;
        this.paymentDate = java.time.LocalDateTime.now().toString();
        updateTimestamp();
        return true;
    }

    /**
     * Mask account number for security (encapsulation of sensitive data).
     */
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) return "****";
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    public static Payment fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 6) throw new IllegalArgumentException("Invalid payment record: " + line);

        Payment p = new Payment();
        p.setId(parts[0]);
        p.setPaymentId(parts[1]);
        p.setStudentId(parts[2]);
        p.setAmount(Double.parseDouble(parts[3]));
        try {
            p.setPaymentMethod(PaymentMethod.valueOf(parts[4]));
        } catch (IllegalArgumentException e) {
            p.setPaymentMethod(PaymentMethod.CASH);
        }
        try {
            p.setStatus(PaymentStatus.valueOf(parts[5]));
        } catch (IllegalArgumentException e) {
            p.setStatus(PaymentStatus.PENDING);
        }
        if (parts.length > 6) p.setDescription(parts[6]);
        if (parts.length > 7) p.setPaymentDate(parts[7]);
        if (parts.length > 8) p.setAccountNumber(parts[8]);
        if (parts.length > 9) p.setCreatedAt(parts[9]);
        if (parts.length > 10) p.setUpdatedAt(parts[10]);
        return p;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public String        getPaymentId()                          { return paymentId; }
    public void          setPaymentId(String paymentId)          { this.paymentId = paymentId; }

    public String        getStudentId()                          { return studentId; }
    public void          setStudentId(String studentId)          { this.studentId = studentId; }

    public double        getAmount()                             { return amount; }
    public void          setAmount(double amount)                { this.amount = amount; }

    public PaymentMethod getPaymentMethod()                      { return paymentMethod; }
    public void          setPaymentMethod(PaymentMethod m)       { this.paymentMethod = m; }

    public PaymentStatus getStatus()                             { return status; }
    public void          setStatus(PaymentStatus status)         { this.status = status; }

    public String        getDescription()                        { return description; }
    public void          setDescription(String description)      { this.description = description; }

    public String        getPaymentDate()                        { return paymentDate; }
    public void          setPaymentDate(String paymentDate)      { this.paymentDate = paymentDate; }

    public String        getAccountNumber()                      { return accountNumber; }
    public void          setAccountNumber(String accountNumber)  { this.accountNumber = accountNumber; }
}
