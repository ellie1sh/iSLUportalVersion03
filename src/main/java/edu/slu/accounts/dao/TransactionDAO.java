package edu.slu.accounts.dao;

import edu.slu.accounts.model.Transaction;
import edu.slu.accounts.util.DatabaseConnection;
import edu.slu.accounts.util.IdGenerator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Transaction operations
 */
public class TransactionDAO {

    /**
     * Create a new transaction
     */
    public boolean createTransaction(Transaction transaction) {
        String sql = """
            INSERT INTO transactions (id, student_id, account_id, transaction_type, description, 
                                    amount, payment_method, payment_reference, transaction_date, status) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (transaction.getId() == null) {
                transaction.setId(IdGenerator.generateId());
            }

            pstmt.setString(1, transaction.getId());
            pstmt.setString(2, transaction.getStudentId());
            pstmt.setString(3, transaction.getAccountId());
            pstmt.setString(4, transaction.getTransactionType().name());
            pstmt.setString(5, transaction.getDescription());
            pstmt.setDouble(6, transaction.getAmount());
            pstmt.setString(7, transaction.getPaymentMethod());
            pstmt.setString(8, transaction.getPaymentReference());
            pstmt.setString(9, transaction.getTransactionDate().toString());
            pstmt.setString(10, transaction.getStatus().name());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find transactions by student ID
     */
    public List<Transaction> findByStudentId(String studentId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE student_id = ? ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding transactions by student ID: " + e.getMessage());
        }

        return transactions;
    }

    /**
     * Find transactions by account ID
     */
    public List<Transaction> findByAccountId(String accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding transactions by account ID: " + e.getMessage());
        }

        return transactions;
    }

    /**
     * Find transaction by ID
     */
    public Optional<Transaction> findById(String id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToTransaction(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding transaction by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Find all transactions
     */
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding all transactions: " + e.getMessage());
        }

        return transactions;
    }

    /**
     * Find payments by student ID
     */
    public List<Transaction> findPaymentsByStudentId(String studentId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE student_id = ? AND transaction_type = 'PAYMENT' ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding payments by student ID: " + e.getMessage());
        }

        return transactions;
    }

    /**
     * Update transaction status
     */
    public boolean updateTransactionStatus(String transactionId, Transaction.TransactionStatus status) {
        String sql = "UPDATE transactions SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setString(2, transactionId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating transaction status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get total payments for student
     */
    public double getTotalPaymentsByStudentId(String studentId) {
        String sql = "SELECT SUM(ABS(amount)) FROM transactions WHERE student_id = ? AND transaction_type = 'PAYMENT' AND status = 'COMPLETED'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.err.println("Error getting total payments: " + e.getMessage());
        }

        return 0.0;
    }

    /**
     * Map ResultSet to Transaction object
     */
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getString("id"));
        transaction.setStudentId(rs.getString("student_id"));
        transaction.setAccountId(rs.getString("account_id"));
        transaction.setTransactionType(Transaction.TransactionType.valueOf(rs.getString("transaction_type")));
        transaction.setDescription(rs.getString("description"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setPaymentMethod(rs.getString("payment_method"));
        transaction.setPaymentReference(rs.getString("payment_reference"));
        transaction.setStatus(Transaction.TransactionStatus.valueOf(rs.getString("status")));
        
        String transactionDateStr = rs.getString("transaction_date");
        if (transactionDateStr != null) {
            transaction.setTransactionDate(LocalDateTime.parse(transactionDateStr));
        }

        return transaction;
    }
}