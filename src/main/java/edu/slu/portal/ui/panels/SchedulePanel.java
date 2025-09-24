package edu.slu.portal.ui.panels;

import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Schedule panel - Shows student class schedule
 */
public class SchedulePanel extends BasePanel {
    
    public SchedulePanel(AuthenticationService authService) {
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
        mainContent.add(createSectionHeader("Class Schedule (FIRST SEMESTER, 2025-2026)", "📅"));
        mainContent.add(Box.createVerticalStrut(20));
        
        // Student info
        Student student = getCurrentStudent();
        if (student != null) {
            JPanel infoPanel = createInfoPanel("👨‍🎓", 
                student.getFullName() + " - " + student.getCourse() + " Year " + student.getYearLevel(),
                "Student Number: " + student.getStudentNumber(),
                LIGHT_BLUE);
            mainContent.add(infoPanel);
            mainContent.add(Box.createVerticalStrut(20));
        }
        
        // Schedule table
        mainContent.add(createScheduleTable());
        
        scrollPane.setViewportView(mainContent);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createScheduleTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        
        String[] columnNames = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable scheduleTable = new JTable(tableModel);
        scheduleTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scheduleTable.setRowHeight(60);
        scheduleTable.setGridColor(BORDER_GRAY);
        scheduleTable.setBackground(WHITE);
        
        // Style header
        scheduleTable.getTableHeader().setBackground(HEADER_BLUE);
        scheduleTable.getTableHeader().setForeground(WHITE);
        scheduleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        scheduleTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Custom cell renderer
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 0) { // Time column
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    setBackground(BACKGROUND_GRAY);
                } else {
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    setBackground(WHITE);
                    
                    if (value != null && !value.toString().isEmpty()) {
                        setBackground(LIGHT_BLUE);
                    }
                }
                
                return c;
            }
        };
        
        for (int i = 0; i < scheduleTable.getColumnCount(); i++) {
            scheduleTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        
        // Sample schedule data
        Object[][] scheduleData = {
            {"7:00-8:00", "", "", "", "", "", ""},
            {"8:00-9:00", "IT 211\nRoom 301", "", "IT 211\nRoom 301", "", "IT 211\nRoom 301", ""},
            {"9:00-10:00", "IT 212\nLab 201", "", "IT 212\nLab 201", "", "IT 212\nLab 201", ""},
            {"10:00-11:00", "", "GSTS\nRoom 205", "", "GSTS\nRoom 205", "", ""},
            {"11:00-12:00", "", "CTE 103\nRoom 102", "", "CTE 103\nRoom 102", "", ""},
            {"12:00-1:00", "LUNCH BREAK", "LUNCH BREAK", "LUNCH BREAK", "LUNCH BREAK", "LUNCH BREAK", ""},
            {"1:00-2:00", "IT 213\nLab 203", "", "IT 213\nLab 203", "", "", ""},
            {"2:00-3:00", "IT 213L\nLab 203", "", "IT 213L\nLab 203", "", "", ""},
            {"3:00-4:00", "", "", "", "", "NSTP-CWTS 1\nRoom 401", ""},
            {"4:00-5:00", "", "", "", "", "FIT OA\nGym", ""}
        };
        
        for (Object[] row : scheduleData) {
            tableModel.addRow(row);
        }
        
        JScrollPane tableScrollPane = new JScrollPane(scheduleTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
        tableScrollPane.setPreferredSize(new Dimension(0, 400));
        
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
}