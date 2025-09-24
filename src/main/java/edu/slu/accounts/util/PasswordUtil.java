package edu.slu.accounts.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification
 * Simple implementation without external dependencies
 */
public class PasswordUtil {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Private constructor to prevent instantiation
     */
    private PasswordUtil() {
    }
    
    /**
     * Hash a plain text password with salt
     */
    public static String hashPassword(String plainPassword) {
        try {
            // Generate salt
            byte[] salt = generateSalt();
            
            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(plainPassword.getBytes());
            
            // Combine salt and hash
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);
            
            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Verify a plain text password against a hashed password
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            byte[] combined = Base64.getDecoder().decode(hashedPassword);
            
            // Extract salt
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            
            // Hash the plain password with the extracted salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedInput = md.digest(plainPassword.getBytes());
            
            // Compare with stored hash
            if (combined.length != salt.length + hashedInput.length) {
                return false;
            }
            
            for (int i = 0; i < hashedInput.length; i++) {
                if (combined[salt.length + i] != hashedInput[i]) {
                    return false;
                }
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Generate a simple password for demo purposes
     */
    public static String generateDemoPassword() {
        return "password"; // Simple password for demo
    }
    
    /**
     * Generate a random salt
     */
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
}