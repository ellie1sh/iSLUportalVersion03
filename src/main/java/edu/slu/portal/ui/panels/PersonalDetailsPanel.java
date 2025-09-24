package edu.slu.portal.ui.panels;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Personal Details panel
 */
public class PersonalDetailsPanel extends BasePanel {
    
    public PersonalDetailsPanel(AuthenticationService authService) {
        super(authService);
    }
    
    @Override
    protected void initializePanel() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(WHITE);
        mainContent.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Header
        mainContent.add(createSectionHeader("Personal Details", "👤"));
        mainContent.add(Box.createVerticalStrut(20));
        
        // Student information
        Student student = getCurrentStudent();
        if (student != null) {
            mainContent.add(createPersonalInfoCard(student));
        }
        
        scrollPane.setViewportView(mainContent);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createPersonalInfoCard(Student student) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        // Student avatar and basic info
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setBackground(WHITE);
        
        // Avatar
        JPanel avatarPanel = new JPanel();
        avatarPanel.setBackground(HEADER_BLUE);
        avatarPanel.setPreferredSize(new Dimension(80, 80));
        avatarPanel.setLayout(new GridBagLayout());
        
        JLabel avatarLabel = new JLabel(student.getInitials());
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        avatarLabel.setForeground(WHITE);
        avatarPanel.add(avatarLabel);
        
        headerPanel.add(avatarPanel);
        headerPanel.add(Box.createHorizontalStrut(20));
        
        // Basic info
        JPanel basicInfoPanel = new JPanel();
        basicInfoPanel.setLayout(new BoxLayout(basicInfoPanel, BoxLayout.Y_AXIS));
        basicInfoPanel.setBackground(WHITE);
        
        JLabel nameLabel = new JLabel(student.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(TEXT_DARK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel studentIdLabel = new JLabel("Student Number: " + student.getStudentNumber());
        studentIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentIdLabel.setForeground(TEXT_GRAY);
        studentIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel courseLabel = new JLabel(student.getCourse() + " - Year " + student.getYearLevel());
        courseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseLabel.setForeground(TEXT_GRAY);
        courseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        basicInfoPanel.add(nameLabel);
        basicInfoPanel.add(Box.createVerticalStrut(5));
        basicInfoPanel.add(studentIdLabel);
        basicInfoPanel.add(courseLabel);
        
        headerPanel.add(basicInfoPanel);
        
        card.add(headerPanel);
        card.add(Box.createVerticalStrut(25));
        
        // Detailed information
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 20, 15));
        detailsPanel.setBackground(WHITE);
        
        // Information fields
        detailsPanel.add(createInfoField("Email Address", student.getEmail() != null ? student.getEmail() : "Not provided"));
        detailsPanel.add(createInfoField("Contact Number", "Not provided"));
        detailsPanel.add(createInfoField("Address", "Not provided"));
        detailsPanel.add(createInfoField("Emergency Contact", "Not provided"));
        detailsPanel.add(createInfoField("Guardian Name", "Not provided"));
        detailsPanel.add(createInfoField("Guardian Contact", "Not provided"));
        
        card.add(detailsPanel);
        
        // Note
        JPanel notePanel = createInfoPanel("ℹ️", "Information Update", 
            "To update your personal information, please contact the Registrar's Office or visit the Student Services Center.",
            new Color(217, 237, 247));
        
        card.add(Box.createVerticalStrut(20));
        card.add(notePanel);
        
        return card;
    }
    
    private JPanel createInfoField(String label, String value) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(WHITE);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelComponent.setForeground(TEXT_GRAY);
        labelComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComponent.setForeground(TEXT_DARK);
        valueComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        fieldPanel.add(labelComponent);
        fieldPanel.add(Box.createVerticalStrut(5));
        fieldPanel.add(valueComponent);
        
        return fieldPanel;
    }
}