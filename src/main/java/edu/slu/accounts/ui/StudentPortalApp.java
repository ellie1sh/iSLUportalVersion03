package edu.slu.accounts.ui;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.service.DataInitializationService;
import edu.slu.accounts.util.DatabaseConnection;

import javax.swing.SwingUtilities;

public class StudentPortalApp {

	public static void main(String[] args) {
		try {
			if (!DatabaseConnection.testConnection()) {
				System.err.println("Database connection failed");
				return;
			}
			DataInitializationService init = new DataInitializationService();
			if (!init.sampleDataExists()) {
				init.initializeSampleData();
			}

			SwingUtilities.invokeLater(() -> {
				AuthenticationService auth = new AuthenticationService();
				new StudentPortalLoginFrame(auth).setVisible(true);
			});
		} finally {
			// do not close DB here since GUI will use it
		}
	}
}

