package edu.slu.portal.ui.panels;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.service.AccountService;
import edu.slu.accounts.service.PaymentService;
import edu.slu.accounts.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Statement of Accounts panel - Shows payment status, fee breakdown, and payment options
 */
public class StatementOfAccountsPanel extends BasePanel {
    
    private final AccountService accountService;
    private final PaymentService paymentService;
    private JPanel paymentStatusPanel;
    private JTable feeTable;
    private DefaultTableModel tableModel;
    private JPanel paymentButtonsPanel;
    
    public StatementOfAccountsPanel(AuthenticationService authService) {
        super(authService);
        this.accountService = new AccountService();
        this.paymentService = new PaymentService();
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
        mainContent.add(createSectionHeader("Statement of Accounts (FIRST SEMESTER, 2025-2026)", "💰"));
        mainContent.add(Box.createVerticalStrut(20));
        
        // Main layout with left and right panels
        JPanel contentLayout = new JPanel(new BorderLayout(25, 0));
        contentLayout.setBackground(WHITE);
        
        // Left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(WHITE);
        leftPanel.setPreferredSize(new Dimension(600, 0));
        
        // Student info
        leftPanel.add(createStudentInfoSection());
        leftPanel.add(Box.createVerticalStrut(20));
        
        // Payment status
        paymentStatusPanel = createPaymentStatusSection();
        leftPanel.add(paymentStatusPanel);
        leftPanel.add(Box.createVerticalStrut(20));
        
        // Fee breakdown
        leftPanel.add(createFeeBreakdownSection());
        
        // Right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(WHITE);
        rightPanel.setPreferredSize(new Dimension(350, 0));
        
        // Payment channels info
        rightPanel.add(createPaymentInfoSection());
        rightPanel.add(Box.createVerticalStrut(20));
        
        // Payment buttons
        paymentButtonsPanel = createPaymentButtonsSection();
        rightPanel.add(paymentButtonsPanel);
        
        contentLayout.add(leftPanel, BorderLayout.WEST);
        contentLayout.add(rightPanel, BorderLayout.EAST);
        
        mainContent.add(contentLayout);
        
        scrollPane.setViewportView(mainContent);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createStudentInfoSection() {
        Student student = getCurrentStudent();
        if (student == null) return new JPanel();
        
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBackground(WHITE);
        studentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(WHITE);
        
        // Student avatar
        JPanel avatarPanel = new JPanel();
        avatarPanel.setBackground(HEADER_BLUE);
        avatarPanel.setPreferredSize(new Dimension(60, 60));
        avatarPanel.setLayout(new GridBagLayout());
        avatarPanel.setBorder(BorderFactory.createEmptyBorder());
        
        JLabel avatarLabel = new JLabel(student.getInitials());
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        avatarLabel.setForeground(WHITE);
        avatarPanel.add(avatarLabel);
        
        leftPanel.add(avatarPanel);
        leftPanel.add(Box.createHorizontalStrut(15));
        
        // Student details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(WHITE);
        
        JLabel idLabel = new JLabel(student.getStudentNumber() + " | " + student.getCourse() + " " + student.getYearLevel());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(TEXT_GRAY);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel nameLabel = new JLabel(student.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(TEXT_DARK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        detailsPanel.add(idLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(nameLabel);
        
        leftPanel.add(detailsPanel);
        studentPanel.add(leftPanel, BorderLayout.CENTER);
        
        return studentPanel;
    }
    
    private JPanel createPaymentStatusSection() {
        String studentId = authService.getCurrentUserId();
        boolean isPaid = accountService.isPrelimPaid(studentId);
        
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(WHITE);
        
        if (isPaid) {
            // Paid status
            JPanel paidPanel = createInfoPanel("✅", "Payment Complete", 
                "Your prelim payment has been processed successfully. You are permitted to take examinations and view grades.",
                new Color(212, 237, 218));
            statusPanel.add(paidPanel);
        } else {
            // Unpaid status
            JPanel unpaidPanel = createInfoPanel("⚠️", "Payment Required", 
                "You have an outstanding balance for PRELIM. Please settle your payment to view your grades and take the exams.",
                new Color(248, 215, 218));
            statusPanel.add(unpaidPanel);
            
            statusPanel.add(Box.createVerticalStrut(15));
            
            // Payment reminder with teacher illustration
            JPanel reminderPanel = new JPanel(new BorderLayout());
            reminderPanel.setBackground(new Color(255, 248, 220));
            reminderPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 193, 7), 1),
                new EmptyBorder(20, 20, 20, 20)
            ));
            
            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            leftPanel.setBackground(new Color(255, 248, 220));
            
            JLabel teacherIcon = new JLabel("👩‍🏫");
            teacherIcon.setFont(new Font("Segoe UI", Font.PLAIN, 32));
            teacherIcon.setBorder(new EmptyBorder(0, 0, 0, 15));
            
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBackground(new Color(255, 248, 220));
            
            JLabel reminderTitle = new JLabel("REMINDER");
            reminderTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
            reminderTitle.setForeground(new Color(133, 100, 4));
            reminderTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel reminderText = new JLabel("<html>Please settle your payment to view your grades and take the exams.<br>Click on the payment options to proceed.</html>");
            reminderText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            reminderText.setForeground(new Color(133, 100, 4));
            reminderText.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            textPanel.add(reminderTitle);
            textPanel.add(Box.createVerticalStrut(8));
            textPanel.add(reminderText);
            
            leftPanel.add(teacherIcon);
            leftPanel.add(textPanel);
            
            reminderPanel.add(leftPanel, BorderLayout.CENTER);
            statusPanel.add(reminderPanel);
        }
        
        return statusPanel;
    }
    
