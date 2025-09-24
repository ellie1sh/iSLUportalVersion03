package edu.slu.grades;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Faculty Grade Manager Interface
 * Allows faculty to input and update student grades in real-time
 * This is the backend integration point for the grades system
 */
public class FacultyGradeManager extends JFrame {
    
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> classCodeCombo;
    private JComboBox<String> gradeTypeCombo;
    private JTextField gradeValueField;
    private JButton updateGradeButton;
    private List<Student> studentsList;
    private List<Grade> allGrades;
    private GradesApplication studentApp; // Reference to student application for real-time updates
    
    // Colors
    private final Color HEADER_BLUE = new Color(44, 90, 160);
    private final Color SIDEBAR_BLUE = new Color(30, 61, 111);
    private final Color WHITE = Color.WHITE;
    private final Color BACKGROUND_GRAY = new Color(245, 245, 245);
    
    public FacultyGradeManager() {
        initializeFacultyInterface();
        initializeMockData();
        setupComponents();
    }
    
    public FacultyGradeManager(GradesApplication studentApp) {
        this.studentApp = studentApp;
        initializeFacultyInterface();
        initializeMockData();
        setupComponents();
    }
    
    private void initializeFacultyInterface() {
        setTitle("Faculty Grade Management System - Saint Louis University");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        studentsList = new ArrayList<>();
        allGrades = new ArrayList<>();
    }
    
