package edu.slu.accounts.model;

import java.time.LocalDateTime;

/**
 * Transaction model class representing financial transactions
 */
public class Transaction {
    private String id;
    private String studentId;
    private String accountId;
    private TransactionType transactionType;
    private String description;
    private double amount;
    private String paymentMethod;
    private String paymentReference;
    private LocalDateTime transactionDate;
    private TransactionStatus status;

    // Enums
    public enum TransactionType {
        ASSESSMENT, PAYMENT, FEE, ADJUSTMENT
    }

    public enum TransactionStatus {
        COMPLETED, PENDING, FAILED, CANCELLED
    }

    // Constructors
    public Transaction() {
        this.transactionDate = LocalDateTime.now();
        this.status = TransactionStatus.COMPLETED;
    }

    public Transaction(String studentId, String accountId, TransactionType transactionType, 
                      String description, double amount) {
        this();
        this.studentId = studentId;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.description = description;
        this.amount = amount;
    }

    public Transaction(String studentId, String accountId, TransactionType transactionType, 
                      String description, double amount, String paymentMethod, String paymentReference) {
        this(studentId, accountId, transactionType, description, amount);
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    // Utility methods
    public boolean isPayment() {
        return transactionType == TransactionType.PAYMENT;
    }

    public boolean isAssessment() {
        return transactionType == TransactionType.ASSESSMENT;
    }

    public String getFormattedAmount() {
        if (isPayment()) {
            return String.format("(%.2f)", Math.abs(amount));
        } else {
            return String.format("%.2f", amount);
        }
    }

    public String getFormattedDate() {
        if (transactionDate != null) {
            return String.format("%02d/%02d/%d", 
                               transactionDate.getMonthValue(),
                               transactionDate.getDayOfMonth(),
                               transactionDate.getYear());
        }
        return "";
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', type=%s, description='%s', amount=%.2f, date=%s, status=%s}", 
                           id, transactionType, description, amount, 
                           transactionDate != null ? transactionDate.toString() : "null", status);
    }
}