    private JPanel createFeeBreakdownSection() {
        JPanel breakdownSection = new JPanel();
        breakdownSection.setLayout(new BoxLayout(breakdownSection, BoxLayout.Y_AXIS));
        breakdownSection.setBackground(WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setBackground(WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel iconLabel = new JLabel("📋");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        JLabel titleLabel = new JLabel("Breakdown of fees as of " + currentDate);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_DARK);
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        
        breakdownSection.add(headerPanel);
        
        // Fee table
        String[] columnNames = {"Date", "Description", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        feeTable = new JTable(tableModel);
        feeTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feeTable.setRowHeight(35);
        feeTable.setGridColor(BORDER_GRAY);
        feeTable.setSelectionBackground(LIGHT_BLUE);
        feeTable.setBackground(WHITE);
        
        // Style table header
        feeTable.getTableHeader().setBackground(BACKGROUND_GRAY);
        feeTable.getTableHeader().setForeground(TEXT_DARK);
        feeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        feeTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Custom cell renderer for amounts
        DefaultTableCellRenderer amountRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 2) { // Amount column
                    setHorizontalAlignment(JLabel.RIGHT);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
                
                return c;
            }
        };
        
        feeTable.getColumnModel().getColumn(2).setCellRenderer(amountRenderer);
        
        // Populate table with sample data
        populateFeeTable();
        
        JScrollPane tableScrollPane = new JScrollPane(feeTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
        tableScrollPane.setPreferredSize(new Dimension(0, 200));
        
        breakdownSection.add(tableScrollPane);
        
        return breakdownSection;
    }
    
    private void populateFeeTable() {
        // Sample fee data
        Object[][] feeData = {
            {"Aug 15, 2025", "Tuition Fee", "₱45,000.00"},
            {"Aug 15, 2025", "Miscellaneous Fee", "₱8,500.00"},
            {"Aug 15, 2025", "Laboratory Fee", "₱3,200.00"},
            {"Aug 15, 2025", "Library Fee", "₱800.00"},
            {"Aug 20, 2025", "Late Registration Fee", "₱500.00"},
            {"", "TOTAL AMOUNT DUE", "₱58,000.00"}
        };
        
        for (Object[] row : feeData) {
            tableModel.addRow(row);
        }
    }
    
    private JPanel createPaymentInfoSection() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(WHITE);
        
        JLabel titleLabel = new JLabel("Online Payment Channels");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("<html>Tuition fees can be paid via the available online payment channels below.</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(TEXT_GRAY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        infoPanel.add(titleLabel);
        infoPanel.add(infoLabel);
        
        return infoPanel;
    }
    
    private JPanel createPaymentButtonsSection() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(WHITE);
        
        // Payment method buttons
        Object[][] paymentMethods = {
            {"UnionBank UPay Online", "🏦", new Color(0, 102, 204)},
            {"Dragonpay Payment Gateway", "🐉", new Color(255, 87, 34)},
            {"BPI Online", "💳", new Color(0, 150, 136)},
            {"BDO Online", "🏧", new Color(33, 150, 243)},
            {"BDO Bills Payment", "📄", new Color(63, 81, 181)},
            {"Bukas Tuition Installment", "⛰️", new Color(76, 175, 80)}
        };
        
        for (Object[] method : paymentMethods) {
            JButton paymentButton = createPaymentButton((String)method[0], (String)method[1], (Color)method[2]);
            buttonsPanel.add(paymentButton);
            buttonsPanel.add(Box.createVerticalStrut(10));
        }
        
        return buttonsPanel;
    }
    
