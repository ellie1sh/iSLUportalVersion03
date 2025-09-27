package edu.slu.portal.ui.panels;

import edu.slu.accounts.service.AuthenticationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Medical Record panel
 */
public class MedicalRecordPanel extends BasePanel {
    
    public MedicalRecordPanel(AuthenticationService authService) {
        super(authService);
    }
    
    @Override
    protected void initializePanel() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(WHITE);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        mainContent.add(createSectionHeader("Medical Record", "🏥"));
        mainContent.add(Box.createVerticalStrut(30));
        
        // Coming soon message
        JPanel comingSoonPanel = createEmptyStatePanel("🏥", "Medical Record", 
            "View your medical records and health information here.<br>" +
            "This feature is currently under development and will be available soon.");
        
        mainContent.add(comingSoonPanel);
        
        add(mainContent, BorderLayout.CENTER);
    }
}