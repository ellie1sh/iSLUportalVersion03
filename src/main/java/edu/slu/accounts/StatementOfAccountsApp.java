package edu.slu.accounts;

import edu.slu.accounts.service.DataInitializationService;
import edu.slu.accounts.ui.ConsoleUI;
import edu.slu.accounts.util.DatabaseConnection;

/**
 * Main application class for the Statement of Accounts System
 */
public class StatementOfAccountsApp {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                   SAINT LOUIS UNIVERSITY                      ");
        System.out.println("              STATEMENT OF ACCOUNTS SYSTEM                     ");
        System.out.println("                     Starting Up...                            ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();

        try {
            // Test database connection
            System.out.println("🔄 Testing database connection...");
            if (DatabaseConnection.testConnection()) {
                System.out.println("✅ Database connection successful!");
            } else {
                System.err.println("❌ Database connection failed!");
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

            // Start the console UI
            System.out.println("🚀 Starting application...");
            System.out.println();
            
            ConsoleUI ui = new ConsoleUI();
            ui.start();

        } catch (Exception e) {
            System.err.println("❌ Application startup failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close database connection
            DatabaseConnection.closeConnection();
            System.out.println("👋 Application terminated.");
        }
    }
}