package edu.slu.accounts.ui;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.service.DataInitializationService;

import javax.swing.*;
import java.awt.*;

class StudentPortalLoginFrame extends JFrame {
	private final AuthenticationService authenticationService;

	StudentPortalLoginFrame(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
		setTitle("iSLU Student Portal - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(720, 480));
		setLocationRelativeTo(null);

		JPanel root = new JPanel(new GridBagLayout());
		root.setBackground(Color.WHITE);
		setContentPane(root);

		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));
		card.setBackground(new Color(40, 40, 40));

		JLabel title = new JLabel("Student Login");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("SansSerif", Font.BOLD, 18));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		JTextField studentNumberField = new JTextField();
		studentNumberField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		studentNumberField.putClientProperty("JComponent.sizeVariant", "regular");
		studentNumberField.setToolTipText("Student Number");

		JPasswordField passwordField = new JPasswordField();
		passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		passwordField.setToolTipText("Password");

		JButton loginButton = new JButton("Login");
		loginButton.setBackground(new Color(70, 130, 180));
		loginButton.setForeground(Color.WHITE);
		loginButton.setFocusPainted(false);
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel demo = new JLabel("Tip: try 2024001 / password");
		demo.setForeground(Color.LIGHT_GRAY);
		demo.setAlignmentX(Component.CENTER_ALIGNMENT);

		loginButton.addActionListener(e -> doLogin(studentNumberField.getText().trim(), new String(passwordField.getPassword()).trim()));
		getRootPane().setDefaultButton(loginButton);

		card.add(title);
		card.add(Box.createVerticalStrut(16));
		card.add(new JLabel("Student Number"));
		card.add(studentNumberField);
		card.add(Box.createVerticalStrut(12));
		card.add(new JLabel("Password"));
		card.add(passwordField);
		card.add(Box.createVerticalStrut(16));
		card.add(loginButton);
		card.add(Box.createVerticalStrut(8));
		card.add(demo);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		root.add(card, gbc);
	}

	private void doLogin(String studentNumber, String password) {
		if (studentNumber.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Enter student number and password.", "Validation", JOptionPane.WARNING_MESSAGE);
			return;
		}
		boolean ok = authenticationService.login(studentNumber, password);
		if (!ok) {
			JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
			return;
		}
		SwingUtilities.invokeLater(() -> {
			dispose();
			new StudentPortalMainFrame(authenticationService).setVisible(true);
		});
	}
}

