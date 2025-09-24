package edu.slu.portal.ui;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.model.Student;
import edu.slu.portal.ui.panels.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main Student Portal UI
 * Comprehensive student portal with login, sidebar navigation, and content panels
 */
public class StudentPortalUI extends JFrame {
    
    // Services
    private final AuthenticationService authService;
    
    // UI Components
    private JPanel loginPanel;
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JLabel headerLabel;
    private JLabel studentInfoLabel;
    
    // Content Panels
    private HomePanel homePanel;
    private SchedulePanel schedulePanel;
    private AttendancePanel attendancePanel;
    private StatementOfAccountsPanel accountsPanel;
    private GradesPanel gradesPanel;
    private TranscriptPanel transcriptPanel;
    private CurriculumPanel curriculumPanel;
    private MedicalRecordPanel medicalPanel;
    private PersonalDetailsPanel personalPanel;
    private JournalPanel journalPanel;
    private DownloadablesPanel downloadablesPanel;
    
    // Current state
    private Student currentStudent;
    private String activeMenuItem = "Home";
    
    // Colors and styling
    private final Color HEADER_BLUE = new Color(44, 90, 160);
    private final Color SIDEBAR_BLUE = new Color(30, 61, 111);
    private final Color BACKGROUND_GRAY = new Color(245, 245, 245);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(51, 51, 51);
    private final Color ACTIVE_MENU = new Color(255, 255, 255, 50);
    
    public StudentPortalUI() {
        this.authService = new AuthenticationService();
        initializeApplication();
        createLoginInterface();
    }
    
    private void initializeApplication() {
        setTitle("Saint Louis University - Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(1200, 700));
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
        loginContainer.setPreferredSize(new Dimension(450, 400));
        loginContainer.setLayout(new BoxLayout(loginContainer, BoxLayout.Y_AXIS));
        
        // University logo and title
        JLabel logoLabel = new JLabel("Saint Louis University", JLabel.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        logoLabel.setForeground(HEADER_BLUE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Student Portal System", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 0, 10, 0);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel studentNumberLabel = new JLabel("Student Number:");
        studentNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField studentNumberField = new JTextField("2024001");
        studentNumberField.setPreferredSize(new Dimension(280, 40));
        studentNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPasswordField passwordField = new JPasswordField("password");
        passwordField.setPreferredSize(new Dimension(280, 40));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        JButton loginButton = new JButton("Login to Portal");
        loginButton.setPreferredSize(new Dimension(280, 45));
        loginButton.setBackground(HEADER_BLUE);
        loginButton.setForeground(WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Demo credentials info
        JPanel demoPanel = new JPanel();
        demoPanel.setBackground(new Color(248, 249, 250));
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));
        
        JLabel demoTitle = new JLabel("Demo Credentials:");
        demoTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        demoTitle.setForeground(TEXT_DARK);
        demoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel demo1 = new JLabel("Student 1: 2024001 / password");
        demo1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        demo1.setForeground(Color.GRAY);
        demo1.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel demo2 = new JLabel("Student 2: 2024002 / password");
        demo2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        demo2.setForeground(Color.GRAY);
        demo2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        demoPanel.add(demoTitle);
        demoPanel.add(Box.createVerticalStrut(5));
        demoPanel.add(demo1);
        demoPanel.add(demo2);
        
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
        formGbc.insets = new Insets(20, 0, 10, 0);
        formPanel.add(loginButton, formGbc);
        
        // Add to login container
        loginContainer.add(logoLabel);
        loginContainer.add(Box.createVerticalStrut(8));
        loginContainer.add(subtitleLabel);
        loginContainer.add(Box.createVerticalStrut(30));
        loginContainer.add(formPanel);
        loginContainer.add(Box.createVerticalStrut(20));
        loginContainer.add(demoPanel);
        
        loginPanel.add(loginContainer, gbc);
        
        // Login button action
        ActionListener loginAction = e -> {
            String studentNumber = studentNumberField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (studentNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your student number.", 
                                            "Login Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your password.", 
                                            "Login Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Show loading cursor
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            // Authenticate user
            if (authService.login(studentNumber, password)) {
                currentStudent = authService.getCurrentUser();
                setCursor(Cursor.getDefaultCursor());
                showMainInterface();
            } else {
                setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(this, 
                    "Invalid credentials. Please check your student number and password.", 
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        };
        
        loginButton.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        
        add(loginPanel);
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
        
        // Create content area
        createContentArea();
        
        mainLayout.add(sidebarPanel, BorderLayout.WEST);
        mainLayout.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(mainLayout, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Show home panel by default
        showPanel("Home");
    }
    
    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BLUE);
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        headerPanel.setPreferredSize(new Dimension(0, 65));
        
        // Left side - title with icon
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(HEADER_BLUE);
        
        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        iconLabel.setForeground(WHITE);
        
        JLabel titleLabel = new JLabel("Student Portal - First Semester, 2025-2026");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        
        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);
        
        // Right side - student info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(HEADER_BLUE);
        
        studentInfoLabel = new JLabel(currentStudent.getFullName() + " (" + currentStudent.getStudentNumber() + ")");
        studentInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentInfoLabel.setForeground(WHITE);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 255, 255, 30));
        logoutButton.setForeground(WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100)),
            new EmptyBorder(8, 15, 8, 15)
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
        sidebarPanel.setPreferredSize(new Dimension(280, 0));
        
        // Sidebar title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(SIDEBAR_BLUE);
        titlePanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        titlePanel.setMaximumSize(new Dimension(280, 60));
        
        JLabel sidebarTitle = new JLabel("Navigation Menu");
        sidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sidebarTitle.setForeground(new Color(255, 255, 255, 180));
        titlePanel.add(sidebarTitle);
        
        sidebarPanel.add(titlePanel);
        
        // Menu items
        String[][] menuItems = {
            {"🏠", "Home"},
            {"📅", "Schedule"}, 
            {"✅", "Attendance"},
            {"💰", "Statement of Accounts"},
            {"📊", "Grades"},
            {"📄", "Transcript of Records"},
            {"📋", "Curriculum Checklist"},
            {"🏥", "Medical Record"},
            {"👤", "Personal Details"},
            {"📝", "Journal/Periodical"},
            {"📚", "Downloadables/About ISLU"}
        };
        
        for (String[] item : menuItems) {
            JButton menuButton = createMenuButton(item[0], item[1]);
            sidebarPanel.add(menuButton);
        }
        
        // Add flexible space at bottom
        sidebarPanel.add(Box.createVerticalGlue());
    }
    