    private void setupComponents() {
        setLayout(new BorderLayout());
        
        // Create header
        createHeader();
        
        // Create main panel
        createMainPanel();
        
        // Create control panel
        createControlPanel();
    }
    
    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BLUE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Faculty Grade Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        
        JLabel subtitleLabel = new JLabel("Real-time Grade Updates for Students");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(HEADER_BLUE);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create students table
        String[] columnNames = {
            "Student Number", "Student Name", "Course", "Year Level", 
            "Class Code", "Course Number", "Prelim", "Midterm", "Tentative Final", "Final Grade"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 6; // Only grade columns are editable
            }
        };
        
        studentsTable = new JTable(tableModel);
        studentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentsTable.setRowHeight(30);
        studentsTable.setGridColor(new Color(233, 236, 239));
        studentsTable.setSelectionBackground(new Color(227, 242, 253));
        
        // Custom cell renderer for grades
        DefaultTableCellRenderer gradeRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column >= 6 && column <= 9) { // Grade columns
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    
                    if (value != null && !isSelected) {
                        String gradeStr = value.toString();
                        if (gradeStr.contains("Not Yet Submitted") || gradeStr.equals("-")) {
                            setForeground(Color.GRAY);
                        } else if (gradeStr.matches("\\d+\\.\\d+")) {
                            double grade = Double.parseDouble(gradeStr);
                            setForeground(grade >= 75.0 ? new Color(40, 167, 69) : new Color(220, 53, 69));
                        }
                    }
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                    setFont(new Font("Segoe UI", Font.PLAIN, 12));
                }
                
                return c;
            }
        };
        
        for (int i = 0; i < studentsTable.getColumnCount(); i++) {
            studentsTable.getColumnModel().getColumn(i).setCellRenderer(gradeRenderer);
        }
        
        // Set column widths
        int[] columnWidths = {100, 150, 80, 80, 80, 120, 80, 80, 100, 100};
        for (int i = 0; i < columnWidths.length && i < studentsTable.getColumnCount(); i++) {
            studentsTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        
        JScrollPane tableScrollPane = new JScrollPane(studentsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Student Grades"));
        
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Populate table
        populateStudentsTable();
    }
    
    private void createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(BACKGROUND_GRAY);
        controlPanel.setBorder(BorderFactory.createTitledBorder("Grade Update Controls"));
        controlPanel.setPreferredSize(new Dimension(300, 0));
        
        // Student selection
        JPanel studentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentPanel.setBackground(BACKGROUND_GRAY);
        studentPanel.add(new JLabel("Student Number:"));
        JTextField studentNumberField = new JTextField("2024001", 10);
        studentPanel.add(studentNumberField);
        
        // Class code selection
        JPanel classPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        classPanel.setBackground(BACKGROUND_GRAY);
        classPanel.add(new JLabel("Class Code:"));
        classCodeCombo = new JComboBox<>(new String[]{"7024", "9454", "9455", "9456", "9457", "9458A", "9458B", "9459A", "9459B", "9547"});
        classPanel.add(classCodeCombo);
        
        // Grade type selection
        JPanel gradeTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gradeTypePanel.setBackground(BACKGROUND_GRAY);
        gradeTypePanel.add(new JLabel("Grade Type:"));
        gradeTypeCombo = new JComboBox<>(new String[]{"prelimGrade", "midtermGrade", "tentativeFinalGrade", "finalGrade"});
        gradeTypePanel.add(gradeTypeCombo);
        
        // Grade value input
        JPanel gradeValuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gradeValuePanel.setBackground(BACKGROUND_GRAY);
        gradeValuePanel.add(new JLabel("Grade Value:"));
        gradeValueField = new JTextField("85.0", 10);
        gradeValuePanel.add(gradeValueField);
        
        // Update button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_GRAY);
        updateGradeButton = new JButton("Update Grade");
        updateGradeButton.setBackground(HEADER_BLUE);
        updateGradeButton.setForeground(WHITE);
        updateGradeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateGradeButton.setPreferredSize(new Dimension(200, 40));
        updateGradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        updateGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudentGrade(
                    studentNumberField.getText(),
                    (String) classCodeCombo.getSelectedItem(),
                    (String) gradeTypeCombo.getSelectedItem(),
                    gradeValueField.getText()
                );
            }
        });
        
        buttonPanel.add(updateGradeButton);
        
        // Instructions
        JTextArea instructionsArea = new JTextArea();
        instructionsArea.setText("Instructions:\n\n" +
                               "1. Select the student number\n" +
                               "2. Choose the class code\n" +
                               "3. Select the grade type\n" +
                               "4. Enter the grade value\n" +
                               "5. Click 'Update Grade'\n\n" +
                               "The student will receive the\n" +
                               "update in real-time!");
        instructionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructionsArea.setBackground(new Color(248, 249, 250));
        instructionsArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        instructionsArea.setEditable(false);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        
        JScrollPane instructionsScroll = new JScrollPane(instructionsArea);
        instructionsScroll.setPreferredSize(new Dimension(0, 150));
        instructionsScroll.setBorder(BorderFactory.createTitledBorder("Instructions"));
        
        // Add components to control panel
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(studentPanel);
        controlPanel.add(classPanel);
        controlPanel.add(gradeTypePanel);
        controlPanel.add(gradeValuePanel);
        controlPanel.add(buttonPanel);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(instructionsScroll);
        controlPanel.add(Box.createVerticalGlue());
        
        add(controlPanel, BorderLayout.EAST);
    }
    
    private void initializeMockData() {
        // Initialize students
        studentsList.add(new Student("2024001", "QUERUBIN", "RIVERA", "IT", "2nd Year"));
        studentsList.add(new Student("2024002", "JOHN", "DOE", "IT", "2nd Year"));
        studentsList.add(new Student("2024003", "JANE", "SMITH", "IT", "2nd Year"));
        
        // Initialize grades for each student
        String[] classCodes = {"7024", "9454", "9455", "9456", "9457", "9458A", "9458B", "9459A", "9459B", "9547"};
        String[] courseNumbers = {"NSTP-CWTS 1", "GSTS", "GEM1", "CTE 103", "IT 211", "IT 212", "IT 212L", "IT 213", "IT 213L", "FIT OA"};
        Integer[] units = {3, 3, 3, 3, 3, 2, 1, 2, 1, 2};
        
        for (Student student : studentsList) {
            for (int i = 0; i < classCodes.length; i++) {
                Grade grade = new Grade(classCodes[i], courseNumbers[i], units[i], null, null, null, null, null);
                grade.setStudentNumber(student.getStudentNumber());
                allGrades.add(grade);
            }
        }
    }
    
    private void populateStudentsTable() {
        tableModel.setRowCount(0);
        
        for (Student student : studentsList) {
            for (Grade grade : allGrades) {
                if (grade.getStudentNumber().equals(student.getStudentNumber())) {
                    Object[] rowData = {
                        student.getStudentNumber(),
                        student.getFullName(),
                        student.getCourse(),
                        student.getYearLevel(),
                        grade.getClassCode(),
                        grade.getCourseNumber(),
                        grade.getPrelimGrade() != null ? String.format("%.1f", grade.getPrelimGrade()) : "Not Yet Submitted",
                        grade.getMidtermGrade() != null ? String.format("%.1f", grade.getMidtermGrade()) : "Not Yet Submitted",
                        grade.getTentativeFinalGrade() != null ? String.format("%.1f", grade.getTentativeFinalGrade()) : "Not Yet Submitted",
                        grade.getFinalGrade() != null ? grade.getFinalGrade() : "Not Yet Submitted"
                    };
                    tableModel.addRow(rowData);
                }
            }
        }
    }
    
    private void updateStudentGrade(String studentNumber, String classCode, String gradeType, String gradeValueStr) {
        try {
            Double gradeValue = null;
            if (!gradeValueStr.trim().isEmpty()) {
                gradeValue = Double.parseDouble(gradeValueStr);
            }
            
            // Find and update the grade
            boolean gradeUpdated = false;
            for (Grade grade : allGrades) {
                if (grade.getStudentNumber().equals(studentNumber) && grade.getClassCode().equals(classCode)) {
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
                            grade.setFinalGrade(gradeValueStr);
                            break;
                    }
                    gradeUpdated = true;
                    break;
                }
            }
            
            if (gradeUpdated) {
                // Refresh the faculty table
                populateStudentsTable();
                
                // Send real-time update to student application
                if (studentApp != null) {
                    studentApp.updateGradeFromFaculty(studentNumber, classCode, gradeType, gradeValue);
                }
                
                // Show success message
                JOptionPane.showMessageDialog(this, 
                    String.format("Grade updated successfully!\nStudent: %s\nClass: %s\n%s: %s", 
                                studentNumber, classCode, gradeType, gradeValueStr),
                    "Grade Updated", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Grade not found for the specified student and class code.",
                    "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric grade value.",
                "Invalid Grade Value", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setStudentApplication(GradesApplication studentApp) {
        this.studentApp = studentApp;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create faculty interface
            FacultyGradeManager facultyManager = new FacultyGradeManager();
            facultyManager.setVisible(true);
            
            // Optionally create student interface as well for testing
            // GradesApplication studentApp = new GradesApplication();
            // facultyManager.setStudentApplication(studentApp);
            // studentApp.setVisible(true);
        });
    }
}