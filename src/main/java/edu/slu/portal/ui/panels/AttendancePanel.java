package edu.slu.portal.ui.panels;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Attendance panel - Shows student attendance records
 */
public class AttendancePanel extends BasePanel {
    
    public AttendancePanel(AuthenticationService authService) {
        super(authService);
    }
    
    @Override
    protected void initializePanel() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(WHITE);
        mainContent.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Header
        mainContent.add(createSectionHeader("Attendance Record", "✅"));
        mainContent.add(Box.createVerticalStrut(20));
        
        // Summary cards
        mainContent.add(createAttendanceSummary());
        mainContent.add(Box.createVerticalStrut(20));
        
        // Attendance table
        mainContent.add(createAttendanceTable());
        
        scrollPane.setViewportView(mainContent);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createAttendanceSummary() {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        summaryPanel.setBackground(WHITE);
        
        // Summary cards
        JPanel presentCard = createSummaryCard("Present", "142", SUCCESS_GREEN, "📅");
        JPanel absentCard = createSummaryCard("Absent", "8", DANGER_RED, "❌");
        JPanel lateCard = createSummaryCard("Late", "5", WARNING_YELLOW, "⏰");
        JPanel percentageCard = createSummaryCard("Attendance", "91.6%", HEADER_BLUE, "📊");
        
        summaryPanel.add(presentCard);
        summaryPanel.add(absentCard);
        summaryPanel.add(lateCard);
        summaryPanel.add(percentageCard);
        
        return summaryPanel;
    }
    
    private JPanel createSummaryCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setBackground(WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 8));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_GRAY);
        
        topPanel.add(iconLabel);
        topPanel.add(titleLabel);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createAttendanceTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        
        JLabel tableTitle = new JLabel("Attendance Details by Subject");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(TEXT_DARK);
        tableTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        String[] columnNames = {"Subject", "Total Classes", "Present", "Absent", "Late", "Percentage"};
        
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        attendanceTable.setRowHeight(35);
        attendanceTable.setGridColor(BORDER_GRAY);
        attendanceTable.setBackground(WHITE);
        
        // Style header
        attendanceTable.getTableHeader().setBackground(HEADER_BLUE);
        attendanceTable.getTableHeader().setForeground(WHITE);
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        attendanceTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Custom cell renderer
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                setHorizontalAlignment(column == 0 ? JLabel.LEFT : JLabel.CENTER);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                
                if (column == 5 && value != null) { // Percentage column
                    String percentage = value.toString();
                    if (percentage.contains("%")) {
                        double percent = Double.parseDouble(percentage.replace("%", ""));
                        if (percent >= 90) setForeground(SUCCESS_GREEN);
                        else if (percent >= 75) setForeground(WARNING_YELLOW);
                        else setForeground(DANGER_RED);
                    }
                } else {
                    setForeground(TEXT_DARK);
                }
                
                return c;
            }
        };
        
        for (int i = 0; i < attendanceTable.getColumnCount(); i++) {
            attendanceTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        
        // Sample attendance data
        Object[][] attendanceData = {
            {"NSTP-CWTS 1", "16", "15", "1", "0", "93.8%"},
            {"GSTS", "32", "30", "1", "1", "93.8%"},
            {"GEM1", "32", "28", "2", "2", "87.5%"},
            {"CTE 103", "32", "29", "2", "1", "90.6%"},
            {"IT 211", "48", "45", "1", "2", "93.8%"},
            {"IT 212", "32", "30", "1", "1", "93.8%"},
            {"IT 212L", "16", "16", "0", "0", "100.0%"},
            {"IT 213", "32", "28", "3", "1", "87.5%"},
            {"IT 213L", "16", "15", "1", "0", "93.8%"},
            {"FIT OA", "16", "14", "2", "0", "87.5%"}
        };
        
        for (Object[] row : attendanceData) {
            tableModel.addRow(row);
        }
        
        JScrollPane tableScrollPane = new JScrollPane(attendanceTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
        tableScrollPane.setPreferredSize(new Dimension(0, 300));
        
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
}