    private JButton createMenuButton(String icon, String text) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(280, 55));
        button.setPreferredSize(new Dimension(280, 55));
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        iconLabel.setForeground(WHITE);
        
        // Text
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(new Color(255, 255, 255, 200));
        textLabel.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        button.add(iconLabel, BorderLayout.WEST);
        button.add(textLabel, BorderLayout.CENTER);
        
        // Set initial appearance
        updateMenuButtonAppearance(button, text.equals(activeMenuItem));
        
        button.addActionListener(e -> {
            if (!text.equals(activeMenuItem)) {
                showPanel(text);
            }
        });
        
        return button;
    }
    
    private void updateMenuButtonAppearance(JButton button, boolean active) {
        if (active) {
            button.setBackground(ACTIVE_MENU);
            button.setOpaque(true);
            Component[] components = button.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    comp.setForeground(WHITE);
                }
            }
        } else {
            button.setBackground(SIDEBAR_BLUE);
            button.setOpaque(true);
            Component[] components = button.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getText().length() == 1) { // Icon
                        label.setForeground(WHITE);
                    } else { // Text
                        label.setForeground(new Color(255, 255, 255, 200));
                    }
                }
            }
        }
        
        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!active) {
                    button.setBackground(new Color(255, 255, 255, 30));
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!active) {
                    button.setBackground(SIDEBAR_BLUE);
                }
            }
        });
    }
    
    private void createContentArea() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Initialize all content panels
        homePanel = new HomePanel(authService);
        schedulePanel = new SchedulePanel(authService);
        attendancePanel = new AttendancePanel(authService);
        accountsPanel = new StatementOfAccountsPanel(authService);
        gradesPanel = new GradesPanel(authService);
        transcriptPanel = new TranscriptPanel(authService);
        curriculumPanel = new CurriculumPanel(authService);
        medicalPanel = new MedicalRecordPanel(authService);
        personalPanel = new PersonalDetailsPanel(authService);
        journalPanel = new JournalPanel(authService);
        downloadablesPanel = new DownloadablesPanel(authService);
    }
    
    private void showPanel(String panelName) {
        // Update active menu item
        String oldActive = activeMenuItem;
        activeMenuItem = panelName;
        
        // Update sidebar button appearances
        Component[] sidebarComponents = sidebarPanel.getComponents();
        for (Component comp : sidebarComponents) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                Component[] buttonComponents = button.getComponents();
                for (Component buttonComp : buttonComponents) {
                    if (buttonComp instanceof JLabel) {
                        JLabel label = (JLabel) buttonComp;
                        if (label.getText().length() > 1) { // Text label
                            boolean isActive = label.getText().equals(panelName);
                            updateMenuButtonAppearance(button, isActive);
                            break;
                        }
                    }
                }
            }
        }
        
        // Clear current content
        contentPanel.removeAll();
        
        // Show appropriate panel
        JPanel panelToShow = null;
        switch (panelName) {
            case "Home":
                panelToShow = homePanel;
                break;
            case "Schedule":
                panelToShow = schedulePanel;
                break;
            case "Attendance":
                panelToShow = attendancePanel;
                break;
            case "Statement of Accounts":
                panelToShow = accountsPanel;
                break;
            case "Grades":
                panelToShow = gradesPanel;
                break;
            case "Transcript of Records":
                panelToShow = transcriptPanel;
                break;
            case "Curriculum Checklist":
                panelToShow = curriculumPanel;
                break;
            case "Medical Record":
                panelToShow = medicalPanel;
                break;
            case "Personal Details":
                panelToShow = personalPanel;
                break;
            case "Journal/Periodical":
                panelToShow = journalPanel;
                break;
            case "Downloadables/About ISLU":
                panelToShow = downloadablesPanel;
                break;
            default:
                panelToShow = homePanel;
                break;
        }
        
        if (panelToShow != null) {
            contentPanel.add(panelToShow, BorderLayout.CENTER);
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout Confirmation", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (choice == JOptionPane.YES_OPTION) {
            authService.logout();
            currentStudent = null;
            activeMenuItem = "Home";
            
            remove(mainPanel);
            createLoginInterface();
            revalidate();
            repaint();
        }
    }
    
    public AuthenticationService getAuthService() {
        return authService;
    }
    
    public Student getCurrentStudent() {
        return currentStudent;
    }
}