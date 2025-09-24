package edu.slu.accounts.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection utility class
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:university.db";
    private static Connection connection;

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    /**
     * Get database connection (singleton pattern)
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            initializeTables();
        }
        return connection;
    }

    /**
     * Initialize database tables
     */
    private static void initializeTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create students table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS students (
                    id TEXT PRIMARY KEY,
                    student_number TEXT UNIQUE NOT NULL,
                    first_name TEXT NOT NULL,
                    middle_name TEXT,
                    last_name TEXT NOT NULL,
                    course TEXT NOT NULL,
                    year_level INTEGER NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create accounts table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS accounts (
                    id TEXT PRIMARY KEY,
                    student_id TEXT NOT NULL,
                    semester TEXT NOT NULL,
                    academic_year TEXT NOT NULL,
                    total_assessment REAL NOT NULL DEFAULT 0,
                    total_paid REAL NOT NULL DEFAULT 0,
                    remaining_balance REAL NOT NULL DEFAULT 0,
                    prelim_amount_due REAL NOT NULL DEFAULT 0,
                    midterm_amount_due REAL NOT NULL DEFAULT 0,
                    final_amount_due REAL NOT NULL DEFAULT 0,
                    prelim_status TEXT DEFAULT 'UNPAID',
                    midterm_status TEXT DEFAULT 'UNPAID',
                    final_status TEXT DEFAULT 'UNPAID',
                    exam_permission TEXT DEFAULT 'NOT_PERMITTED',
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (student_id) REFERENCES students (id)
                )
            """);

            // Create transactions table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS transactions (
                    id TEXT PRIMARY KEY,
                    student_id TEXT NOT NULL,
                    account_id TEXT NOT NULL,
                    transaction_type TEXT NOT NULL,
                    description TEXT NOT NULL,
                    amount REAL NOT NULL,
                    payment_method TEXT,
                    payment_reference TEXT,
                    transaction_date TEXT DEFAULT CURRENT_TIMESTAMP,
                    status TEXT DEFAULT 'COMPLETED',
                    FOREIGN KEY (student_id) REFERENCES students (id),
                    FOREIGN KEY (account_id) REFERENCES accounts (id)
                )
            """);

            // Create grades table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS grades (
                    id TEXT PRIMARY KEY,
                    student_id TEXT NOT NULL,
                    subject_code TEXT NOT NULL,
                    subject_name TEXT NOT NULL,
                    units REAL NOT NULL,
                    prelim_grade REAL,
                    midterm_grade REAL,
                    final_grade REAL,
                    semester_grade REAL,
                    remarks TEXT,
                    semester TEXT NOT NULL,
                    academic_year TEXT NOT NULL,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (student_id) REFERENCES students (id)
                )
            """);

            // Create payment_methods table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS payment_methods (
                    id TEXT PRIMARY KEY,
                    method_name TEXT NOT NULL,
                    method_code TEXT UNIQUE NOT NULL,
                    service_fee REAL DEFAULT 0,
                    percentage_fee REAL DEFAULT 0,
                    is_active BOOLEAN DEFAULT 1,
                    description TEXT
                )
            """);

            System.out.println("Database tables initialized successfully.");
        }
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}