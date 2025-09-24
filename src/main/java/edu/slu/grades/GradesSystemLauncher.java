package edu.slu.grades;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main launcher for the Grades System
 * Allows launching either Student or Faculty interface
 */
public class GradesSystemLauncher extends JFrame {
    
    private final Color HEADER_BLUE = new Color(44, 90, 160);
    private final Color WHITE = Color.WHITE;
    
    public GradesSystemLauncher() {
        initializeLauncher();
        createLauncherInterface();
    }
    
    private void initializeLauncher() {
        setTitle("Saint Louis University - Grades System Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void createLauncherInterface() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(HEADER_BLUE);
        headerPanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        
        JLabel titleLabel = new JLabel("Saint Louis University", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(WHITE);
        
        JLabel subtitleLabel = new JLabel("Grades Management System", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(subtitleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        contentPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Student interface button
        JButton studentButton = createLauncherButton(
            "Student Portal",
            "View grades and academic information",
            "👨‍🎓"
        );
        
        studentButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new GradesApplication().setVisible(true);
            });
        });
        
        // Faculty interface button
        JButton facultyButton = createLauncherButton(
            "Faculty Portal",
            "Manage and update student grades",
            "👨‍🏫"
        );
        
        facultyButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new FacultyGradeManager().setVisible(true);
            });
        });
        
        // Demo button (both interfaces)
        JButton demoButton = createLauncherButton(
            "Demo Mode",
            "Launch both Student and Faculty interfaces",
            "🚀"
        );
        
        demoButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                // Create student application
                GradesApplication studentApp = new GradesApplication();
                studentApp.setVisible(true);
                
                // Create faculty manager with reference to student app
                FacultyGradeManager facultyManager = new FacultyGradeManager(studentApp);
                facultyManager.setVisible(true);
                
                // Position windows side by side
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                
                studentApp.setSize(screenWidth / 2, screenHeight - 100);
                studentApp.setLocation(0, 50);
                
                facultyManager.setSize(screenWidth / 2, screenHeight - 100);
                facultyManager.setLocation(screenWidth / 2, 50);
                
                // Show info dialog
                JOptionPane.showMessageDialog(null,
                    "Demo Mode Activated!\n\n" +
                    "Left Window: Student Portal\n" +
                    "Right Window: Faculty Portal\n\n" +
                    "Use the Faculty Portal to update grades\n" +
                    "and see real-time updates in the Student Portal!",
                    "Demo Mode", JOptionPane.INFORMATION_MESSAGE);
            });
        });
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(studentButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(facultyButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        contentPanel.add(demoButton, gbc);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel footerLabel = new JLabel("Select your interface to continue", JLabel.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JButton createLauncherButton(String title, String description, String icon) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(200, 120));
        button.setBackground(WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        
        // Title
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(HEADER_BLUE);
        
        // Description
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>", JLabel.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(Color.GRAY);
        
        // Layout
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(WHITE);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLabel);
        
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        button.add(iconLabel, BorderLayout.NORTH);
        button.add(textPanel, BorderLayout.CENTER);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(227, 242, 253));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(HEADER_BLUE, 2),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(233, 236, 239), 2),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
        });
        
        return button;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GradesSystemLauncher().setVisible(true);
        });
    }
}