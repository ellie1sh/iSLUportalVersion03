package edu.slu.grades;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main Grades Application - Java Swing Implementation
 * Maintains the exact format and functionality from the provided image
 */
public class GradesApplication extends JFrame {
    
    // UI Components
    private JPanel loginPanel;
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JTable gradesTable;
    private DefaultTableModel tableModel;
    private JLabel headerLabel;
    private JLabel studentInfoLabel;
    private JTextArea noticeArea;
    private JPanel legendPanel;
    
    // Data
    private Student currentStudent;
    private List<Grade> gradesList;
    private Timer realTimeUpdateTimer;
    
    // Colors matching the image
    private final Color HEADER_BLUE = new Color(44, 90, 160);
    private final Color SIDEBAR_BLUE = new Color(30, 61, 111);
    private final Color TABLE_HEADER_BLUE = new Color(44, 90, 160);
    private final Color BACKGROUND_GRAY = new Color(245, 245, 245);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(51, 51, 51);
    
    public GradesApplication() {
        initializeApplication();
        createLoginInterface();
        setupRealTimeUpdates();
    }
    
    private void initializeApplication() {
        setTitle("Saint Louis University - Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Initialize data
        gradesList = new ArrayList<>();
        initializeMockData();
    }
    
    private void createLoginInterface() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(HEADER_BLUE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Login container
        JPanel loginContainer = new JPanel();
        loginContainer.setBackground(WHITE);
        loginContainer.setBorder(new EmptyBorder(40, 40, 40, 40));
        loginContainer.setPreferredSize(new Dimension(400, 350));
        loginContainer.setLayout(new BoxLayout(loginContainer, BoxLayout.Y_AXIS));
        
        // University logo
        JLabel logoLabel = new JLabel("Saint Louis University", JLabel.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(HEADER_BLUE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Student Portal", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 0, 10, 0);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel studentNumberLabel = new JLabel("Student Number:");
        studentNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JTextField studentNumberField = new JTextField("2024001");
        studentNumberField.setPreferredSize(new Dimension(250, 35));
        studentNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JPasswordField passwordField = new JPasswordField("password");
        passwordField.setPreferredSize(new Dimension(250, 35));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(250, 40));
        loginButton.setBackground(HEADER_BLUE);
        loginButton.setForeground(WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Demo credentials info
        JTextArea demoInfo = new JTextArea("Demo Credentials:\nStudent: 2024001 / password");
        demoInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        demoInfo.setForeground(Color.GRAY);
        demoInfo.setBackground(new Color(248, 249, 250));
        demoInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        demoInfo.setEditable(false);
        
        // Add components to form
        formGbc.gridx = 0; formGbc.gridy = 0;
        formPanel.add(studentNumberLabel, formGbc);
        formGbc.gridy = 1;
        formPanel.add(studentNumberField, formGbc);
        formGbc.gridy = 2;
        formPanel.add(passwordLabel, formGbc);
        formGbc.gridy = 3;
        formPanel.add(passwordField, formGbc);
        formGbc.gridy = 4;
        formPanel.add(loginButton, formGbc);
        
        // Add to login container
        loginContainer.add(logoLabel);
        loginContainer.add(Box.createVerticalStrut(5));
        loginContainer.add(subtitleLabel);
        loginContainer.add(Box.createVerticalStrut(30));
        loginContainer.add(formPanel);
        loginContainer.add(Box.createVerticalStrut(20));
        loginContainer.add(demoInfo);
        
        loginPanel.add(loginContainer, gbc);
        
        // Login button action
        loginButton.addActionListener(e -> {
            String studentNumber = studentNumberField.getText();
            String password = new String(passwordField.getPassword());
            
            if (authenticateUser(studentNumber, password)) {
                showMainInterface();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", 
                                            "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        add(loginPanel);
    }
    
    private boolean authenticateUser(String studentNumber, String password) {
        // Mock authentication - in real implementation, this would check against database
        if ("2024001".equals(studentNumber) && "password".equals(password)) {
            currentStudent = new Student("2024001", "QUERUBIN", "RIVERA", "IT", "2nd Year");
            return true;
        }
        return false;
    }
    
    private void showMainInterface() {
        remove(loginPanel);
        createMainInterface();
        revalidate();
        repaint();
    }
    
    private void createMainInterface() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_GRAY);
        
        // Create header
        createHeader();
        
        // Create main layout
        JPanel mainLayout = new JPanel(new BorderLayout());
        mainLayout.setBackground(BACKGROUND_GRAY);
        
        // Create sidebar
        createSidebar();
        
        // Create content panel
        createContentPanel();
        
        mainLayout.add(sidebarPanel, BorderLayout.WEST);
        mainLayout.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(mainLayout, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BLUE);
        headerPanel.setBorder(new EmptyBorder(12, 20, 12, 20));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        
        // Left side - title with icon
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftPanel.setBackground(HEADER_BLUE);
        
        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        iconLabel.setForeground(WHITE);
        
        JLabel titleLabel = new JLabel("Grades (FIRST SEMESTER, 2025-2026)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(WHITE);
        
        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);
        
        // Right side - student info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(HEADER_BLUE);
        
        studentInfoLabel = new JLabel(currentStudent.getFirstName() + " " + currentStudent.getLastName());
        studentInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentInfoLabel.setForeground(WHITE);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 255, 255, 50));
        logoutButton.setForeground(WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        
        rightPanel.add(studentInfoLabel);
        rightPanel.add(logoutButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        headerLabel = new JLabel();
        headerLabel.setLayout(new BorderLayout());
        headerLabel.add(headerPanel, BorderLayout.CENTER);
    }
    
    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BLUE);
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        
        String[] menuItems = {
            "🏠 Home",
            "📅 Schedule", 
            "✅ Attendance",
            "💰 Statement of Accounts",
            "📊 Grades",
            "📄 Transcript of Records",
            "📋 Curriculum Checklist",
            "🏥 Medical Record",
            "👤 Personal Details",
            "📝 Journal/Periodical",
            "📚 Downloadables / About SLU"
        };
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], i == 4); // Grades is active
            sidebarPanel.add(menuButton);
        }
    }
    
    private JButton createMenuButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(15, 20, 15, 20));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (active) {
            button.setBackground(new Color(255, 255, 255, 50));
            button.setForeground(WHITE);
        } else {
            button.setBackground(SIDEBAR_BLUE);
            button.setForeground(new Color(255, 255, 255, 200));
        }
        
        button.addActionListener(e -> {
            if (!active && text.contains("Statement of Accounts")) {
                // Navigate to statement of accounts (placeholder)
                JOptionPane.showMessageDialog(this, "Statement of Accounts functionality available in the web version.",
                                            "Information", JOptionPane.INFORMATION_MESSAGE);
            } else if (!active) {
                JOptionPane.showMessageDialog(this, text.substring(2) + " functionality is under development.",
                                            "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        return button;
    }
    
    private void createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create notice panel
        createNoticePanel();
        
        // Create grades table
        createGradesTable();
        
        // Create legend panel
        createLegendPanel();
        
        // Create footer
        createFooter();
        
        // Layout content
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(WHITE);
        topPanel.add(noticeArea, BorderLayout.NORTH);
        
        JScrollPane tableScrollPane = new JScrollPane(gradesTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableScrollPane.setPreferredSize(new Dimension(0, 300));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(WHITE);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        centerPanel.add(legendPanel, BorderLayout.SOUTH);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    private void createNoticePanel() {
        noticeArea = new JTextArea();
        noticeArea.setText("NOTE: Deadline of submission for completion of Students is February 04, 2026. " +
                          "NC due to NFE/INC if not completed, the final grades shall become permanent.");
        noticeArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noticeArea.setBackground(new Color(255, 243, 205));
        noticeArea.setForeground(new Color(133, 100, 4));
        noticeArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        noticeArea.setEditable(false);
        noticeArea.setLineWrap(true);
        noticeArea.setWrapStyleWord(true);
        noticeArea.setPreferredSize(new Dimension(0, 60));
    }
    
    private void createGradesTable() {
        String[] columnNames = {
            "Class Code", "Course Number", "Units", "Prelim Grade", 
            "Midterm Grade", "Tentative Final Grade", "Final Grade", "Weights"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only for students
            }
        };
        
        gradesTable = new JTable(tableModel);
        gradesTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gradesTable.setRowHeight(35);
        gradesTable.setGridColor(new Color(233, 236, 239));
        gradesTable.setSelectionBackground(new Color(227, 242, 253));
        gradesTable.setBackground(WHITE);
        
        // Style table header
        JTableHeader header = gradesTable.getTableHeader();
        header.setBackground(TABLE_HEADER_BLUE);
        header.setForeground(WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 45));
        header.setBorder(BorderFactory.createEmptyBorder());
        
        // Custom cell renderer for grade values
        DefaultTableCellRenderer gradeRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column >= 3 && column <= 6) { // Grade columns
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                    
                    if (value != null && !isSelected) {
                        String gradeStr = value.toString();
                        if (gradeStr.contains("Not Yet Submitted") || gradeStr.equals("-")) {
                            setForeground(Color.GRAY);
                        } else if (gradeStr.matches("\\d+\\.\\d+")) {
                            double grade = Double.parseDouble(gradeStr);
                            setForeground(grade >= 75.0 ? new Color(40, 167, 69) : new Color(220, 53, 69));
                        } else {
                            setForeground(TEXT_DARK);
                        }
                    }
                } else if (column == 2) { // Units column
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
                
                return c;
            }
        };
        
        // Apply renderer to all columns
        for (int i = 0; i < gradesTable.getColumnCount(); i++) {
            gradesTable.getColumnModel().getColumn(i).setCellRenderer(gradeRenderer);
        }
        
        // Set column widths
        int[] columnWidths = {100, 150, 60, 100, 100, 140, 100, 80};
        for (int i = 0; i < columnWidths.length; i++) {
            gradesTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        
        // Populate table with mock data
        populateGradesTable();
    }
    
    private void populateGradesTable() {
        tableModel.setRowCount(0); // Clear existing data
        
        for (Grade grade : gradesList) {
            Object[] rowData = {
                grade.getClassCode(),
                grade.getCourseNumber(),
                grade.getUnits(),
                grade.getPrelimGrade() != null ? String.format("%.1f", grade.getPrelimGrade()) : "Not Yet Submitted",
                grade.getMidtermGrade() != null ? String.format("%.1f", grade.getMidtermGrade()) : "Not Yet Submitted",
                grade.getTentativeFinalGrade() != null ? String.format("%.1f", grade.getTentativeFinalGrade()) : "Not Yet Submitted",
                grade.getFinalGrade() != null ? grade.getFinalGrade() : "Not Yet Submitted",
                grade.getWeights() != null ? grade.getWeights() : "-"
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void createLegendPanel() {
        legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setBackground(new Color(248, 249, 250));
        legendPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        legendPanel.setPreferredSize(new Dimension(0, 180));
        
        JLabel legendTitle = new JLabel("LEGEND:");
        legendTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        legendTitle.setForeground(TEXT_DARK);
        legendTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel legendContent = new JPanel(new GridLayout(2, 4, 20, 8));
        legendContent.setBackground(new Color(248, 249, 250));
        
        // Legend items matching the image
        String[][] legendItems = {
            {"P", "Passed", "HP", "High Pass"},
            {"INC", "Incomplete", "WP", "Withdrawal w/ Permission"},
            {"D", "Dropped", "F", "Failure"},
            {"NC", "No Credit", "NFE", "No Final Examination"}
        };
        
        for (String[] pair : legendItems) {
            JPanel column = new JPanel();
            column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
            column.setBackground(new Color(248, 249, 250));
            
            for (int i = 0; i < pair.length; i += 2) {
                JPanel legendRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
                legendRow.setBackground(new Color(248, 249, 250));
                
                JLabel gradeLabel = new JLabel(pair[i]);
                gradeLabel.setBackground(HEADER_BLUE);
                gradeLabel.setForeground(WHITE);
                gradeLabel.setOpaque(true);
                gradeLabel.setBorder(new EmptyBorder(2, 6, 2, 6));
                gradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
                
                JLabel descLabel = new JLabel(pair[i + 1]);
                descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                descLabel.setForeground(new Color(73, 80, 87));
                
                legendRow.add(gradeLabel);
                legendRow.add(descLabel);
                column.add(legendRow);
            }
            
            legendContent.add(column);
        }
        
        // Add passing grade info
        JPanel passingGradePanel = new JPanel(new GridLayout(2, 2, 20, 5));
        passingGradePanel.setBackground(new Color(248, 249, 250));
        
        JPanel undergradPanel = createGradingPanel("FOR UNDERGRADUATE", "Passing Grade: 75%", "Failure: Below 75%");
        JPanel gradPanel = createGradingPanel("FOR GRADUATE SCHOOL", "Passing Grade: 85%", "Failure: Below 85%");
        
        passingGradePanel.add(undergradPanel);
        passingGradePanel.add(gradPanel);
        
        legendPanel.add(legendTitle);
        legendPanel.add(Box.createVerticalStrut(15));
        legendPanel.add(legendContent);
        legendPanel.add(Box.createVerticalStrut(10));
        legendPanel.add(passingGradePanel);
    }
    
    private JPanel createGradingPanel(String title, String passing, String failure) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 249, 250));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(HEADER_BLUE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel passingLabel = new JLabel(passing);
        passingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        passingLabel.setForeground(new Color(73, 80, 87));
        passingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel failureLabel = new JLabel(failure);
        failureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        failureLabel.setForeground(new Color(73, 80, 87));
        failureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(passingLabel);
        panel.add(failureLabel);
        
        return panel;
    }
    
    private void createFooter() {
        JLabel footerLabel = new JLabel("Copyright © 2025 Saint Louis University Inc. All rights reserved.", JLabel.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        contentPanel.add(footerLabel, BorderLayout.SOUTH);
    }
    
    private void initializeMockData() {
        // Mock data matching the image exactly
        gradesList.add(new Grade("7024", "NSTP-CWTS 1", 3, null, null, null, null, null));
        gradesList.add(new Grade("9454", "GSTS", 3, null, null, null, null, null));
        gradesList.add(new Grade("9455", "GEM1", 3, null, null, null, null, null));
        gradesList.add(new Grade("9456", "CTE 103", 3, null, null, null, null, null));
        gradesList.add(new Grade("9457", "IT 211", 3, null, null, null, null, null));
        gradesList.add(new Grade("9458A", "IT 212", 2, null, null, null, null, null));
        gradesList.add(new Grade("9458B", "IT 212L", 1, null, null, null, null, null));
        gradesList.add(new Grade("9459A", "IT 213", 2, null, null, null, null, null));
        gradesList.add(new Grade("9459B", "IT 213L", 1, null, null, null, null, null));
        gradesList.add(new Grade("9547", "FIT OA", 2, null, null, null, null, null));
    }
    
    private void setupRealTimeUpdates() {
        // Setup timer for real-time grade updates (simulating faculty backend integration)
        realTimeUpdateTimer = new Timer();
        
        // Simulate real-time updates for demonstration
        realTimeUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    // Simulate faculty updating a grade
                    updateGradeFromFaculty("2024001", "7024", "prelimGrade", 85.5);
                });
            }
        }, 5000); // Update after 5 seconds
        
        realTimeUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    updateGradeFromFaculty("2024001", "9454", "prelimGrade", 92.0);
                });
            }
        }, 8000); // Update after 8 seconds
    }
    
    /**
     * Method to be called by faculty backend for real-time grade updates
     * This is the integration point for faculty grade submission
     */
    public void updateGradeFromFaculty(String studentNumber, String classCode, String gradeType, Double gradeValue) {
        if (currentStudent != null && currentStudent.getStudentNumber().equals(studentNumber)) {
            // Find the grade to update
            for (Grade grade : gradesList) {
                if (grade.getClassCode().equals(classCode)) {
                    // Update the specific grade type
                    switch (gradeType.toLowerCase()) {
                        case "prelimgrade":
                            grade.setPrelimGrade(gradeValue);
                            break;
                        case "midtermgrade":
                            grade.setMidtermGrade(gradeValue);
                            break;
                        case "tentativefinalgrade":
                            grade.setTentativeFinalGrade(gradeValue);
                            break;
                        case "finalgrade":
                            grade.setFinalGrade(gradeValue.toString());
                            break;
                    }
                    
                    // Refresh the table display
                    populateGradesTable();
                    
                    // Show notification to student
                    showGradeUpdateNotification(grade.getCourseNumber(), gradeType, gradeValue);
                    break;
                }
            }
        }
    }
    
    private void showGradeUpdateNotification(String courseNumber, String gradeType, Double gradeValue) {
        SwingUtilities.invokeLater(() -> {
            String message = String.format("New grade posted for %s!\n%s: %.1f", 
                                         courseNumber, gradeType.replace("Grade", " Grade"), gradeValue);
            
            JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog(this, "Grade Update");
            
            // Auto-close the dialog after 3 seconds
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> dialog.dispose());
                }
            }, 3000);
            
            dialog.setVisible(true);
        });
    }
    
    private void logout() {
        if (realTimeUpdateTimer != null) {
            realTimeUpdateTimer.cancel();
        }
        
        currentStudent = null;
        remove(mainPanel);
        createLoginInterface();
        revalidate();
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GradesApplication().setVisible(true);
        });
    }
}