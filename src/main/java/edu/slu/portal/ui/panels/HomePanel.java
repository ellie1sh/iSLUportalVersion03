package edu.slu.portal.ui.panels;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.service.AccountService;
import edu.slu.accounts.service.GradeService;
import edu.slu.accounts.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Home panel - Dashboard view for the student portal
 */
public class HomePanel extends BasePanel {
    
    private final AccountService accountService;
    private final GradeService gradeService;
    
    public HomePanel(AuthenticationService authService) {
        super(authService);
        this.accountService = new AccountService();
        this.gradeService = new GradeService();
    }
    
    @Override
    protected void initializePanel() {
        // Main scroll pane
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(WHITE);
        mainContent.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Header
        mainContent.add(createSectionHeader("Dashboard", "🏠"));
        mainContent.add(Box.createVerticalStrut(20));
        
        // Welcome section
        mainContent.add(createWelcomeSection());
        mainContent.add(Box.createVerticalStrut(25));
        
        // Quick stats
        mainContent.add(createQuickStatsSection());
        mainContent.add(Box.createVerticalStrut(25));
        
        // Recent activities
        mainContent.add(createRecentActivitiesSection());
        mainContent.add(Box.createVerticalStrut(25));
        
        // Important notices
        mainContent.add(createNoticesSection());
        
        scrollPane.setViewportView(mainContent);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createWelcomeSection() {
        Student student = getCurrentStudent();
        if (student == null) return new JPanel();
        
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(LIGHT_BLUE);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HEADER_BLUE, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(LIGHT_BLUE);
        
        // Student avatar
        JLabel avatarLabel = new JLabel("👨‍🎓");
        avatarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        avatarLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        leftPanel.add(avatarLabel);
        
        // Welcome text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(LIGHT_BLUE);
        
        JLabel welcomeLabel = new JLabel("Welcome back, " + student.getFirstName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(HEADER_BLUE);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel infoLabel = new JLabel(student.getStudentNumber() + " | " + student.getCourse() + " Year " + student.getYearLevel());
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        infoLabel.setForeground(TEXT_GRAY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        JLabel dateLabel = new JLabel("Today is " + currentDate);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(TEXT_GRAY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(welcomeLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(infoLabel);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(dateLabel);
        
        leftPanel.add(textPanel);
        welcomePanel.add(leftPanel, BorderLayout.CENTER);
        
        return welcomePanel;
    }
    
    private JPanel createQuickStatsSection() {
        JPanel statsSection = new JPanel();
        statsSection.setLayout(new BoxLayout(statsSection, BoxLayout.Y_AXIS));
        statsSection.setBackground(WHITE);
        
        JLabel sectionTitle = new JLabel("Quick Overview");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_DARK);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        statsSection.add(sectionTitle);
        
        // Stats grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        statsGrid.setBackground(WHITE);
        
        // Payment status
        String studentId = authService.getCurrentUserId();
        boolean isPaid = accountService.isPrelimPaid(studentId);
        String paymentStatus = isPaid ? "Paid" : "Unpaid";
        Color paymentColor = isPaid ? SUCCESS_GREEN : DANGER_RED;
        
        JPanel paymentCard = createStatCard("💰", "Payment Status", paymentStatus, paymentColor);
        
        // Grade access
        boolean canViewGrades = gradeService.canViewGrades(studentId);
        String gradeAccess = canViewGrades ? "Available" : "Restricted";
        Color gradeColor = canViewGrades ? SUCCESS_GREEN : WARNING_YELLOW;
        
        JPanel gradeCard = createStatCard("📊", "Grade Access", gradeAccess, gradeColor);
        
        // Attendance
        JPanel attendanceCard = createStatCard("✅", "Attendance", "85%", SUCCESS_GREEN);
        
        // Current semester
        JPanel semesterCard = createStatCard("📅", "Current Term", "1st Semester 2025-2026", HEADER_BLUE);
        
        statsGrid.add(paymentCard);
        statsGrid.add(gradeCard);
        statsGrid.add(attendanceCard);
        statsGrid.add(semesterCard);
        
        statsSection.add(statsGrid);
        
        return statsSection;
    }
    
    private JPanel createStatCard(String icon, String title, String value, Color valueColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setBackground(WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_GRAY);
        
        topPanel.add(iconLabel);
        topPanel.add(titleLabel);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(valueColor);
        valueLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRecentActivitiesSection() {
        JPanel activitiesSection = new JPanel();
        activitiesSection.setLayout(new BoxLayout(activitiesSection, BoxLayout.Y_AXIS));
        activitiesSection.setBackground(WHITE);
        
        JLabel sectionTitle = new JLabel("Recent Activities");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_DARK);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        activitiesSection.add(sectionTitle);
        
        JPanel activitiesCard = createCard(null, createActivitiesList());
        activitiesSection.add(activitiesCard);
        
        return activitiesSection;
    }
    
    private JPanel createActivitiesList() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(WHITE);
        
        // Sample activities
        String[][] activities = {
            {"📊", "Grades updated for NSTP-CWTS 1", "2 hours ago"},
            {"💰", "Payment reminder sent", "1 day ago"},
            {"📅", "Schedule updated for IT 212L", "2 days ago"},
            {"✅", "Attendance recorded for IT 211", "3 days ago"},
            {"📄", "Transcript request processed", "1 week ago"}
        };
        
        for (int i = 0; i < activities.length; i++) {
            JPanel activityItem = createActivityItem(activities[i][0], activities[i][1], activities[i][2]);
            listPanel.add(activityItem);
            
            if (i < activities.length - 1) {
                listPanel.add(Box.createVerticalStrut(12));
            }
        }
        
        return listPanel;
    }
    
    private JPanel createActivityItem(String icon, String description, String time) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(WHITE);
        item.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(WHITE);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_DARK);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(TEXT_GRAY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(descLabel);
        textPanel.add(timeLabel);
        
        leftPanel.add(iconLabel);
        leftPanel.add(textPanel);
        
        item.add(leftPanel, BorderLayout.CENTER);
        
        return item;
    }
    
    private JPanel createNoticesSection() {
        JPanel noticesSection = new JPanel();
        noticesSection.setLayout(new BoxLayout(noticesSection, BoxLayout.Y_AXIS));
        noticesSection.setBackground(WHITE);
        
        JLabel sectionTitle = new JLabel("Important Notices");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_DARK);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        noticesSection.add(sectionTitle);
        
        // Academic notice
        JPanel academicNotice = createInfoPanel("📚", "Academic Reminder", 
            "Deadline for completion of Students is February 04, 2026. NC due to NFE/INC if not completed, the final grades shall become permanent.",
            new Color(255, 243, 205));
        
        // Payment notice
        String studentId = authService.getCurrentUserId();
        if (!accountService.isPrelimPaid(studentId)) {
            JPanel paymentNotice = createInfoPanel("💰", "Payment Required", 
                "You have an outstanding balance for PRELIM. Please settle your payment to view your grades and take the exams.",
                new Color(248, 215, 218));
            
            noticesSection.add(paymentNotice);
            noticesSection.add(Box.createVerticalStrut(15));
        }
        
        noticesSection.add(academicNotice);
        
        return noticesSection;
    }
}