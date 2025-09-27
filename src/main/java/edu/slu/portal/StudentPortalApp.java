package edu.slu.portal;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.service.DataInitializationService;
import edu.slu.accounts.util.DatabaseConnection;
import edu.slu.portal.ui.StudentPortalUI;

import javax.swing.*;

/**
 * Main Student Portal Application
 * Integrates all student services including login, grades, accounts, schedule, etc.
 */
public class StudentPortalApp {
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system look and feel is not available
        }
        
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                   SAINT LOUIS UNIVERSITY                      ");
        System.out.println("                    STUDENT PORTAL SYSTEM                      ");
        System.out.println("                     Starting Up...                            ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();

        SwingUtilities.invokeLater(() -> {
            try {
                // Test database connection
                System.out.println("🔄 Testing database connection...");
                if (DatabaseConnection.testConnection()) {
                    System.out.println("✅ Database connection successful!");
                } else {
                    System.err.println("❌ Database connection failed!");
                    JOptionPane.showMessageDialog(null, 
                        "Database connection failed. Please check your database setup.", 
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }

                // Initialize sample data
                DataInitializationService dataService = new DataInitializationService();
                
                if (!dataService.sampleDataExists()) {
                    System.out.println();
                    dataService.initializeSampleData();
                } else {
                    System.out.println("ℹ️  Sample data already exists, skipping initialization.");
                }

                System.out.println();
                dataService.displaySampleCredentials();

                // Start the student portal UI
                System.out.println("🚀 Starting Student Portal...");
                System.out.println();
                
                StudentPortalUI portal = new StudentPortalUI();
                portal.setVisible(true);

            } catch (Exception e) {
                System.err.println("❌ Application startup failed: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Application startup failed: " + e.getMessage(), 
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
        
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnection.closeConnection();
            System.out.println("👋 Student Portal terminated.");
        }));
    }
}