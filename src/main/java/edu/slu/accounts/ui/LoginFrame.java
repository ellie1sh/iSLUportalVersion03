package edu.slu.accounts.ui;

import edu.slu.accounts.service.AuthenticationService;

import javax.swing.*;
import java.awt.*;

/**
 * Swing login window that authenticates against the SQLite-backed Student table
 */
public class LoginFrame extends JFrame {
    private final JTextField studentNumberField;
    private final JPasswordField passwordField;
    private final AuthenticationService authenticationService;

    public LoginFrame() {
        super("iSLU Student Portal - Login");
        this.authenticationService = new AuthenticationService();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("iSLU Student Portal", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentNumberLabel = new JLabel("Student Number");
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; form.add(studentNumberLabel, gbc);
        studentNumberField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1; form.add(studentNumberField, gbc);

        JLabel passwordLabel = new JLabel("Password");
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; form.add(passwordLabel, gbc);
        passwordField = new JPasswordField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1; form.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> onLogin());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 1; form.add(loginButton, gbc);

        root.add(form, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void onLogin() {
        String studentNumber = studentNumberField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (studentNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter student number and password.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = authenticationService.login(studentNumber, password);
        if (ok) {
            dispose();
            StudentPortalFrame portal = new StudentPortalFrame(authenticationService);
            portal.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

