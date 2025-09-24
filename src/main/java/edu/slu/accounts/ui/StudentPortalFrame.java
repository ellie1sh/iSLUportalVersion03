package edu.slu.accounts.ui;

import edu.slu.accounts.model.Student;
import edu.slu.accounts.service.AccountService;
import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.service.GradeService;
import edu.slu.accounts.service.PaymentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Main student portal window with sidebar navigation and content area
 */
public class StudentPortalFrame extends JFrame {
    private final AuthenticationService authService;
    private final AccountService accountService;
    private final GradeService gradeService;
    private final PaymentService paymentService;

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public StudentPortalFrame(AuthenticationService authService) {
        super("iSLU Student Portal");
        this.authService = authService;
        this.accountService = new AccountService();
        this.gradeService = new GradeService();
        this.paymentService = new PaymentService();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        root.add(contentPanel, BorderLayout.CENTER);

        // Register panels
        contentPanel.add(buildHomePanel(), "Home");
        contentPanel.add(buildSchedulePanel(), "Schedule");
        contentPanel.add(buildAttendancePanel(), "Attendance");
        contentPanel.add(buildStatementPanel(), "Statement");
        contentPanel.add(buildGradesPanel(), "Grades");
        contentPanel.add(buildTranscriptPanel(), "Transcript");
        contentPanel.add(buildCurriculumPanel(), "Curriculum");
        contentPanel.add(buildMedicalPanel(), "Medical");
        contentPanel.add(buildPersonalPanel(), "Personal");
        contentPanel.add(buildJournalPanel(), "Journal");
        contentPanel.add(buildDownloadablesPanel(), "Downloadables");

        setContentPane(root);
        showCard("Home");
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel title = new JLabel("Saint Louis University - Student Portal");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(title, BorderLayout.WEST);

        if (authService.isLoggedIn()) {
            Student s = authService.getCurrentUser();
            JLabel user = new JLabel(String.format("%s (%s) - %s %d", s.getFullName(), s.getStudentNumber(), s.getCourse(), s.getYearLevel()));
            panel.add(user, BorderLayout.EAST);
        }

        return panel;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1, 0, 0));
        sidebar.setPreferredSize(new Dimension(240, 0));

        sidebar.add(navButton("Home", "Home"));
        sidebar.add(navButton("Schedule", "Schedule"));
        sidebar.add(navButton("Attendance", "Attendance"));
        sidebar.add(navButton("Statement of Accounts", "Statement"));
        sidebar.add(navButton("Grades", "Grades"));
        sidebar.add(navButton("Transcript of Records", "Transcript"));
        sidebar.add(navButton("Curriculum Checklist", "Curriculum"));
        sidebar.add(navButton("Medical Record", "Medical"));
        sidebar.add(navButton("Personal Details", "Personal"));
        sidebar.add(navButton("Journal/Periodical", "Journal"));
        sidebar.add(navButton("Downloadables / About iSLU", "Downloadables"));

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> onLogout());
        sidebar.add(logout);

        return sidebar;
    }

    private JButton navButton(String label, String card) {
        JButton b = new JButton(label);
        b.addActionListener(e -> showCard(card));
        return b;
    }

    private void showCard(String name) {
        cardLayout.show(contentPanel, name);
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setText("Welcome to the iSLU Student Portal. Use the navigation on the left.");
        panel.add(new JScrollPane(ta), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildStatementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);

        String studentId = authService.getCurrentUserId();
        String summary = accountService.getPaymentSummary(studentId);
        String breakdown = accountService.getTransactionBreakdown(studentId);

        area.setText(summary + "\n" + breakdown);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton payBtn = new JButton("Make Payment (Console)…");
        payBtn.setToolTipText("Temporarily uses console input for payment method selection");
        payBtn.addActionListener(e -> {
            // Temporary bridge to console-based payment interaction
            new Thread(() -> paymentService.processPaymentInteractive(studentId, new java.util.Scanner(System.in))).start();
            JOptionPane.showMessageDialog(this,
                "Payment flow opens in console temporarily.\nAfter completing, click Refresh.",
                "Payment",
                JOptionPane.INFORMATION_MESSAGE);
        });
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            String s = accountService.getPaymentSummary(studentId);
            String b = accountService.getTransactionBreakdown(studentId);
            area.setText(s + "\n" + b);
        });
        actions.add(refreshBtn);
        actions.add(payBtn);

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String studentId = authService.getCurrentUserId();

        java.util.List<edu.slu.accounts.model.Grade> grades = gradeService.getStudentGrades(studentId);
        String[] cols = {"Subject", "Subject Name", "Units", "Prelim"};
        Object[][] data;
        if (grades.isEmpty()) {
            data = new Object[][]{{"No access or no grades", "", "", ""}};
        } else {
            data = new Object[grades.size()][4];
            for (int i = 0; i < grades.size(); i++) {
                var g = grades.get(i);
                data[i][0] = g.getSubjectCode();
                data[i][1] = g.getSubjectName();
                data[i][2] = g.getUnits();
                data[i][3] = g.getFormattedPrelimGrade();
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            java.util.List<edu.slu.accounts.model.Grade> list = gradeService.getStudentGrades(studentId);
            model.setRowCount(0);
            if (list.isEmpty()) {
                model.addRow(new Object[]{"No access or no grades", "", "", ""});
            } else {
                for (var g : list) {
                    model.addRow(new Object[]{g.getSubjectCode(), g.getSubjectName(), g.getUnits(), g.getFormattedPrelimGrade()});
                }
            }
        });
        top.add(refresh);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String studentId = authService.getCurrentUserId();
        Object[][] data = LegacyDataUtil.loadSchedule(studentId);
        JTable table = new JTable(new DefaultTableModel(data, LegacyDataUtil.scheduleColumns()) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String studentId = authService.getCurrentUserId();
        Object[][] data = LegacyDataUtil.loadAttendance(studentId);
        JTable table = new JTable(new DefaultTableModel(data, LegacyDataUtil.attendanceColumns()) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildTranscriptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String studentId = authService.getCurrentUserId();
        Object[][] data = LegacyDataUtil.loadTranscript(studentId);
        JTable table = new JTable(new DefaultTableModel(data, LegacyDataUtil.transcriptColumns()) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCurriculumPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setText("✅ BACHELOR OF SCIENCE IN INFORMATION TECHNOLOGY\nFirst Semester Sample Checklist\n- IT 101 Programming Fundamentals\n- IT 102 Data Structures and Algorithms\n- GE 101 Mathematics in the Modern World\n- GE 102 Purposive Communication\n- PE 101 Physical Education");
        panel.add(new JScrollPane(ta), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMedicalPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setText("Medical Record\n- Vaccination: Complete\n- Medical Clearance: Valid\n- Next Appointment: N/A");
        panel.add(new JScrollPane(ta), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildPersonalPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        if (authService.isLoggedIn()) {
            Student s = authService.getCurrentUser();
            ta.setText("Student Number: " + s.getStudentNumber() + "\n" +
                       "Name: " + s.getFullName() + "\n" +
                       "Course/Year: " + s.getCourse() + " " + s.getYearLevel() + "\n" +
                       "Email: " + s.getEmail());
        } else {
            ta.setText("Not logged in");
        }
        panel.add(new JScrollPane(ta), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildJournalPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setText("Journal Indexes\n- SLU Periodical Article Indexes\n- Steps in Accessing the Periodical Article Indexes\n- Contact the Library for access");
        panel.add(new JScrollPane(ta), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDownloadablesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setText("Downloadables / About iSLU\n- Student Handbook (contact registrar)\n- Academic Calendar (contact registrar)\n- About iSLU: Student services and portals");
        panel.add(new JScrollPane(ta), BorderLayout.CENTER);
        return panel;
    }

    private void onLogout() {
        authService.logout();
        dispose();
        LoginFrame login = new LoginFrame();
        login.setVisible(true);
    }
}