    private JButton createPaymentButton(String text, String icon, Color backgroundColor) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(320, 50));
        button.setMaximumSize(new Dimension(320, 50));
        button.setBackground(backgroundColor);
        button.setBorder(new EmptyBorder(12, 15, 12, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        iconLabel.setForeground(WHITE);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(WHITE);
        textLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        button.add(iconLabel, BorderLayout.WEST);
        button.add(textLabel, BorderLayout.CENTER);
        
        button.addActionListener(e -> showPaymentModal(text));
        
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
    
    private void showPaymentModal(String paymentMethod) {
        String studentId = authService.getCurrentUserId();
        
        if (accountService.isPrelimPaid(studentId)) {
            showSuccess("Your prelim payment is already completed!");
            return;
        }
        
        JDialog paymentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                           "Payment - " + paymentMethod, true);
        paymentDialog.setSize(500, 400);
        paymentDialog.setLocationRelativeTo(this);
        
        JPanel dialogContent = new JPanel(new BorderLayout());
        dialogContent.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Header
        JLabel headerLabel = new JLabel("Payment through " + paymentMethod);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(TEXT_DARK);
        
        // Amount section
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        amountPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JLabel amountLabel = new JLabel("AMOUNT TO PAY");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        amountLabel.setForeground(TEXT_GRAY);
        
        JTextField amountField = new JTextField("₱6,500.00");
        amountField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        amountField.setPreferredSize(new Dimension(0, 40));
        amountField.setEditable(false);
        amountField.setBackground(BACKGROUND_GRAY);
        
        amountPanel.add(amountLabel);
        amountPanel.add(Box.createVerticalStrut(8));
        amountPanel.add(amountField);
        
        // Charges note
        JTextArea chargesNote = new JTextArea("Note: There will be a service charge depending on the payment channel selected. Additional fees may apply.");
        chargesNote.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chargesNote.setForeground(TEXT_GRAY);
        chargesNote.setBackground(new Color(255, 248, 220));
        chargesNote.setBorder(new EmptyBorder(15, 15, 15, 15));
        chargesNote.setEditable(false);
        chargesNote.setLineWrap(true);
        chargesNote.setWrapStyleWord(true);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton cancelButton = createStyledButton("Cancel", Color.LIGHT_GRAY, TEXT_DARK);
        cancelButton.addActionListener(e -> paymentDialog.dispose());
        
        JButton proceedButton = createStyledButton("Proceed with Payment", HEADER_BLUE, WHITE);
        proceedButton.addActionListener(e -> {
            paymentDialog.dispose();
            processPayment(paymentMethod);
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(proceedButton);
        
        dialogContent.add(headerLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(amountPanel, BorderLayout.NORTH);
        centerPanel.add(chargesNote, BorderLayout.CENTER);
        
        dialogContent.add(centerPanel, BorderLayout.CENTER);
        dialogContent.add(buttonPanel, BorderLayout.SOUTH);
        
        paymentDialog.add(dialogContent);
        paymentDialog.setVisible(true);
    }
    
    private void processPayment(String paymentMethod) {
        String studentId = authService.getCurrentUserId();
        
        // Show processing dialog
        JDialog processingDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                              "Processing Payment", true);
        processingDialog.setSize(300, 150);
        processingDialog.setLocationRelativeTo(this);
        
        JPanel processingContent = new JPanel(new GridBagLayout());
        processingContent.add(new JLabel("Processing payment..."));
        processingDialog.add(processingContent);
        
        // Simulate payment processing in background
        SwingWorker<Boolean, Void> paymentWorker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Thread.sleep(2000); // Simulate processing time
                return paymentService.processPayment(studentId, 6500.0, paymentMethod);
            }
            
            @Override
            protected void done() {
                processingDialog.dispose();
                try {
                    boolean success = get();
                    if (success) {
                        showSuccess("Payment processed successfully!\n\nYou can now:\n• Take prelim examinations\n• View your grades\n• Access all academic services");
                        refreshPaymentStatus();
                    } else {
                        showError("Payment processing failed. Please try again.");
                    }
                } catch (Exception e) {
                    showError("An error occurred during payment processing.");
                }
            }
        };
        
        paymentWorker.execute();
        processingDialog.setVisible(true);
    }
    
    private void refreshPaymentStatus() {
        // Refresh the payment status section
        remove(paymentStatusPanel);
        paymentStatusPanel = createPaymentStatusSection();
        
        // Find the parent container and update it
        Container parent = getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
    }
}