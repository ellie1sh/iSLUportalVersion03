package edu.slu.accounts.util;

import java.util.UUID;

/**
 * ID generator utility class
 */
public class IdGenerator {
    
    private IdGenerator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Generate a unique ID using UUID
     */
    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate a student number (for demo purposes)
     */
    public static String generateStudentNumber() {
        return "2024" + String.format("%03d", (int)(Math.random() * 1000));
    }

    /**
     * Generate a payment reference
     */
    public static String generatePaymentReference() {
        return "PAY" + System.currentTimeMillis();
    }

    /**
     * Generate a transaction reference
     */
    public static String generateTransactionReference() {
        return "TXN" + System.currentTimeMillis();
    }
}