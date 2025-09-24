package edu.slu.accounts.service;

import edu.slu.accounts.dao.AccountDAO;
import edu.slu.accounts.dao.TransactionDAO;
import edu.slu.accounts.model.Account;
import edu.slu.accounts.model.Transaction;
import edu.slu.accounts.util.IdGenerator;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing student accounts and financial information
 */
public class AccountService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    /**
     * Create a new student account with initial assessment
     */
    public boolean createStudentAccount(String studentId) {
        // Create account with standard assessment
        double totalAssessment = 45000.0; // Standard tuition fee
        Account account = new Account(studentId, "FIRST SEMESTER", "2025-2026", totalAssessment);
        
        boolean accountCreated = accountDAO.createAccount(account);
        
        if (accountCreated) {
            // Add initial assessment transactions
            addInitialAssessments(studentId, account.getId());
            System.out.println("Student account created successfully with assessment: ₱" + 
                             String.format("%.2f", totalAssessment));
        }
        
        return accountCreated;
    }

    /**
     * Get student account information
     */
    public Optional<Account> getStudentAccount(String studentId) {
        return accountDAO.findByStudentId(studentId);
    }

    /**
     * Get account by ID
     */
    public Optional<Account> getAccountById(String accountId) {
        return accountDAO.findById(accountId);
    }

    /**
     * Check if student has paid prelim fees
     */
    public boolean isPrelimPaid(String studentId) {
        Optional<Account> accountOpt = getStudentAccount(studentId);
        return accountOpt.map(Account::isPrelimPaid).orElse(false);
    }

    /**
     * Check if student can take exams
     */
    public boolean canTakeExams(String studentId) {
        Optional<Account> accountOpt = getStudentAccount(studentId);
        return accountOpt.map(Account::canTakeExams).orElse(false);
    }

    /**
     * Get student transaction history
     */
    public List<Transaction> getTransactionHistory(String studentId) {
        return transactionDAO.findByStudentId(studentId);
    }

    /**
     * Process payment for student
     */
    public boolean processPayment(String studentId, double amount, String paymentMethod, String paymentReference) {
        Optional<Account> accountOpt = getStudentAccount(studentId);
        
        if (accountOpt.isEmpty()) {
            System.err.println("Account not found for student: " + studentId);
            return false;
        }
        
        Account account = accountOpt.get();
        
        // Create payment transaction
        Transaction paymentTransaction = new Transaction(
            studentId, 
            account.getId(), 
            Transaction.TransactionType.PAYMENT,
            "PAYMENT RECEIVED (" + paymentReference + ")",
            -amount, // Negative amount for payments
            paymentMethod,
            paymentReference
        );
        
        boolean transactionCreated = transactionDAO.createTransaction(paymentTransaction);
        
        if (transactionCreated) {
            // Update account balances
            account.processPayment(amount);
            boolean accountUpdated = accountDAO.updateAccount(account);
            
            if (accountUpdated) {
                System.out.println("Payment processed successfully:");
                System.out.println("  Amount: ₱" + String.format("%.2f", amount));
                System.out.println("  Reference: " + paymentReference);
                System.out.println("  New Balance: ₱" + String.format("%.2f", account.getRemainingBalance()));
                System.out.println("  Prelim Status: " + account.getPrelimStatus());
                System.out.println("  Exam Permission: " + account.getExamPermission());
                return true;
            }
        }
        
        return false;
    }

    /**
     * Get payment summary for display
     */
    public String getPaymentSummary(String studentId) {
        Optional<Account> accountOpt = getStudentAccount(studentId);
        
        if (accountOpt.isEmpty()) {
            return "Account not found";
        }
        
        Account account = accountOpt.get();
        StringBuilder summary = new StringBuilder();
        
        summary.append("═══════════════════════════════════════════════════════════════\n");
        summary.append("                    STATEMENT OF ACCOUNTS                      \n");
        summary.append("                   FIRST SEMESTER, 2025-2026                  \n");
        summary.append("═══════════════════════════════════════════════════════════════\n\n");
        
        summary.append(String.format("Your amount due for PRELIM is: ₱%.2f\n", account.getPrelimAmountDue()));
        summary.append(String.format("Your remaining balance is: ₱%.2f\n\n", account.getRemainingBalance()));
        
        summary.append("PRELIM STATUS: ").append(account.getPrelimStatus()).append("\n");
        summary.append("EXAM PERMISSION: ").append(account.getExamPermission()).append("\n\n");
        
        if (account.getPrelimStatus() == Account.PaymentStatus.UNPAID) {
            summary.append("⚠️  Please pay before prelim exams.\n");
            summary.append("   Ignore if you're SLU Dependent or Full TOF Scholar.\n\n");
        } else {
            summary.append("✅ Permitted to take the exams.\n\n");
        }
        
        return summary.toString();
    }

    /**
     * Add initial assessment transactions
     */
    private void addInitialAssessments(String studentId, String accountId) {
        String[][] assessments = {
            {"TUITION FEE @320.00/u", "9020.00"},
            {"TUITION FEE @1167.00/u", "10503.00"},
            {"TUITION FEE @434.00/u", "1302.00"},
            {"OTHER FEES", "6784.00"},
            {"OTHER/LAB FEE(S)", "14064.00"},
            {"PMS WaterDrinkingSystem (JV100486)", "60.00"},
            {"Internationalization Fee (JV100487)", "150.00"},
            {"Miscellaneous Fees", "3117.00"}
        };

        for (String[] assessment : assessments) {
            Transaction transaction = new Transaction(
                studentId,
                accountId,
                Transaction.TransactionType.ASSESSMENT,
                assessment[0],
                Double.parseDouble(assessment[1])
            );
            transactionDAO.createTransaction(transaction);
        }
    }

    /**
     * Get formatted transaction breakdown
     */
    public String getTransactionBreakdown(String studentId) {
        List<Transaction> transactions = getTransactionHistory(studentId);
        StringBuilder breakdown = new StringBuilder();
        
        breakdown.append("═══════════════════════════════════════════════════════════════\n");
        breakdown.append("                    BREAKDOWN OF FEES                          \n");
        breakdown.append("═══════════════════════════════════════════════════════════════\n");
        breakdown.append(String.format("%-12s %-45s %15s\n", "Date", "Description", "Amount"));
        breakdown.append("───────────────────────────────────────────────────────────────\n");
        
        // Add beginning balance
        breakdown.append(String.format("%-12s %-45s %15s\n", "", "BEGINNING BALANCE", "0.00"));
        
        // Add transactions
        for (Transaction transaction : transactions) {
            String date = transaction.getFormattedDate();
            String description = transaction.getDescription();
            String amount = transaction.getFormattedAmount();
            
            // Truncate description if too long
            if (description.length() > 45) {
                description = description.substring(0, 42) + "...";
            }
            
            breakdown.append(String.format("%-12s %-45s %15s\n", date, description, amount));
        }
        
        breakdown.append("═══════════════════════════════════════════════════════════════\n");
        
        return breakdown.toString();
    }

    /**
     * Get all accounts (for admin purposes)
     */
    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }

    /**
     * Get accounts with unpaid prelims
     */
    public List<Account> getUnpaidPrelimAccounts() {
        return accountDAO.findUnpaidPrelims();
    }
}