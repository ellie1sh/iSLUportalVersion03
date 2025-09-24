package edu.slu.portal.ui.panels;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.service.GradeService;
import edu.slu.grades.Grade;
import edu.slu.grades.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Grades panel - Shows student grades with proper formatting and legend
 */
public class GradesPanel extends BasePanel {
    
    private final GradeService gradeService;
    private JTable gradesTable;
    private DefaultTableModel tableModel;
    private List<Grade> gradesList;
    
    public GradesPanel(AuthenticationService authService) {
        super(authService);
        this.gradeService = new GradeService();
        this.gradesList = new ArrayList<>();
        initializeMockGrades();
    }
    
    @Override
    protected void initializePanel() {
        // Main scroll pane
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(WHITE);
        mainContent.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Header
        mainContent.add(createSectionHeader("Grades (FIRST SEMESTER, 2025-2026)", "📊"));
        mainContent.add(Box.createVerticalStrut(20));
        
        // Check if user can view grades
        String studentId = authService.getCurrentUserId();
        if (!gradeService.canViewGrades(studentId)) {
            mainContent.add(createAccessRestrictedPanel());
        } else {
            // Notice panel
            mainContent.add(createNoticePanel());
            mainContent.add(Box.createVerticalStrut(20));
            
            // Grades table
            mainContent.add(createGradesTablePanel());
            mainContent.add(Box.createVerticalStrut(25));
            
            // Legend
            mainContent.add(createLegendPanel());
        }
        
        scrollPane.setViewportView(mainContent);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createAccessRestrictedPanel() {
        JPanel restrictedPanel = new JPanel(new GridBagLayout());
        restrictedPanel.setBackground(WHITE);
        restrictedPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        
        // Teacher illustration
        JLabel teacherIcon = new JLabel("👩‍🏫");
        teacherIcon.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        teacherIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Grade Access Restricted");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("<html><center>You have an outstanding balance for <strong>PRELIM</strong>.<br><br>Please settle your payment in the Statement of Accounts section<br>to view your grades and take examinations.</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(TEXT_GRAY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton paymentButton = createStyledButton("Go to Statement of Accounts", HEADER_BLUE, WHITE);
        paymentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        paymentButton.addActionListener(e -> {
            // This would typically navigate to the Statement of Accounts panel
            showWarning("Please use the sidebar to navigate to Statement of Accounts.");
        });
        
        contentPanel.add(teacherIcon);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(paymentButton);
        
        restrictedPanel.add(contentPanel);
        
        return restrictedPanel;
    }
    
    private JPanel createNoticePanel() {
        JPanel noticePanel = new JPanel(new BorderLayout());
        noticePanel.setBackground(new Color(255, 243, 205));
        noticePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel noticeLabel = new JLabel("<html><b>NOTE:</b> Deadline of submission for completion of Students is February 04, 2026. NC due to NFE/INC if not completed, the final grades shall become permanent.</html>");
        noticeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noticeLabel.setForeground(new Color(133, 100, 4));
        
        noticePanel.add(noticeLabel, BorderLayout.CENTER);
        
        return noticePanel;
    }
    
    private JPanel createGradesTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        
        // Table setup
        String[] columnNames = {
            "Class Code", "Course Number", "Units", "Prelim Grade", 
            "Midterm Grade", "Tentative Final Grade", "Final Grade", "Weights"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        gradesTable = new JTable(tableModel);
        gradesTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gradesTable.setRowHeight(40);
        gradesTable.setGridColor(BORDER_GRAY);
        gradesTable.setSelectionBackground(LIGHT_BLUE);
        gradesTable.setBackground(WHITE);
        gradesTable.setShowVerticalLines(true);
        gradesTable.setShowHorizontalLines(true);
        
        // Style table header
        JTableHeader header = gradesTable.getTableHeader();
        header.setBackground(HEADER_BLUE);
        header.setForeground(WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 45));
        header.setBorder(BorderFactory.createEmptyBorder());
        
        // Custom cell renderer for different columns
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
                            setForeground(TEXT_GRAY);
                        } else if (gradeStr.matches("\\d+\\.\\d+")) {
                            double grade = Double.parseDouble(gradeStr);
                            setForeground(grade >= 75.0 ? SUCCESS_GREEN : DANGER_RED);
                        } else {
                            setForeground(TEXT_DARK);
                        }
                    }
                } else if (column == 2) { // Units column
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                    setForeground(TEXT_DARK);
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    setForeground(TEXT_DARK);
                }
                
                return c;
            }
        };
        
        // Apply renderer to all columns
        for (int i = 0; i < gradesTable.getColumnCount(); i++) {
            gradesTable.getColumnModel().getColumn(i).setCellRenderer(gradeRenderer);
        }
        
        // Set column widths
        int[] columnWidths = {120, 180, 70, 120, 120, 160, 120, 100};
        for (int i = 0; i < columnWidths.length && i < gradesTable.getColumnCount(); i++) {
            gradesTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        
        // Populate table
        populateGradesTable();
        
        JScrollPane tableScrollPane = new JScrollPane(gradesTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
        tableScrollPane.setPreferredSize(new Dimension(0, 350));
        
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private void populateGradesTable() {
        tableModel.setRowCount(0);
        
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
    
    private JPanel createLegendPanel() {
        JPanel legendSection = new JPanel();
        legendSection.setLayout(new BoxLayout(legendSection, BoxLayout.Y_AXIS));
        legendSection.setBackground(BACKGROUND_GRAY);
        legendSection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel legendTitle = new JLabel("LEGEND:");
        legendTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        legendTitle.setForeground(TEXT_DARK);
        legendTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        legendSection.add(legendTitle);
        legendSection.add(Box.createVerticalStrut(15));
        
        // Legend items grid
        JPanel legendGrid = new JPanel(new GridLayout(2, 4, 20, 12));
        legendGrid.setBackground(BACKGROUND_GRAY);
        
        String[][] legendItems = {
            {"P", "Passed", "HP", "High Pass"},
            {"INC", "Incomplete", "WP", "Withdrawal w/ Permission"},
            {"D", "Dropped", "F", "Failure"},
            {"NC", "No Credit", "NFE", "No Final Examination"}
        };
        
        for (String[] pair : legendItems) {
            JPanel column = new JPanel();
            column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
            column.setBackground(BACKGROUND_GRAY);
            
            for (int i = 0; i < pair.length; i += 2) {
                JPanel legendRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
                legendRow.setBackground(BACKGROUND_GRAY);
                
                JLabel gradeLabel = new JLabel(pair[i]);
                gradeLabel.setBackground(HEADER_BLUE);
                gradeLabel.setForeground(WHITE);
                gradeLabel.setOpaque(true);
                gradeLabel.setBorder(new EmptyBorder(3, 8, 3, 8));
                gradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
                
                JLabel descLabel = new JLabel(pair[i + 1]);
                descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                descLabel.setForeground(TEXT_GRAY);
                
                legendRow.add(gradeLabel);
                legendRow.add(descLabel);
                column.add(legendRow);
            }
            
            legendGrid.add(column);
        }
        
        legendSection.add(legendGrid);
        legendSection.add(Box.createVerticalStrut(15));
        
        // Passing grade information
        JPanel passingGradePanel = new JPanel(new GridLayout(1, 2, 30, 0));
        passingGradePanel.setBackground(BACKGROUND_GRAY);
        
        JPanel undergradPanel = createGradingInfoPanel("FOR UNDERGRADUATE", "Passing Grade: 75%", "Failure: Below 75%");
        JPanel gradPanel = createGradingInfoPanel("FOR GRADUATE SCHOOL", "Passing Grade: 85%", "Failure: Below 85%");
        
        passingGradePanel.add(undergradPanel);
        passingGradePanel.add(gradPanel);
        
        legendSection.add(passingGradePanel);
        
        return legendSection;
    }
    
    private JPanel createGradingInfoPanel(String title, String passing, String failure) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_GRAY);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(HEADER_BLUE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel passingLabel = new JLabel(passing);
        passingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passingLabel.setForeground(TEXT_GRAY);
        passingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel failureLabel = new JLabel(failure);
        failureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        failureLabel.setForeground(TEXT_GRAY);
        failureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(passingLabel);
        panel.add(failureLabel);
        
        return panel;
    }
    
    private void initializeMockGrades() {
        // Mock data matching the original grades system
        gradesList.add(new Grade("7024", "NSTP-CWTS 1", 3, 85.5, null, null, null, null));
        gradesList.add(new Grade("9454", "GSTS", 3, 92.0, null, null, null, null));
        gradesList.add(new Grade("9455", "GEM1", 3, null, null, null, null, null));
        gradesList.add(new Grade("9456", "CTE 103", 3, null, null, null, null, null));
        gradesList.add(new Grade("9457", "IT 211", 3, null, null, null, null, null));
        gradesList.add(new Grade("9458A", "IT 212", 2, null, null, null, null, null));
        gradesList.add(new Grade("9458B", "IT 212L", 1, null, null, null, null, null));
        gradesList.add(new Grade("9459A", "IT 213", 2, null, null, null, null, null));
        gradesList.add(new Grade("9459B", "IT 213L", 1, null, null, null, null, null));
        gradesList.add(new Grade("9547", "FIT OA", 2, null, null, null, null, null));
    }
    
    /**
     * Update grades from external source (like faculty system)
     */
    public void updateGrade(String classCode, String gradeType, Double gradeValue) {
        for (Grade grade : gradesList) {
            if (grade.getClassCode().equals(classCode)) {
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
                
                SwingUtilities.invokeLater(() -> {
                    populateGradesTable();
                    showSuccess("Grade updated for " + grade.getCourseNumber());
                });
                break;
            }
        }
    }
}