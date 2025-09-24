package edu.slu.accounts.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password utility class for hashing and verifying passwords
 */
public class PasswordUtil {
    private static final int BCRYPT_ROUNDS = 12;

    private PasswordUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Hash a password using BCrypt
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verify a password against a hash
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Generate a simple password for demo purposes
     */
    public static String generateDemoPassword() {
        return "password"; // Simple password for demo
    }
}