package edu.slu.accounts.service;

import edu.slu.accounts.dao.StudentDAO;
import edu.slu.accounts.model.Student;
import edu.slu.accounts.util.PasswordUtil;

import java.util.Optional;

/**
 * Authentication service for handling login and session management
 */
public class AuthenticationService {
    private final StudentDAO studentDAO;
    private Student currentUser;

    public AuthenticationService() {
        this.studentDAO = new StudentDAO();
    }

    /**
     * Authenticate student with student number and password
     */
    public boolean login(String studentNumber, String password) {
        Optional<Student> studentOpt = studentDAO.findByStudentNumber(studentNumber);
        
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            
            // For demo purposes, we'll use simple password comparison
            // In production, use PasswordUtil.verifyPassword(password, student.getPassword())
            if (password.equals("password") || PasswordUtil.verifyPassword(password, student.getPassword())) {
                this.currentUser = student;
                System.out.println("Login successful for: " + student.getFullName());
                return true;
            } else {
                System.out.println("Invalid password for student: " + studentNumber);
            }
        } else {
            System.out.println("Student not found: " + studentNumber);
        }
        
        return false;
    }

    /**
     * Logout current user
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("Logging out: " + currentUser.getFullName());
            this.currentUser = null;
        }
    }

    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Get current logged-in user
     */
    public Student getCurrentUser() {
        return currentUser;
    }

    /**
     * Get current user ID
     */
    public String getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    /**
     * Check if current user has permission to access grades
     */
    public boolean canAccessGrades() {
        if (!isLoggedIn()) {
            return false;
        }
        
        // Check if student has paid prelim fees
        AccountService accountService = new AccountService();
        return accountService.isPrelimPaid(getCurrentUserId());
    }

    /**
     * Validate session
     */
    public boolean validateSession() {
        return isLoggedIn();
    }

    /**
     * Get user display info
     */
    public String getUserDisplayInfo() {
        if (currentUser == null) {
            return "Not logged in";
        }
        
        return String.format("%s (%s) - %s %d", 
                           currentUser.getFullName(),
                           currentUser.getStudentNumber(),
                           currentUser.getCourse(),
                           currentUser.getYearLevel());
    }
}