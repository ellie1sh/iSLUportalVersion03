package edu.slu.accounts.model;

import java.time.LocalDateTime;

/**
 * Account model class representing a student's financial account
 */
public class Account {
    private String id;
    private String studentId;
    private String semester;
    private String academicYear;
    private double totalAssessment;
    private double totalPaid;
    private double remainingBalance;
    private double prelimAmountDue;
    private double midtermAmountDue;
    private double finalAmountDue;
    private PaymentStatus prelimStatus;
    private PaymentStatus midtermStatus;
    private PaymentStatus finalStatus;
    private ExamPermission examPermission;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Enums
    public enum PaymentStatus {
        PAID, UNPAID, PARTIAL
    }

    public enum ExamPermission {
        PERMITTED, NOT_PERMITTED, CONDITIONAL
    }

    // Constructors
    public Account() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.prelimStatus = PaymentStatus.UNPAID;
        this.midtermStatus = PaymentStatus.UNPAID;
        this.finalStatus = PaymentStatus.UNPAID;
        this.examPermission = ExamPermission.NOT_PERMITTED;
    }

    public Account(String studentId, String semester, String academicYear, double totalAssessment) {
        this();
        this.studentId = studentId;
        this.semester = semester;
        this.academicYear = academicYear;
        this.totalAssessment = totalAssessment;
        this.remainingBalance = totalAssessment;
        this.prelimAmountDue = calculatePrelimAmount(totalAssessment);
    }

    // Calculate prelim amount (typically 1/3 of total assessment)
    private double calculatePrelimAmount(double totalAssessment) {
        return totalAssessment * 0.33; // 33% for prelims
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public double getTotalAssessment() { return totalAssessment; }
    public void setTotalAssessment(double totalAssessment) { this.totalAssessment = totalAssessment; }

    public double getTotalPaid() { return totalPaid; }
    public void setTotalPaid(double totalPaid) { this.totalPaid = totalPaid; }

    public double getRemainingBalance() { return remainingBalance; }
    public void setRemainingBalance(double remainingBalance) { this.remainingBalance = remainingBalance; }

    public double getPrelimAmountDue() { return prelimAmountDue; }
    public void setPrelimAmountDue(double prelimAmountDue) { this.prelimAmountDue = prelimAmountDue; }

    public double getMidtermAmountDue() { return midtermAmountDue; }
    public void setMidtermAmountDue(double midtermAmountDue) { this.midtermAmountDue = midtermAmountDue; }

    public double getFinalAmountDue() { return finalAmountDue; }
    public void setFinalAmountDue(double finalAmountDue) { this.finalAmountDue = finalAmountDue; }

    public PaymentStatus getPrelimStatus() { return prelimStatus; }
    public void setPrelimStatus(PaymentStatus prelimStatus) { this.prelimStatus = prelimStatus; }

    public PaymentStatus getMidtermStatus() { return midtermStatus; }
    public void setMidtermStatus(PaymentStatus midtermStatus) { this.midtermStatus = midtermStatus; }

    public PaymentStatus getFinalStatus() { return finalStatus; }
    public void setFinalStatus(PaymentStatus finalStatus) { this.finalStatus = finalStatus; }

    public ExamPermission getExamPermission() { return examPermission; }
    public void setExamPermission(ExamPermission examPermission) { this.examPermission = examPermission; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Business methods
    public void processPayment(double amount) {
        this.totalPaid += amount;
        this.remainingBalance -= amount;
        this.updatedAt = LocalDateTime.now();

        // Update prelim amount due
        if (prelimAmountDue > 0) {
            double prelimPayment = Math.min(amount, prelimAmountDue);
            prelimAmountDue -= prelimPayment;
            
            if (prelimAmountDue <= 0) {
                prelimStatus = PaymentStatus.PAID;
                examPermission = ExamPermission.PERMITTED;
            }
        }

        // Ensure non-negative values
        if (prelimAmountDue < 0) prelimAmountDue = 0;
        if (remainingBalance < 0) remainingBalance = 0;
    }

    public boolean isPrelimPaid() {
        return prelimStatus == PaymentStatus.PAID;
    }

    public boolean canTakeExams() {
        return examPermission == ExamPermission.PERMITTED;
    }

    @Override
    public String toString() {
        return String.format("Account{id='%s', student='%s', semester='%s %s', " +
                           "totalAssessment=%.2f, totalPaid=%.2f, remainingBalance=%.2f, " +
                           "prelimStatus=%s, examPermission=%s}", 
                           id, studentId, semester, academicYear, 
                           totalAssessment, totalPaid, remainingBalance, 
                           prelimStatus, examPermission);
    }
}