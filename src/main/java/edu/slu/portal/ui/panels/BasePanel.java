package edu.slu.portal.ui.panels;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Base panel class for all content panels in the student portal
 */
public abstract class BasePanel extends JPanel {
    
    protected final AuthenticationService authService;
    
    // Common colors
    protected final Color HEADER_BLUE = new Color(44, 90, 160);
    protected final Color LIGHT_BLUE = new Color(227, 242, 253);
    protected final Color WHITE = Color.WHITE;
    protected final Color TEXT_DARK = new Color(51, 51, 51);
    protected final Color TEXT_GRAY = new Color(108, 117, 125);
    protected final Color BACKGROUND_GRAY = new Color(248, 249, 250);
    protected final Color BORDER_GRAY = new Color(233, 236, 239);
    protected final Color SUCCESS_GREEN = new Color(40, 167, 69);
    protected final Color WARNING_YELLOW = new Color(255, 193, 7);
    protected final Color DANGER_RED = new Color(220, 53, 69);
    
    public BasePanel(AuthenticationService authService) {
        this.authService = authService;
        setLayout(new BorderLayout());
        setBackground(WHITE);
        initializePanel();
    }
    
    /**
     * Initialize the panel - to be implemented by subclasses
     */
    protected abstract void initializePanel();
    
    /**
     * Get the current logged-in student
     */
    protected Student getCurrentStudent() {
        return authService.getCurrentUser();
    }
    
    /**
     * Check if user is logged in
     */
    protected boolean isLoggedIn() {
        return authService.isLoggedIn();
    }
    
    /**
     * Create a section header
     */
    protected JPanel createSectionHeader(String title, String icon) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(WHITE);
        
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            iconLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
            titlePanel.add(iconLabel);
        }
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Add separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_GRAY);
        headerPanel.add(separator, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    /**
     * Create a card panel
     */
    protected JPanel createCard(String title, JComponent content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        if (title != null) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(TEXT_DARK);
            titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
            card.add(titleLabel, BorderLayout.NORTH);
        }
        
        if (content != null) {
            card.add(content, BorderLayout.CENTER);
        }
        
        return card;
    }
    
    /**
     * Create an info panel with icon and text
     */
    protected JPanel createInfoPanel(String icon, String title, String description, Color backgroundColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor.darker(), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(backgroundColor);
        
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            iconLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
            leftPanel.add(iconLabel);
        }
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(backgroundColor);
        
        if (title != null) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            titleLabel.setForeground(TEXT_DARK);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            textPanel.add(titleLabel);
        }
        
        if (description != null) {
            JLabel descLabel = new JLabel("<html>" + description + "</html>");
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            descLabel.setForeground(TEXT_GRAY);
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            textPanel.add(descLabel);
        }
        
        leftPanel.add(textPanel);
        panel.add(leftPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a styled button
     */
    protected JButton createStyledButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    /**
     * Create a loading panel
     */
    protected JPanel createLoadingPanel(String message) {
        JPanel loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.setBackground(WHITE);
        
        JLabel loadingLabel = new JLabel(message != null ? message : "Loading...");
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loadingLabel.setForeground(TEXT_GRAY);
        
        loadingPanel.add(loadingLabel);
        
        return loadingPanel;
    }
    
    /**
     * Create an empty state panel
     */
    protected JPanel createEmptyStatePanel(String icon, String title, String description) {
        JPanel emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.setBackground(WHITE);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(iconLabel);
            contentPanel.add(Box.createVerticalStrut(15));
        }
        
        if (title != null) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(TEXT_DARK);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(titleLabel);
            contentPanel.add(Box.createVerticalStrut(8));
        }
        
        if (description != null) {
            JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            descLabel.setForeground(TEXT_GRAY);
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(descLabel);
        }
        
        emptyPanel.add(contentPanel);
        
        return emptyPanel;
    }
    
    /**
     * Show an error message
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show a success message
     */
    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show a warning message
     */
    protected void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}