package edu.slu.portal.ui.panels;

import edu.slu.accounts.service.AuthenticationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Downloadables/About ISLU panel
 */
public class DownloadablesPanel extends BasePanel {
    
    public DownloadablesPanel(AuthenticationService authService) {
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
        mainContent.add(createSectionHeader("Downloadables / About Saint Louis University", "📚"));
        mainContent.add(Box.createVerticalStrut(20));
        
        // About section
        mainContent.add(createAboutSection());
        mainContent.add(Box.createVerticalStrut(25));
        
        // Downloadables section
        mainContent.add(createDownloadablesSection());
        
        scrollPane.setViewportView(mainContent);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createAboutSection() {
        JPanel aboutSection = new JPanel();
        aboutSection.setLayout(new BoxLayout(aboutSection, BoxLayout.Y_AXIS));
        aboutSection.setBackground(WHITE);
        
        JLabel sectionTitle = new JLabel("About Saint Louis University");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_DARK);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        aboutSection.add(sectionTitle);
        
        JPanel aboutCard = createCard(null, createAboutContent());
        aboutSection.add(aboutCard);
        
        return aboutSection;
    }
    
    private JPanel createAboutContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        
        JTextArea aboutText = new JTextArea();
        aboutText.setText("Saint Louis University (SLU) is a private Catholic research university in Baguio, Philippines. " +
                         "Founded in 1911, SLU is one of the oldest and most prestigious universities in the Philippines. " +
                         "The university offers undergraduate and graduate programs across various fields of study.\n\n" +
                         "SLU is known for its commitment to academic excellence, research, and community service. " +
                         "The university has multiple campuses and continues to be a leader in higher education in the Philippines.");
        aboutText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aboutText.setForeground(TEXT_DARK);
        aboutText.setBackground(WHITE);
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        contentPanel.add(aboutText);
        
        return contentPanel;
    }
    
    private JPanel createDownloadablesSection() {
        JPanel downloadSection = new JPanel();
        downloadSection.setLayout(new BoxLayout(downloadSection, BoxLayout.Y_AXIS));
        downloadSection.setBackground(WHITE);
        
        JLabel sectionTitle = new JLabel("Available Downloads");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_DARK);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        downloadSection.add(sectionTitle);
        
        // Download items grid
        JPanel downloadGrid = new JPanel(new GridLayout(0, 2, 15, 15));
        downloadGrid.setBackground(WHITE);
        
        // Sample downloadable items
        downloadGrid.add(createDownloadItem("📄", "Student Handbook", "Complete guide for students", "PDF"));
        downloadGrid.add(createDownloadItem("📋", "Academic Calendar", "Academic year schedule", "PDF"));
        downloadGrid.add(createDownloadItem("📝", "Registration Forms", "Various registration forms", "ZIP"));
        downloadGrid.add(createDownloadItem("🎓", "Graduation Requirements", "Requirements for graduation", "PDF"));
        downloadGrid.add(createDownloadItem("📊", "Grade Report Template", "Template for grade reports", "DOCX"));
        downloadGrid.add(createDownloadItem("🏛️", "University Brochure", "SLU information brochure", "PDF"));
        
        downloadSection.add(downloadGrid);
        
        return downloadSection;
    }
    
    private JPanel createDownloadItem(String icon, String title, String description, String fileType) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(descLabel);
        
        leftPanel.add(iconLabel);
        leftPanel.add(textPanel);
        
        JLabel fileTypeLabel = new JLabel(fileType);
        fileTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        fileTypeLabel.setForeground(WHITE);
        fileTypeLabel.setBackground(HEADER_BLUE);
        fileTypeLabel.setOpaque(true);
        fileTypeLabel.setBorder(new EmptyBorder(3, 8, 3, 8));
        
        item.add(leftPanel, BorderLayout.CENTER);
        item.add(fileTypeLabel, BorderLayout.EAST);
        
        // Click handler
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showWarning("Download functionality is not implemented yet.\nThis would download: " + title);
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(LIGHT_BLUE);
                leftPanel.setBackground(LIGHT_BLUE);
                textPanel.setBackground(LIGHT_BLUE);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(WHITE);
                leftPanel.setBackground(WHITE);
                textPanel.setBackground(WHITE);
            }
        });
        
        return item;
    }
}