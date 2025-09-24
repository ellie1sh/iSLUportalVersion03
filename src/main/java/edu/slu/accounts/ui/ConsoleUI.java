package edu.slu.accounts.ui;

import edu.slu.accounts.service.AccountService;
import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.service.GradeService;
import edu.slu.accounts.service.PaymentService;
import edu.slu.accounts.model.Student;

import java.util.Scanner;

/**
 * Console-based user interface for the Statement of Accounts system
 */
public class ConsoleUI {
    private final Scanner scanner;
    private final AuthenticationService authService;
    private final AccountService accountService;
    private final PaymentService paymentService;
    private final GradeService gradeService;
    private boolean running;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.authService = new AuthenticationService();
        this.accountService = new AccountService();
        this.paymentService = new PaymentService();
        this.gradeService = new GradeService();
        this.running = true;
    }

    /**
     * Start the application
     */
    public void start() {
        displayWelcome();
        
        while (running) {
            if (!authService.isLoggedIn()) {
                showLoginScreen();
            } else {
                showMainMenu();
            }
        }
        
        System.out.println("Thank you for using Saint Louis University Statement of Accounts System!");
        scanner.close();
    }

    /**
     * Display welcome screen
     */
    private void displayWelcome() {
        clearScreen();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                   SAINT LOUIS UNIVERSITY                      ");
        System.out.println("              STATEMENT OF ACCOUNTS SYSTEM                     ");
        System.out.println("                  First Semester, 2025-2026                   ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();
    }

    /**
     * Show login screen
     */
    private void showLoginScreen() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                          LOGIN                                ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();
        
        // Show demo credentials
        System.out.println("📝 DEMO CREDENTIALS:");
        System.out.println("   Student 1: 2024001 / password (Unpaid)");
        System.out.println("   Student 2: 2024002 / password (Unpaid)");
        System.out.println();
        
        System.out.print("Student Number: ");
        String studentNumber = scanner.nextLine().trim();
        
        if (studentNumber.isEmpty()) {
            System.out.println("❌ Student number cannot be empty!");
            waitForEnter();
            return;
        }
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        if (password.isEmpty()) {
            System.out.println("❌ Password cannot be empty!");
            waitForEnter();
            return;
        }
        
        System.out.println("\n🔄 Authenticating...");
        
        if (authService.login(studentNumber, password)) {
            System.out.println("✅ Login successful!");
            Student currentUser = authService.getCurrentUser();
            System.out.println("Welcome, " + currentUser.getFullName() + "!");
            waitForEnter();
        } else {
            System.out.println("❌ Invalid credentials. Please try again.");
            waitForEnter();
        }
    }

    /**
     * Show main menu
     */
    private void showMainMenu() {
        clearScreen();
        displayHeader();
        
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                         MAIN MENU                            ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("[1] View Statement of Accounts");
        System.out.println("[2] View Transaction Breakdown");
        System.out.println("[3] Make Payment");
        System.out.println("[4] View Grades");
        System.out.println("[5] Account Information");
        System.out.println("[6] Logout");
        System.out.println("[0] Exit");
        System.out.println();
        System.out.print("Select an option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1" -> viewStatementOfAccounts();
            case "2" -> viewTransactionBreakdown();
            case "3" -> makePayment();
            case "4" -> viewGrades();
            case "5" -> viewAccountInformation();
            case "6" -> logout();
            case "0" -> exitApplication();
            default -> {
                System.out.println("❌ Invalid option. Please try again.");
                waitForEnter();
            }
        }
    }

    /**
     * Display header with user info
     */
    private void displayHeader() {
        if (authService.isLoggedIn()) {
            Student user = authService.getCurrentUser();
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.println("                   SAINT LOUIS UNIVERSITY                      ");
            System.out.println("              STATEMENT OF ACCOUNTS SYSTEM                     ");
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.printf("Student: %s (%s) - %s %d\n", 
                            user.getFullName(), user.getStudentNumber(), 
                            user.getCourse(), user.getYearLevel());
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.println();
        }
    }

    /**
     * View statement of accounts
     */
    private void viewStatementOfAccounts() {
        clearScreen();
        displayHeader();
        
        String studentId = authService.getCurrentUserId();
        String summary = accountService.getPaymentSummary(studentId);
        System.out.println(summary);
        
        // Show payment reminder if unpaid
        if (!accountService.isPrelimPaid(studentId)) {
            System.out.println("💡 TIP: Use option [3] to make a payment and unlock grade access!");
        } else {
            System.out.println("✅ Your prelim payment is complete! You can view your grades.");
        }
        
        waitForEnter();
    }

    /**
     * View transaction breakdown
     */
    private void viewTransactionBreakdown() {
        clearScreen();
        displayHeader();
        
        String studentId = authService.getCurrentUserId();
        String breakdown = accountService.getTransactionBreakdown(studentId);
        System.out.println(breakdown);
        
        waitForEnter();
    }

    /**
     * Make payment
     */
    private void makePayment() {
        clearScreen();
        displayHeader();
        
        String studentId = authService.getCurrentUserId();
        
        // Check if payment is needed
        if (accountService.isPrelimPaid(studentId)) {
            System.out.println("✅ Your prelim payment is already completed!");
            System.out.println("You are permitted to take exams and view grades.");
            waitForEnter();
            return;
        }
        
        boolean paymentSuccess = paymentService.processPaymentInteractive(studentId, scanner);
        
        if (paymentSuccess) {
            System.out.println("\n🎓 CONGRATULATIONS! 🎓");
            System.out.println("You can now:");
            System.out.println("• Take prelim examinations");
            System.out.println("• View your grades");
            System.out.println("• Access all academic services");
        }
        
        waitForEnter();
    }

    /**
     * View grades
     */
    private void viewGrades() {
        clearScreen();
        displayHeader();
        
        String studentId = authService.getCurrentUserId();
        gradeService.displayGrades(studentId);
        
        if (gradeService.canViewGrades(studentId)) {
            System.out.println("\n💡 TIP: Keep up the good work! Midterm and final grades will be");
            System.out.println("    available after respective examination periods.");
        }
        
        waitForEnter();
    }

    /**
     * View account information
     */
    private void viewAccountInformation() {
        clearScreen();
        displayHeader();
        
        Student user = authService.getCurrentUser();
        String studentId = authService.getCurrentUserId();
        
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                    ACCOUNT INFORMATION                        ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Student Number: " + user.getStudentNumber());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Course: " + user.getCourse() + " - Year " + user.getYearLevel());
        System.out.println("Email: " + user.getEmail());
        System.out.println();
        
        // Payment status
        System.out.println("PAYMENT STATUS:");
        System.out.println("• Prelim: " + (accountService.isPrelimPaid(studentId) ? "✅ PAID" : "❌ UNPAID"));
        System.out.println("• Exam Permission: " + (accountService.canTakeExams(studentId) ? "✅ PERMITTED" : "❌ NOT PERMITTED"));
        System.out.println("• Grade Access: " + (gradeService.canViewGrades(studentId) ? "✅ ALLOWED" : "❌ RESTRICTED"));
        
        // Grade summary if accessible
        if (gradeService.canViewGrades(studentId)) {
            System.out.println();
            System.out.println("GRADE SUMMARY:");
            System.out.println("• " + gradeService.getGradeSummary(studentId));
        }
        
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        waitForEnter();
    }

    /**
     * Logout
     */
    private void logout() {
        authService.logout();
        System.out.println("✅ Logged out successfully!");
        waitForEnter();
    }

    /**
     * Exit application
     */
    private void exitApplication() {
        System.out.print("Are you sure you want to exit? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            running = false;
        }
    }

    /**
     * Clear screen (simulate)
     */
    private void clearScreen() {
        // Print multiple newlines to simulate clearing screen
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Wait for user to press Enter
     */
    private void waitForEnter() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}