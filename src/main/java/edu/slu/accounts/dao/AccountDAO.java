package edu.slu.accounts.dao;

import edu.slu.accounts.model.Account;
import edu.slu.accounts.util.DatabaseConnection;
import edu.slu.accounts.util.IdGenerator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Account operations
 */
public class AccountDAO {

    /**
     * Create a new account
     */
    public boolean createAccount(Account account) {
        String sql = """
            INSERT INTO accounts (id, student_id, semester, academic_year, total_assessment, 
                                total_paid, remaining_balance, prelim_amount_due, midterm_amount_due, 
                                final_amount_due, prelim_status, midterm_status, final_status, 
                                exam_permission, created_at, updated_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (account.getId() == null) {
                account.setId(IdGenerator.generateId());
            }

            pstmt.setString(1, account.getId());
            pstmt.setString(2, account.getStudentId());
            pstmt.setString(3, account.getSemester());
            pstmt.setString(4, account.getAcademicYear());
            pstmt.setDouble(5, account.getTotalAssessment());
            pstmt.setDouble(6, account.getTotalPaid());
            pstmt.setDouble(7, account.getRemainingBalance());
            pstmt.setDouble(8, account.getPrelimAmountDue());
            pstmt.setDouble(9, account.getMidtermAmountDue());
            pstmt.setDouble(10, account.getFinalAmountDue());
            pstmt.setString(11, account.getPrelimStatus().name());
            pstmt.setString(12, account.getMidtermStatus().name());
            pstmt.setString(13, account.getFinalStatus().name());
            pstmt.setString(14, account.getExamPermission().name());
            pstmt.setString(15, account.getCreatedAt().toString());
            pstmt.setString(16, account.getUpdatedAt().toString());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find account by student ID and current semester
     */
    public Optional<Account> findByStudentId(String studentId) {
        return findByStudentIdAndSemester(studentId, "FIRST SEMESTER", "2025-2026");
    }

    /**
     * Find account by student ID and specific semester
     */
    public Optional<Account> findByStudentIdAndSemester(String studentId, String semester, String academicYear) {
        String sql = "SELECT * FROM accounts WHERE student_id = ? AND semester = ? AND academic_year = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, semester);
            pstmt.setString(3, academicYear);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAccount(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding account by student ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Find account by ID
     */
    public Optional<Account> findById(String id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAccount(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding account by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Update account
     */
    public boolean updateAccount(Account account) {
        String sql = """
            UPDATE accounts SET total_paid = ?, remaining_balance = ?, prelim_amount_due = ?, 
                              midterm_amount_due = ?, final_amount_due = ?, prelim_status = ?, 
                              midterm_status = ?, final_status = ?, exam_permission = ?, updated_at = ? 
            WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            account.setUpdatedAt(LocalDateTime.now());

            pstmt.setDouble(1, account.getTotalPaid());
            pstmt.setDouble(2, account.getRemainingBalance());
            pstmt.setDouble(3, account.getPrelimAmountDue());
            pstmt.setDouble(4, account.getMidtermAmountDue());
            pstmt.setDouble(5, account.getFinalAmountDue());
            pstmt.setString(6, account.getPrelimStatus().name());
            pstmt.setString(7, account.getMidtermStatus().name());
            pstmt.setString(8, account.getFinalStatus().name());
            pstmt.setString(9, account.getExamPermission().name());
            pstmt.setString(10, account.getUpdatedAt().toString());
            pstmt.setString(11, account.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating account: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find all accounts
     */
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding all accounts: " + e.getMessage());
        }

        return accounts;
    }

    /**
     * Find accounts with unpaid prelims
     */
    public List<Account> findUnpaidPrelims() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE prelim_status = 'UNPAID' ORDER BY updated_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding unpaid prelim accounts: " + e.getMessage());
        }

        return accounts;
    }

    /**
     * Map ResultSet to Account object
     */
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getString("id"));
        account.setStudentId(rs.getString("student_id"));
        account.setSemester(rs.getString("semester"));
        account.setAcademicYear(rs.getString("academic_year"));
        account.setTotalAssessment(rs.getDouble("total_assessment"));
        account.setTotalPaid(rs.getDouble("total_paid"));
        account.setRemainingBalance(rs.getDouble("remaining_balance"));
        account.setPrelimAmountDue(rs.getDouble("prelim_amount_due"));
        account.setMidtermAmountDue(rs.getDouble("midterm_amount_due"));
        account.setFinalAmountDue(rs.getDouble("final_amount_due"));
        account.setPrelimStatus(Account.PaymentStatus.valueOf(rs.getString("prelim_status")));
        account.setMidtermStatus(Account.PaymentStatus.valueOf(rs.getString("midterm_status")));
        account.setFinalStatus(Account.PaymentStatus.valueOf(rs.getString("final_status")));
        account.setExamPermission(Account.ExamPermission.valueOf(rs.getString("exam_permission")));
        
        String createdAtStr = rs.getString("created_at");
        if (createdAtStr != null) {
            account.setCreatedAt(LocalDateTime.parse(createdAtStr));
        }
        
        String updatedAtStr = rs.getString("updated_at");
        if (updatedAtStr != null) {
            account.setUpdatedAt(LocalDateTime.parse(updatedAtStr));
        }

        return account;
    }
}