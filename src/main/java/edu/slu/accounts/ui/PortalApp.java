package edu.slu.accounts.ui;

import edu.slu.accounts.service.DataInitializationService;
import edu.slu.accounts.util.DatabaseConnection;

import javax.swing.SwingUtilities;

/**
 * Java-only entry point for the Student Portal UI (Swing)
 */
public class PortalApp {
    public static void main(String[] args) {
        try {
            // Ensure DB exists and seed sample data
            if (DatabaseConnection.testConnection()) {
                DataInitializationService seeder = new DataInitializationService();
                if (!seeder.sampleDataExists()) {
                    seeder.initializeSampleData();
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}

