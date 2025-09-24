import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class ISLUStudentPortal extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private JPanel footbarPanel;
    private JLabel userNameLabel;
    private JLabel semesterLabel;
    private JTextArea announcementsArea;
    private JTextArea statusArea;
    private JPanel mainCardHolder;
    private CardLayout mainCardLayout;
    private MyDoublyLinkedList<MenuItem> menu;

    // Student data
    private String studentID;
    private String studentName;
    private String semester = "FIRST SEMESTER, 2025-2026";
    private String status = "CURRENTLY ENROLLED THIS FIRST SEMESTER, 2025-2026 IN BSIT 2.";

    public ISLUStudentPortal(String studentID) {
        this.studentID = studentID;
        this.studentName = getStudentNameFromDatabase(studentID);
        
        // Initialize random amounts for each account
        this.amountDue = generateRandomAmountDue();
        this.currentBalance = generateRandom5DigitAmount();
        
        initializeComponents();
        setupLayout(PortalUtils.createHomeSublist());
        loadAnnouncements();
        loadStudentStatus();
    }

    /**
     * Generates a random amount between 0.00 to 24000.00
     */
    private double generateRandom5DigitAmount() {
        // Generate random amount between 0.00 and 24000.00
        return Math.random() * 24000.0;
    }

    /**
     * Generates a random amount due between 0.00 to 7000.00
     */
    private double generateRandomAmountDue() {
        // Generate random amount between 0.00 and 7000.00
        return Math.random() * 7000.0;
    }

    /**
     * Generates random grade between 76-99
     */
    private int generateRandomGrade() {
        return 76 + (int) (Math.random() * 24); // 76 to 99
    }

    /**
     * Generates transcript data with all semesters and random grades
     */
    private Object[][] generateTranscriptData() {
        java.util.List<Object[]> data = new java.util.ArrayList<>();
        
        // FIRST SEMESTER, 2024-2025
        data.add(new Object[]{"FIRST SEMESTER, 2024-2025", "", "", ""});
        data.add(new Object[]{"CFE 101", "GOD'S JOURNEY WITH HIS PEOPLE", generateRandomGrade(), 3});
        data.add(new Object[]{"FIT HW", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (HEALTH AND WELLNESS)", generateRandomGrade(), 2});
        data.add(new Object[]{"GART", "ART APPRECIATION", generateRandomGrade(), 3});
        data.add(new Object[]{"GHIST", "READINGS IN PHILIPPINE HISTORY", generateRandomGrade(), 3});
        data.add(new Object[]{"GSELF", "UNDERSTANDING THE SELF", generateRandomGrade(), 3});
        data.add(new Object[]{"IT 111", "INTRODUCTION TO COMPUTING (LEC)", generateRandomGrade(), 2});
        data.add(new Object[]{"IT 111L", "INTRODUCTION TO COMPUTING (LAB)", generateRandomGrade(), 1});
        data.add(new Object[]{"IT 112", "COMPUTER PROGRAMMING 1 (LEC)", generateRandomGrade(), 2});
        data.add(new Object[]{"IT 112L", "COMPUTER PROGRAMMING 1 (LAB)", generateRandomGrade(), 1});
        data.add(new Object[]{"IT 113", "DISCRETE MATHEMATICS", generateRandomGrade(), 3});
        
        // SECOND SEMESTER, 2024-2025
        data.add(new Object[]{"SECOND SEMESTER, 2024-2025", "", "", ""});
        data.add(new Object[]{"CFE 102", "CHRISTIAN MORALITY IN OUR TIMES", generateRandomGrade(), 3});
        data.add(new Object[]{"FIT CS", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (COMBATIVE SPORTS)", generateRandomGrade(), 2});
        data.add(new Object[]{"GCWORLD", "THE CONTEMPORARY WORLD", generateRandomGrade(), 3});
        data.add(new Object[]{"GMATH", "MATHEMATICS IN THE MODERN WORLD", generateRandomGrade(), 3});
        data.add(new Object[]{"GPCOM", "PURPOSIVE COMMUNICATION", generateRandomGrade(), 3});
        data.add(new Object[]{"IT 121", "INFORMATION SYSTEM FUNDAMENTALS", generateRandomGrade(), 3});
        data.add(new Object[]{"IT 122", "COMPUTER PROGRAMMING 2", generateRandomGrade(), 2});
        data.add(new Object[]{"IT 122L", "COMPUTER PROGRAMMING 2 (LAB)", generateRandomGrade(), 1});
        data.add(new Object[]{"IT 123", "PLATFORM TECHNOLOGIES", generateRandomGrade(), 2});
        data.add(new Object[]{"IT 123L", "PLATFORM TECHNOLOGIES (LAB)", generateRandomGrade(), 1});
        
        // SHORT TERM, 2025
        data.add(new Object[]{"SHORT TERM, 2025", "", "", ""});
        data.add(new Object[]{"GRIZAL", "THE LIFE AND WORKS OF RIZAL", generateRandomGrade(), 3});
        data.add(new Object[]{"IT 131", "COMPUTER ARCHITECTURE", generateRandomGrade(), 2});
        data.add(new Object[]{"IT 131L", "COMPUTER ARCHITECTURE (LAB)", generateRandomGrade(), 1});
        
        return data.toArray(new Object[data.size()][4]);
    }

    private void initializeComponents() {
        setTitle("iSLU Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create main panel
        mainPanel = new JPanel(new BorderLayout());

        // Create header
        JPanel headerPanel = createHeader();

        // Create sidebar
        sidebarPanel = createSidebar();

        footbarPanel = createFooter();
        // Create content panel

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        mainContentPanel.add(contentPanel, BorderLayout.CENTER);
        mainContentPanel.add(footbarPanel, BorderLayout.SOUTH);


        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(13, 37, 73));
        headerPanel.setPreferredSize(new Dimension(0, 55));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JSeparator headerSeparator = new JSeparator();
        headerSeparator.setForeground(new Color(70, 130, 180)); // Blue separator color
        headerSeparator.setBackground(new Color(70, 130, 180));
        headerSeparator.setPreferredSize(new Dimension(0, 2));

        // Logo section
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(new Color(52, 73, 94));
        
        // Load InsideLogo.png image
        JLabel logoLabel = new JLabel();
        try {
            java.net.URL logoUrl = getClass().getResource("/photos/InsideLogo.png");
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image originalImage = originalIcon.getImage();
                // Scale the image to appropriate size (height of about 40px to fit in header)
                Image scaledImage = originalImage.getScaledInstance(-1, 40, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                logoLabel.setIcon(scaledIcon);
            } else {
                // Fallback to text if image not found
                logoLabel.setText("iSLU");
                logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
                logoLabel.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            // Fallback to text if any error occurs
            logoLabel.setText("iSLU");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
            logoLabel.setForeground(Color.WHITE);
        }
        
        logoPanel.add(logoLabel);

        // User info section
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(31, 47, 57));

        userNameLabel = new JLabel(studentName);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userNameLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            // Dispose current student portal window
            dispose();
            // Open login page
            new Login().setVisible(true);
        });

        userPanel.add(userNameLabel);
        userPanel.add(Box.createHorizontalStrut(20));
        userPanel.add(logoutButton);

        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        JPanel headerWithSeparator = new JPanel(new BorderLayout());
        headerWithSeparator.add(headerSeparator, BorderLayout.SOUTH);
        headerWithSeparator.add(headerPanel, BorderLayout.CENTER);

        return headerWithSeparator;
    }

    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);// Match sidebar color
        footerPanel.setPreferredSize(new Dimension(0, 50));
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Add separator line at the top of footer
        JSeparator footerSeparator = new JSeparator();
        footerSeparator.setForeground(new Color(70, 130, 180)); // Blue separator color
        footerSeparator.setBackground(new Color(70, 130, 180));
        footerSeparator.setPreferredSize(new Dimension(0, 2));

        // Footer content
        JLabel copyrightLabel = new JLabel("Copyright © 2021 TMDD - Software Development. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(Color.BLACK);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel footerWithSeparator = new JPanel(new BorderLayout());
        footerWithSeparator.add(footerSeparator, BorderLayout.NORTH);
        footerWithSeparator.add(footerPanel, BorderLayout.CENTER);

        footerPanel.add(copyrightLabel, BorderLayout.CENTER);

        return footerWithSeparator;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(13, 37, 73));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Semester info
        semesterLabel = new JLabel(semester);
        semesterLabel.setForeground(Color.WHITE);
        semesterLabel.setFont(new Font("Arial", Font.BOLD, 12));
        semesterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        semesterLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        sidebar.add(semesterLabel);

        // Navigation menu items
        JFrame frame = new JFrame("Student Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);


        menu = PortalUtils.createIntegratedMenuSystem();
        JPanel mainSideButtonPanel = new JPanel();
        mainSideButtonPanel.setBackground(new Color(13, 37, 73));
        mainSideButtonPanel.setLayout(new GridLayout(0, 1, 0, 0));
        mainSideButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        mainCardLayout = new CardLayout();
        mainCardHolder = new JPanel(mainCardLayout);

        for (MenuItem option : menu) {
            JPanel buttonPanel = getButtonPanel(option);
            mainSideButtonPanel.add(buttonPanel);


            buttonPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mainCardLayout.show(mainCardHolder, option.getName());
                    showContent(option);
                }
            });
        }
        sidebar.add(mainSideButtonPanel, BorderLayout.WEST);

        return sidebar;
    }

    private static JPanel getButtonPanel(MenuItem text) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout()); // Use BorderLayout to align the label

        // Set the panel's background color
        buttonPanel.setBackground(new Color(13, 37, 73));
        buttonPanel.setOpaque(true);

        // Create the top/bottom border
        Border topBottomBorder = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black);

        // Create the padding border (this goes inside the top/bottom border)
        Border padding = new EmptyBorder(10, 10, 10, 10);

        // Combine the borders
        Border finalBorder = BorderFactory.createCompoundBorder(topBottomBorder, padding);
        buttonPanel.setBorder(finalBorder);

        // Create a JLabel for the text
        JLabel buttonLabel = new JLabel(text.getName());
        buttonLabel.setForeground(Color.WHITE);
        buttonLabel.setHorizontalAlignment(SwingConstants.LEFT);
        buttonLabel.setOpaque(false); // Make the label transparent

        // Add the label to the panel. Use BorderLayout.WEST to align it to the left.
        buttonPanel.add(buttonLabel, BorderLayout.WEST);

        return buttonPanel;
    }

    private void setupLayout(MySinglyLinkedList<String> subItem) {
        // Create announcements panel
        JPanel announcementsPanel = createAnnouncementsPanel(subItem);
        loadAnnouncements();
        // Create student status panel
        JPanel statusPanel = createStatusPanel(subItem);
        loadStudentStatus();

        contentPanel.add(announcementsPanel);
        contentPanel.add(statusPanel);
    }

    // Method for the "Grade" sub-panels
    private JPanel createGradesPanel(MySinglyLinkedList<String> subItems) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(subItems.toString(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Convert the LinkedList to a String array for JTable column headers
        String[] columnNames ={"Subject", "Prelim Grade", "Midterm Grade","Tentative Final Grade","Final Grade"};

        // Create an empty data array
        Object[][] data = new Object[0][columnNames.length];

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        JTable table = new JTable(tableModel);
        table.setPreferredSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false); // Disable column reordering
        table.setAutoCreateRowSorter(false); // Disable sorting

        // Populate the table with dummy data and calculate the final grade
        // Note: The grades are now stored as numbers for calculation
        Object[] grades1 = {"Intro to Programming", 90, 92, 91};
        Object[] grades2 = {"Data Structures", 85, 88, 87};
        Object[] grades3 = {"Algorithms", 95, 96, 95};

        // Calculate the average for each set of grades
        double average1 = (double) ((int) grades1[1] + (int) grades1[2] + (int) grades1[3]) / 3.0;
        double average2 = (double) ((int) grades2[1] + (int) grades2[2] + (int) grades2[3]) / 3.0;
        double average3 = (double) ((int) grades3[1] + (int) grades3[2] + (int) grades3[3]) / 3.0;

        // Add the calculated average to the end of each grades array
        // You can format the average to two decimal places
        grades1 = new Object[] {grades1[0], grades1[1], grades1[2], grades1[3], String.format("%.2f", average1)};
        grades2 = new Object[] {grades2[0], grades1[1], grades2[2], grades2[3], String.format("%.2f", average2)};
        grades3 = new Object[] {grades3[0], grades3[1], grades3[2], grades3[3], String.format("%.2f", average3)};

        tableModel.addRow(grades1);
        tableModel.addRow(grades2);
        tableModel.addRow(grades3);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 20, 0, 20));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Method for the Announcements sub-panels

    private JPanel createAnnouncementsPanel(MySinglyLinkedList<String> subItem) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                subItem.getFirst(),
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));
        panel.setBackground(Color.WHITE);

        announcementsArea = new JTextArea();
        announcementsArea.setEditable(false);
        announcementsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        announcementsArea.setLineWrap(true);
        announcementsArea.setWrapStyleWord(true);
        announcementsArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(announcementsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(0, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    // Method for the Status sub-panels
    private JPanel createStatusPanel(MySinglyLinkedList<String> subItem) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                subItem.get(1),
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));
        panel.setBackground(Color.WHITE);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Arial", Font.PLAIN, 12));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(0, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    // Content for announcements
    private void loadAnnouncements() {

        StringBuilder announcements = new StringBuilder();

        announcements.append("[INVITATION FOR FRESHMEN] 29th PSQ - University Elimination Round\n\n");
        announcements.append("Good day, freshmen Louisians!\n\n");
        announcements.append("We are inviting all of you to join and participate in the 29th Philippine Statistics Quiz - SLU Qualifiers, Elimination Round! The competition will be conducted online on September 12, 2025. The top 30 qualifiers from the elimination round will battle it out in the final round in October 2025. The top 2 placers will represent Saint Louis University in the Regional Finals tentatively scheduled in November!\n\n");
        announcements.append("For those currently enrolled in a mathematics course, you may consult with your respective instructors for more details.\n\n");
        announcements.append("For any inquiries, do not hesitate to contact Mr. Clarenz Magsakay via cbmagsakay@slu.edu.ph.\n\n");
        announcements.append("Have a wonderful day!\n");
        announcements.append("─────────────────────────────────────────────────────\n\n");

        announcements.append("[CALL FOR RESPONDENTS] A Research Survey on Assessing the Coffee Production potential of SLU Maryheights Coffee Plantation\n\n");
        announcements.append("We are conducting a research survey to evaluate the coffee production potential of the SLU Maryheights Coffee Plantation located in Bakakeng Norte. This survey focuses on assessing the operational readiness of the plantation and exploring marketing opportunities for its coffee products.\n\n");
        announcements.append("The primary objective of this study is to identify opportunities for developing single-origin coffee from the area, which will promote a sustainable coffee industry in Baguio City.\n\n");
        announcements.append("Sincerely yours,\nMA. ARACELI D. TAMBOL\nSLUHTM Faculty Researcher\n");
        announcements.append("─────────────────────────────────────────────────────\n\n");

        announcements.append("AppliedHE Public & Private University Ranking Survey\n\n");
        announcements.append("Kindly answer the survey. This will take approximately 5 minutes of your time. Thank You!\n");
        announcements.append("─────────────────────────────────────────────────────\n\n");

        announcements.append("ACADEMIC CALENDAR 2025-2026\n");
        announcements.append("View the complete academic calendar for detailed information about important dates and deadlines.");

        announcementsArea.setText(announcements.toString());
        announcementsArea.setCaretPosition(0);
    }
    // Content for student status
    private void loadStudentStatus() {

        String status = "Status:\n" +
                "- " + this.status + "\n\n\n" +
                "Announcement from instructor:\n\n" +
                "Class: 7024-NSTP-CWTS 1\n\n" +
                "Google classroom invite link:\n" +
                "https://classroom.google.com/c/NzkxOTgxNDQ3NTcy?cjc=3hnunus2\n\n" +
                "Instructor: Bullong, Doris K.\n\n" +
                "─────────────────────────────────────────────────────\n\n" +
                "Additional Information:\n" +
                "• Make sure to check your class schedule regularly\n" +
                "• Join the Google Classroom for important updates\n" +
                "• Contact your instructor for any class-related queries\n" +
                "• Keep track of assignment deadlines and exam schedules";

        statusArea.setText(status);
        statusArea.setCaretPosition(0);
    }

    // method for showing different contents
    private void showContent(MenuItem item) {
// Clear current content
        contentPanel.removeAll();
        switch (item.getName()) {
            case "🏠 Home":
                setupLayout(item.getSubItems());
                break;
            case "📚 Journal/Periodical":
                contentPanel.add(createJournalPeriodicalPanel(item.getSubItems()));
                break;
            case "📅 Schedule":
                contentPanel.add(showScheduleContent(item.getSubItems()));
                break;
            case "📌 Attendance":
                contentPanel.add(showAttendanceContent(item.getSubItems()));
                break;
            case "📊 Grades":
                contentPanel.add(createGradesPanel(item.getSubItems()));
                break;
            case "👤 Personal Details":
                showPersonalDetailsContent(item.getSubItems());
                break;
            case "🧮 Statement of Accounts":
                contentPanel.add(createStatementOfAccountsPanel(item.getSubItems()));
                break;
            case "📋 Transcript of Records":
                contentPanel.add(createTranscriptOfRecordsPanel(item.getSubItems()));
                break;
            case "✅ Curriculum Checklist":
                contentPanel.add(createCurriculumChecklistPanel(item.getSubItems()));
                break;
            case "ℹ️ Downloadable/ About iSLU":
                contentPanel.add(createAboutISLUPanel(item.getSubItems()));
                break;
            default:
                // Fallback for any other menu item with a sublist
                showGenericContent(item.getName());
        }
        contentPanel.revalidate();
        contentPanel.repaint();

    }
    // Journal/Periodical Panel
    private JPanel createJournalPeriodicalPanel(MySinglyLinkedList<String> subItems) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        header.setBackground(Color.WHITE);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(520, 28));

        JButton searchButton = new JButton("Search");
        JButton advancedButton = new JButton("Advance Search");

        header.add(searchField);
        header.add(searchButton);
        header.add(advancedButton);

        mainPanel.add(header, BorderLayout.NORTH);

        // Body copy mirroring the reference
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JLabel title1 = new JLabel(subItems.getFirst());
        title1.setFont(new Font("Arial", Font.BOLD, 12));
        body.add(title1);

        JTextArea text1 = new JTextArea("An index is a list of items pulled together for a purpose. Journal indexes (also called bibliographic indexes or bibliographic databases) are lists of journals, organized by discipline, subject, or type of publication.");
        text1.setWrapStyleWord(true);
        text1.setLineWrap(true);
        text1.setEditable(false);
        text1.setBackground(Color.WHITE);
        text1.setBorder(null);
        body.add(text1);
        body.add(Box.createVerticalStrut(10));

        JLabel title2 = new JLabel(subItems.get(1));
        title2.setFont(new Font("Arial", Font.BOLD, 12));
        body.add(title2);

        JTextArea text2 = new JTextArea("One of the Home Library Services that the University Libraries offer is the Periodical Article Indexes where the subscribed print journals are being indexed and can be accessed through an online bibliographic database.\n\nThe Periodical Article Indexes database provides access to periodical articles by subject or author and it can help you find articles about a specific topic.");
        text2.setWrapStyleWord(true);
        text2.setLineWrap(true);
        text2.setEditable(false);
        text2.setBackground(Color.WHITE);
        text2.setBorder(null);
        body.add(text2);
        body.add(Box.createVerticalStrut(10));

        JLabel title3 = new JLabel(subItems.get(2));
        title3.setFont(new Font("Arial", Font.BOLD, 12));
        body.add(title3);

        JTextArea steps = new JTextArea(
            "1. Enter your topic on the search box and click Search\n" +
            "2. You will see the various bibliographic details (i.e. title of the journal, the specific date, volume and issue, and page numbers for the article) that contain your topic.\n" +
            "3. Should you opt to read the full text of the article, you may request it by sending an email to uldir@slu.edu.ph"
        );
        steps.setWrapStyleWord(true);
        steps.setLineWrap(true);
        steps.setEditable(false);
        steps.setBackground(Color.WHITE);
        steps.setBorder(null);
        body.add(steps);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scroll, BorderLayout.CENTER);

        return mainPanel;
    }
    //method for Schedule Content
    private JPanel showScheduleContent(MySinglyLinkedList<String> subItems) {
        JPanel schedulePanel = new JPanel(new BorderLayout());
        schedulePanel.setBorder(BorderFactory.createTitledBorder(subItems.toString()));

        // Header with student and semester info
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel left = new JLabel(studentName + " (" + studentID + ")");
        left.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel right = new JLabel(semester);
        right.setFont(new Font("Arial", Font.PLAIN, 12));
        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        schedulePanel.add(header, BorderLayout.NORTH);

        // Build dynamic schedule from provided course list equivalent
        List<CourseScheduleItem> courses = getSampleCourses();
        int totalUnits = courses.stream().mapToInt(c -> c.units).sum();

        // Time slots (30-minute increments) based on min/max course times
        LocalTime minStart = courses.stream().map(c -> c.startTime).min(LocalTime::compareTo).orElse(LocalTime.of(8, 0));
        LocalTime maxEnd = courses.stream().map(c -> c.endTime).max(LocalTime::compareTo).orElse(LocalTime.of(18, 0));
        minStart = roundDownToHalfHour(minStart);
        maxEnd = roundUpToHalfHour(maxEnd);

        // Column names from subItems
        String[] columnNames = {"","Time", "Monday","Tuesday","Wednesday","Thursday","Friday"};

        // Build data rows
        List<Object[]> rows = new ArrayList<>();
        for (LocalTime slot = minStart; slot.isBefore(maxEnd); slot = slot.plusMinutes(30)) {
            LocalTime next = slot.plusMinutes(30);
            Object[] row = new Object[columnNames.length];
            row[0] = formatTimeRange(slot, next);
            row[1] = courseLabelAtTime(courses, slot, "M");
            row[2] = courseLabelAtTime(courses, slot, "T");
            row[3] = courseLabelAtTime(courses, slot, "W");
            row[4] = courseLabelAtTime(courses, slot, "TH");
            row[5] = courseLabelAtTime(courses, slot, "F");
            row[6] = courseLabelAtTime(courses, slot, "S");
            rows.add(row);
        }

        DefaultTableModel scheduleModel = new DefaultTableModel(rows.toArray(new Object[0][]), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable scheduleTable = new JTable(scheduleModel);
        scheduleTable.setRowHeight(28);
        scheduleTable.getTableHeader().setReorderingAllowed(false);
        scheduleTable.setAutoCreateRowSorter(false);
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        schedulePanel.add(scrollPane, BorderLayout.CENTER);

        // Footer with total units
        JLabel footer = new JLabel("Total Units: " + totalUnits);
        footer.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        schedulePanel.add(footer, BorderLayout.SOUTH);

        return schedulePanel;
    }

    private String courseLabelAtTime(List<CourseScheduleItem> courses, LocalTime slot, String day) {
        for (CourseScheduleItem c : courses) {
            if (c.occursOn(day) && !slot.isBefore(c.startTime) && slot.isBefore(c.endTime)) {
                return c.courseNumber + " (" + c.room + ")";
            }
        }
        return "";
    }

    private static LocalTime roundDownToHalfHour(LocalTime time) {
        int minute = time.getMinute();
        return time.withMinute(minute < 30 ? 0 : 30).withSecond(0).withNano(0);
    }

    private static LocalTime roundUpToHalfHour(LocalTime time) {
        int minute = time.getMinute();
        if (minute == 0 || minute == 30) {
            return time.withSecond(0).withNano(0);
        }
        LocalTime base = time.withSecond(0).withNano(0);
        return minute < 30 ? base.withMinute(30) : base.plusHours(1).withMinute(0);
    }

    private static String formatTimeRange(LocalTime start, LocalTime end) {
        return formatTime(start) + "-" + formatTime(end);
    }

    private static String formatTime(LocalTime t) {
        int hour = t.getHour();
        int minute = t.getMinute();
        String ampm = hour < 12 ? "AM" : "PM";
        int displayHour = hour % 12;
        if (displayHour == 0) displayHour = 12;
        return String.format("%d:%02d %s", displayHour, minute, ampm);
    }

    // Lightweight course model aligned with provided fields
    private static class CourseScheduleItem {
        final String classCode;
        final String courseNumber;
        final String courseDescription;
        final int units;
        final LocalTime startTime;
        final LocalTime endTime;
        final String days;
        final String room;
        final Set<String> daySet;

        CourseScheduleItem(String classCode, String courseNumber, String courseDescription,
                            int units, LocalTime startTime, LocalTime endTime, String days, String room) {
            this.classCode = classCode;
            this.courseNumber = courseNumber;
            this.courseDescription = courseDescription;
            this.units = units;
            this.startTime = startTime;
            this.endTime = endTime;
            this.days = days;
            this.room = room;
            this.daySet = parseDays(days);
        }

        boolean occursOn(String day) {
            return daySet.contains(day);
        }

        private static Set<String> parseDays(String s) {
            Set<String> set = new HashSet<>();
            if (s == null) return set;
            String str = s.trim().toUpperCase();
            // Handle common multi-letter tokens first
            if (str.contains("TH")) {
                set.add("TH");
                str = str.replace("TH", "");
            }
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                switch (ch) {
                    case 'M': set.add("M"); break;
                    case 'T': set.add("T"); break;
                    case 'W': set.add("W"); break;
                    case 'F': set.add("F"); break;
                    case 'S': set.add("S"); break;
                    default: break;
                }
            }
            return set;
        }
    }

    private List<CourseScheduleItem> sampleCourses;

    private List<CourseScheduleItem> getSampleCourses() {
        if (sampleCourses == null) {
            sampleCourses = new ArrayList<>();
            // Sample data adapted from provided list
            sampleCourses.add(new CourseScheduleItem("7024", "NSTP-CWTS 1", "FOUNDATIONS OF SERVICE", 3,
                    LocalTime.of(13, 30), LocalTime.of(14, 30), "MWF", "D906"));
            sampleCourses.add(new CourseScheduleItem("9454", "GSTS", "SCIENCE, TECHNOLOGY, AND SOCIETY", 3,
                    LocalTime.of(9, 30), LocalTime.of(10, 30), "TTHS", "D504"));
            sampleCourses.add(new CourseScheduleItem("9455", "GENVI", "ENVIRONMENTAL SCIENCE", 3,
                    LocalTime.of(9, 30), LocalTime.of(10, 30), "MWF", "D503"));
            sampleCourses.add(new CourseScheduleItem("9456", "CFE 103", "CATHOLIC FOUNDATION OF MISSION", 3,
                    LocalTime.of(13, 30), LocalTime.of(14, 30), "TTHS", "D503"));
            sampleCourses.add(new CourseScheduleItem("9457", "IT 211", "REQUIREMENTS ANALYSIS AND MODELING", 3,
                    LocalTime.of(10, 30), LocalTime.of(11, 30), "MWF", "D511"));
            sampleCourses.add(new CourseScheduleItem("9458A", "IT 212", "DATA STRUCTURES (LEC)", 2,
                    LocalTime.of(14, 30), LocalTime.of(15, 30), "TF", "D513"));
            sampleCourses.add(new CourseScheduleItem("9458B", "IT 212L", "DATA STRUCTURES (LAB)", 1,
                    LocalTime.of(16, 0), LocalTime.of(17, 30), "TF", "D522"));
            sampleCourses.add(new CourseScheduleItem("9459A", "IT 213", "NETWORK FUNDAMENTALS (LEC)", 2,
                    LocalTime.of(8, 30), LocalTime.of(9, 30), "TF", "D513"));
            sampleCourses.add(new CourseScheduleItem("9459B", "IT 213L", "NETWORK FUNDAMENTALS (LAB)", 1,
                    LocalTime.of(11, 30), LocalTime.of(13, 0), "TF", "D528"));
            sampleCourses.add(new CourseScheduleItem("9547", "FIT OA", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (OUTDOOR AND ADVENTURE ACTIVITIES)", 2,
                    LocalTime.of(15, 30), LocalTime.of(17, 30), "TH", "D221"));
        }
        return sampleCourses;
    }
    // method for attendance Content
    private Component showAttendanceContent(MySinglyLinkedList<String> subItems) {
        JPanel attendancePanel = new JPanel(new BorderLayout());
        attendancePanel.setBorder(BorderFactory.createTitledBorder(subItems.toString()));

        String[] columnNames = {"Subject", "Present", "Absent", "Late", "Percentage"};
        Object[][] data = {
                {"NSTP-CWTS 1", "15", "1", "0", "93.75%"},
                {"Programming 2", "14", "2", "1", "87.5%"},
                {"Data Structures", "16", "0", "1", "100%"},
                {"Database Systems", "15", "1", "0", "93.75%"},
                {"Web Development", "13", "2", "2", "81.25%"}
        };

        DefaultTableModel attendanceModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        JTable attendanceTable = new JTable(attendanceModel);
        attendanceTable.setRowHeight(25);
        attendanceTable.getTableHeader().setReorderingAllowed(false); // Disable column reordering
        attendanceTable.setAutoCreateRowSorter(false); // Disable sorting
        JScrollPane scrollPane = new JScrollPane(attendanceTable);

        attendancePanel.add(scrollPane, BorderLayout.CENTER);
        return attendancePanel;
    }
    // method for Personal Details Content
    private void showPersonalDetailsContent(MySinglyLinkedList<String> subItems) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 240, 240));
        
        // Main profile panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Header with "User Profile" title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(13, 37, 73));
        headerPanel.setPreferredSize(new Dimension(0, 50));
        
        JLabel headerIcon = new JLabel("👤");
        headerIcon.setForeground(Color.WHITE);
        headerIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        headerPanel.add(headerIcon);
        
        JLabel headerLabel = new JLabel("User Profile");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        
        profilePanel.add(headerPanel, BorderLayout.NORTH);
        
        // Main content area with two columns
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(Color.WHITE);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left side - Profile picture and action buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(200, 0));
        
        // Profile picture placeholder
        JLabel profilePicture = new JLabel();
        profilePicture.setIcon(createProfilePictureIcon());
        profilePicture.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePicture.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        profilePicture.setPreferredSize(new Dimension(150, 150));
        profilePicture.setMaximumSize(new Dimension(150, 150));
        profilePicture.setMinimumSize(new Dimension(150, 150));
        
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(profilePicture);
        leftPanel.add(Box.createVerticalStrut(20));
        
        // Action buttons - styled to match the design
        JButton personalDetailsBtn = new JButton("Personal Details");
        personalDetailsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        personalDetailsBtn.setPreferredSize(new Dimension(180, 40));
        personalDetailsBtn.setMinimumSize(new Dimension(180, 40));
        personalDetailsBtn.setMaximumSize(new Dimension(180, 40));
        personalDetailsBtn.setBackground(new Color(245, 245, 245));
        personalDetailsBtn.setForeground(new Color(60, 60, 60));
        personalDetailsBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        personalDetailsBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        personalDetailsBtn.setFocusPainted(false);
        personalDetailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton accountInfoBtn = new JButton("Account Info");
        accountInfoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountInfoBtn.setPreferredSize(new Dimension(180, 40));
        accountInfoBtn.setMinimumSize(new Dimension(180, 40));
        accountInfoBtn.setMaximumSize(new Dimension(180, 40));
        accountInfoBtn.setBackground(new Color(245, 245, 245));
        accountInfoBtn.setForeground(new Color(60, 60, 60));
        accountInfoBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        accountInfoBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        accountInfoBtn.setFocusPainted(false);
        accountInfoBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton changePasswordBtn = new JButton("Change Password");
        changePasswordBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePasswordBtn.setPreferredSize(new Dimension(180, 40));
        changePasswordBtn.setMinimumSize(new Dimension(180, 40));
        changePasswordBtn.setMaximumSize(new Dimension(180, 40));
        changePasswordBtn.setBackground(new Color(245, 245, 245));
        changePasswordBtn.setForeground(new Color(60, 60, 60));
        changePasswordBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        changePasswordBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        changePasswordBtn.setFocusPainted(false);
        changePasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listeners
        personalDetailsBtn.addActionListener(e -> {
            // Reset all button styles
            resetButtonStyles(personalDetailsBtn, accountInfoBtn, changePasswordBtn);
            // Highlight current button
            personalDetailsBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            showPersonalDetailsInRightPanel();
        });
        
        accountInfoBtn.addActionListener(e -> {
            // Reset all button styles
            resetButtonStyles(personalDetailsBtn, accountInfoBtn, changePasswordBtn);
            // Highlight current button
            accountInfoBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            showAccountInfo();
        });
        
        changePasswordBtn.addActionListener(e -> {
            // Reset all button styles
            resetButtonStyles(personalDetailsBtn, accountInfoBtn, changePasswordBtn);
            // Highlight current button
            changePasswordBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            showPasswordChangeInRightPanel();
        });
        
        leftPanel.add(personalDetailsBtn);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(accountInfoBtn);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(changePasswordBtn);
        leftPanel.add(Box.createVerticalGlue());
        
        // Right side - All existing Personal Details information
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);

        // Get student info from database
        StudentInfo studentInfo = DataManager.getStudentInfo(studentID);
        String birthday = studentInfo != null ? studentInfo.getDateOfBirth() : "N/A";
        String email = studentID + "@slu.edu.ph";

        // Get profile data from database
        String profileData = DataManager.getStudentProfile(studentID);
        System.out.println("DEBUG: Retrieved profile data for " + studentID + ": " + profileData);
        ProfileData parsedProfile = parseProfileData(profileData);
        System.out.println("DEBUG: Parsed profile - Gender: " + parsedProfile.gender + ", Citizenship: " + parsedProfile.citizenship);

        // GENERAL INFORMATION Section
        JPanel generalPanel = createSectionPanel("GENERAL INFORMATION", new Object[][]{
            {"Gender:", parsedProfile.gender, false, "combo", new String[]{"Male", "Female"}},
            {"Birthday:", birthday, false, "text", null},
            {"Citizenship:", parsedProfile.citizenship, false, "text", null},
            {"Religion:", parsedProfile.religion, false, "text", null},
            {"Civil Status:", parsedProfile.civilStatus, false, "text", null},
            {"Birthplace:", parsedProfile.birthplace, false, "text", null},
            {"Nationality:", parsedProfile.nationality, false, "text", null}
        });

        // CONTACT INFORMATION Section
        JPanel contactPanel = createSectionPanel("CONTACT INFORMATION", new Object[][]{
            {"Home Address:", parsedProfile.homeAddress, false, "text", null},
            {"Home Telephone No:", parsedProfile.homeTel, false, "text", null},
            {"Baguio Address:", parsedProfile.baguioAddress, false, "text", null},
            {"Baguio Telephone No:", parsedProfile.baguioTel, false, "text", null},
            {"Cellphone No:", parsedProfile.cellphone, false, "text", null},
            {"Email Address:", email, false, "text", null}
        });

        // CONTACT PERSONS Section
        JPanel contactPersonsPanel = createSectionPanel("CONTACT PERSONS", new Object[][]{
            {"Father's Name:", parsedProfile.fatherName, false, "text", null},
            {"Father's Occupation:", parsedProfile.fatherOcc, false, "text", null},
            {"Mother's Maiden Name:", parsedProfile.motherName, false, "text", null},
            {"Mother's Occupation:", parsedProfile.motherOcc, false, "text", null},
            {"Guardian Name:", parsedProfile.guardianName, false, "text", null},
            {"Guardian Address:", parsedProfile.guardianAddress, false, "text", null}
        });

        // Add all sections to right panel
        rightPanel.add(generalPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(contactPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(contactPersonsPanel);

        // Add note at the bottom
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notePanel.setBackground(Color.WHITE);
        JLabel noteLabel = new JLabel("NOTE: For corrections please email records@slu.edu.ph");
        noteLabel.setForeground(Color.RED);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        notePanel.add(noteLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(notePanel);

        // Add scroll pane to right panel
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setBorder(null);

        mainContentPanel.add(leftPanel, BorderLayout.WEST);
        mainContentPanel.add(rightScrollPane, BorderLayout.CENTER);
        
        profilePanel.add(mainContentPanel, BorderLayout.CENTER);
        contentPanel.add(profilePanel, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * Creates a profile picture icon (gray silhouette)
     */
    private ImageIcon createProfilePictureIcon() {
        // Create a simple gray circle as profile picture placeholder
        int size = 150;
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fill with light gray
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillOval(0, 0, size, size);
        
        // Draw a simple person silhouette
        g2d.setColor(new Color(150, 150, 150));
        g2d.fillOval(size/4, size/4, size/2, size/2); // Head
        g2d.fillOval(size/3, size*2/3, size/3, size/3); // Body
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Shows the password change form
     */
    private void showPasswordChangeForm() {
        JDialog passwordDialog = new JDialog(this, "Change Password", true);
        passwordDialog.setSize(400, 300);
        passwordDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Old Password
        JPanel oldPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        oldPassPanel.add(new JLabel("Old Password:"));
        JPasswordField oldPasswordField = new JPasswordField(20);
        oldPassPanel.add(oldPasswordField);
        formPanel.add(oldPassPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        // New Password
        JPanel newPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newPassPanel.add(new JLabel("New Password:"));
        JPasswordField newPasswordField = new JPasswordField(20);
        newPassPanel.add(newPasswordField);
        formPanel.add(newPassPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Retype Password
        JPanel retypePassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        retypePassPanel.add(new JLabel("Retype Password:"));
        JPasswordField retypePasswordField = new JPasswordField(20);
        retypePassPanel.add(retypePasswordField);
        formPanel.add(retypePassPanel);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("💾 Save");
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        
        JButton cancelButton = new JButton("❌ Cancel");
        cancelButton.setBackground(new Color(220, 220, 220));
        
        saveButton.addActionListener(e -> {
            String oldPass = new String(oldPasswordField.getPassword());
            String newPass = new String(newPasswordField.getPassword());
            String retypePass = new String(retypePasswordField.getPassword());
            
            if (oldPass.isEmpty() || newPass.isEmpty() || retypePass.isEmpty()) {
                JOptionPane.showMessageDialog(passwordDialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!newPass.equals(retypePass)) {
                JOptionPane.showMessageDialog(passwordDialog, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update password using DataManager
            if (DataManager.updateStudentPassword(studentID, newPass)) {
                JOptionPane.showMessageDialog(passwordDialog, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                passwordDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(passwordDialog, "Failed to update password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> passwordDialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        passwordDialog.add(mainPanel);
        passwordDialog.setVisible(true);
    }
    
    /**
     * Shows the Account Information in the right panel
     */
    private void showAccountInfo() {
        // Find the main content panel and update only the right side
        JPanel mainContentPanel = (JPanel) contentPanel.getComponent(0);
        
        // Remove existing right panel if it exists
        if (mainContentPanel.getComponentCount() > 1) {
            mainContentPanel.remove(1);
        }
        
        // Create new right panel with account information
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Account Information title
        JLabel accountTitle = new JLabel("ACCOUNT INFORMATION");
        accountTitle.setFont(new Font("Arial", Font.BOLD, 16));
        accountTitle.setForeground(new Color(13, 37, 73));
        accountTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        rightPanel.add(accountTitle);
        
        // Account details panel - clean table format
        JPanel accountDetailsPanel = new JPanel();
        accountDetailsPanel.setLayout(new BoxLayout(accountDetailsPanel, BoxLayout.Y_AXIS));
        accountDetailsPanel.setBackground(Color.WHITE);
        accountDetailsPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        accountDetailsPanel.setPreferredSize(new Dimension(400, 200));
        
        // Get student info from database
        StudentInfo studentInfo = DataManager.getStudentInfo(studentID);
        String accountName = studentInfo != null ? studentInfo.getFullName() : "Unknown";
        
        // Add account details with clean table format
        addCleanAccountInfoRow(accountDetailsPanel, "User ID/Login ID:", studentID);
        addCleanAccountInfoRow(accountDetailsPanel, "Account Name:", accountName);
        addCleanAccountInfoRow(accountDetailsPanel, "Date Registered:", "August 01, 2024");
        addCleanAccountInfoRow(accountDetailsPanel, "Account Type:", "Student");
        
        rightPanel.add(accountDetailsPanel);
        
        // Add the new right panel to the main content panel
        mainContentPanel.add(rightPanel, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * Resets all button styles to default
     */
    private void resetButtonStyles(JButton btn1, JButton btn2, JButton btn3) {
        JButton[] buttons = {btn1, btn2, btn3};
        for (JButton btn : buttons) {
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
        }
    }
    
    /**
     * Shows the Password Change form in the right panel
     */
    private void showPasswordChangeInRightPanel() {
        // Find the main content panel and update only the right side
        JPanel mainContentPanel = (JPanel) contentPanel.getComponent(0);
        
        // Remove existing right panel if it exists
        if (mainContentPanel.getComponentCount() > 1) {
            mainContentPanel.remove(1);
        }
        
        // Create new right panel with password change form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Password Change title
        JLabel passwordTitle = new JLabel("CHANGE PASSWORD");
        passwordTitle.setFont(new Font("Arial", Font.BOLD, 16));
        passwordTitle.setForeground(new Color(13, 37, 73));
        passwordTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        rightPanel.add(passwordTitle);
        
        // Password form panel
        JPanel passwordFormPanel = new JPanel();
        passwordFormPanel.setLayout(new BoxLayout(passwordFormPanel, BoxLayout.Y_AXIS));
        passwordFormPanel.setBackground(Color.WHITE);
        passwordFormPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Add password fields
        addPasswordField(passwordFormPanel, "Old Password:");
        addPasswordField(passwordFormPanel, "New Password:");
        addPasswordField(passwordFormPanel, "Re-Type Password:");
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(13, 37, 73));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.setPreferredSize(new Dimension(80, 30));
        saveButton.setFocusPainted(false);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(200, 200, 200));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setFocusPainted(false);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        passwordFormPanel.add(buttonPanel);
        rightPanel.add(passwordFormPanel);
        
        // Add the new right panel to the main content panel
        mainContentPanel.add(rightPanel, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * Adds a password field to the panel
     */
    private void addPasswordField(JPanel panel, String label) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        labelComponent.setForeground(new Color(60, 60, 60));
        labelComponent.setPreferredSize(new Dimension(150, 25));
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setPreferredSize(new Dimension(200, 25));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        fieldPanel.add(labelComponent, BorderLayout.WEST);
        fieldPanel.add(passwordField, BorderLayout.CENTER);
        
        // Add separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        
        panel.add(fieldPanel);
        panel.add(separator);
    }
    
    /**
     * Shows the Personal Details in the right panel while keeping left panel intact
     */
    private void showPersonalDetailsInRightPanel() {
        // Find the main content panel and update only the right side
        JPanel mainContentPanel = (JPanel) contentPanel.getComponent(0);
        
        // Remove existing right panel if it exists
        if (mainContentPanel.getComponentCount() > 1) {
            mainContentPanel.remove(1);
        }
        
        // Create new right panel with personal details
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create scrollable content for personal details
        JPanel personalDetailsContent = new JPanel();
        personalDetailsContent.setLayout(new BoxLayout(personalDetailsContent, BoxLayout.Y_AXIS));
        personalDetailsContent.setBackground(Color.WHITE);
        
        // Add all the existing personal details sections
        addPersonalDetailsSections(personalDetailsContent);
        
        // Create scroll pane for the content
        JScrollPane rightScrollPane = new JScrollPane(personalDetailsContent);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setBorder(null);
        rightScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Add the scroll pane to the right panel
        rightPanel.add(rightScrollPane);
        
        // Add the new right panel to the main content panel
        mainContentPanel.add(rightPanel, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * Adds all personal details sections to the panel
     */
    private void addPersonalDetailsSections(JPanel parentPanel) {
        // Get student profile data
        String profileString = DataManager.getStudentProfile(studentID);
        ProfileData profileData = parseProfileData(profileString);
        
        // General Information section
        Object[][] generalData = {
            {"Gender", profileData != null ? profileData.getGender() : "Not specified"},
            {"Citizenship", profileData != null ? profileData.getCitizenship() : "Not specified"},
            {"Religion", profileData != null ? profileData.getReligion() : "Not specified"},
            {"Civil Status", profileData != null ? profileData.getCivilStatus() : "Not specified"},
            {"Birthplace", profileData != null ? profileData.getBirthplace() : "Not specified"},
            {"Nationality", profileData != null ? profileData.getNationality() : "Not specified"}
        };
        parentPanel.add(createSectionPanel("General Information", generalData));
        parentPanel.add(Box.createVerticalStrut(20));
        
        // Contact Information section
        Object[][] contactData = {
            {"Home Address", profileData != null ? profileData.getHomeAddress() : "Not specified"},
            {"Home Telephone", profileData != null ? profileData.getHomeTel() : "Not specified"},
            {"Baguio Address", profileData != null ? profileData.getBaguioAddress() : "Not specified"},
            {"Baguio Telephone", profileData != null ? profileData.getBaguioTel() : "Not specified"},
            {"Cellphone", profileData != null ? profileData.getCellphone() : "Not specified"}
        };
        parentPanel.add(createSectionPanel("Contact Information", contactData));
        parentPanel.add(Box.createVerticalStrut(20));
        
        // Contact Persons section
        Object[][] contactPersonsData = {
            {"Father's Name", profileData != null ? profileData.getFatherName() : "Not specified"},
            {"Father's Occupation", profileData != null ? profileData.getFatherOcc() : "Not specified"},
            {"Mother's Name", profileData != null ? profileData.getMotherName() : "Not specified"},
            {"Mother's Occupation", profileData != null ? profileData.getMotherOcc() : "Not specified"},
            {"Guardian's Name", profileData != null ? profileData.getGuardianName() : "Not specified"},
            {"Guardian's Address", profileData != null ? profileData.getGuardianAddress() : "Not specified"}
        };
        parentPanel.add(createSectionPanel("Contact Persons", contactPersonsData));
    }
    
    /**
     * Adds an account information row to the panel
     */
    private void addAccountInfoRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        labelComponent.setForeground(new Color(60, 60, 60));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 12));
        valueComponent.setForeground(new Color(40, 40, 40));
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.EAST);
        
        // Add separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        
        panel.add(rowPanel);
        panel.add(separator);
    }
    
    /**
     * Adds a compact account information row to the panel
     */
    private void addCompactAccountInfoRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8)); // Very small padding
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 8)); // Very small font
        labelComponent.setForeground(new Color(60, 60, 60));
        labelComponent.setPreferredSize(new Dimension(80, 12)); // Very small fixed width and height
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 8)); // Very small font
        valueComponent.setForeground(new Color(40, 40, 40));
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.CENTER);
        
        // Add separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        
        panel.add(rowPanel);
        panel.add(separator);
    }
    
    /**
     * Adds a clean account information row to the panel (matches image format)
     */
    private void addCleanAccountInfoRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20)); // Proper padding
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.PLAIN, 12)); // Regular font, not bold
        labelComponent.setForeground(new Color(60, 60, 60));
        labelComponent.setPreferredSize(new Dimension(150, 20)); // Fixed width for alignment
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 12)); // Regular font
        valueComponent.setForeground(new Color(40, 40, 40));
        valueComponent.setHorizontalAlignment(SwingConstants.RIGHT); // Right align values
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.EAST);
        
        // Add separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        
        panel.add(rowPanel);
        panel.add(separator);
    }
    

    private JPanel createSectionPanel(String title, Object[][] data) {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Section title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(10, 45, 90));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        sectionPanel.add(titleLabel, BorderLayout.NORTH);

        // Data panel
        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < data.length; i++) {
            String label = (String) data[i][0];
            String value = (String) data[i][1];
            boolean editable = (Boolean) data[i][2];
            String fieldType = (String) data[i][3];
            String[] options = (String[]) data[i][4];

            // Label
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            JLabel labelComponent = new JLabel(label);
            labelComponent.setFont(new Font("Arial", Font.PLAIN, 12));
            labelComponent.setPreferredSize(new Dimension(200, 25));
            dataPanel.add(labelComponent, gbc);

            // Value component
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JPanel valuePanel = new JPanel(new BorderLayout());
            valuePanel.setBackground(Color.WHITE);
            
            Component valueComponent;
            if (fieldType.equals("combo") && editable) {
                JComboBox<String> comboBox = new JComboBox<>(options);
                comboBox.setSelectedItem(value.equals("None") ? null : value);
                comboBox.setFont(new Font("Arial", Font.PLAIN, 12));
                comboBox.setPreferredSize(new Dimension(250, 25));
                valueComponent = comboBox;
            } else {
                JTextField textField = new JTextField(value);
                textField.setFont(new Font("Arial", Font.PLAIN, 12));
                textField.setPreferredSize(new Dimension(250, 25));
                textField.setEditable(editable);
                if (!editable) {
                    textField.setBackground(new Color(240, 240, 240));
                }
                valueComponent = textField;
            }
            
            valuePanel.add(valueComponent, BorderLayout.WEST);
            
            // Edit button (pen icon)
            if (editable) {
                JButton editButton = new JButton("✏️");
                editButton.setFont(new Font("Arial", Font.PLAIN, 12));
                editButton.setPreferredSize(new Dimension(30, 25));
                editButton.setToolTipText("Edit " + label);
                editButton.addActionListener(e -> showEditDialog(label, valueComponent, fieldType, options));
                valuePanel.add(editButton, BorderLayout.EAST);
            }
            
            dataPanel.add(valuePanel, gbc);
        }

        sectionPanel.add(dataPanel, BorderLayout.CENTER);
        return sectionPanel;
    }

    private void showEditDialog(String fieldName, Component component, String fieldType, String[] options) {
        JDialog editDialog = new JDialog(this, "Edit " + fieldName, true);
        editDialog.setSize(400, 200);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel headerLabel = new JLabel("Edit " + fieldName);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        
        editDialog.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        JLabel instructionLabel = new JLabel("Enter new value for " + fieldName + ":");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createVerticalStrut(15));

        Component inputComponent;
        if (fieldType.equals("combo")) {
            JComboBox<String> comboBox = new JComboBox<>(options);
            comboBox.setFont(new Font("Arial", Font.PLAIN, 12));
            comboBox.setPreferredSize(new Dimension(300, 30));
            inputComponent = comboBox;
        } else {
            JTextField textField = new JTextField();
            textField.setFont(new Font("Arial", Font.PLAIN, 12));
            textField.setPreferredSize(new Dimension(300, 30));
            inputComponent = textField;
        }
        
        contentPanel.add(inputComponent);
        contentPanel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(0, 150, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.setPreferredSize(new Dimension(80, 35));
        saveButton.addActionListener(e -> {
            String newValue;
            if (fieldType.equals("combo")) {
                newValue = ((JComboBox<String>) inputComponent).getSelectedItem() != null ? 
                          ((JComboBox<String>) inputComponent).getSelectedItem().toString() : "None";
            } else {
                newValue = ((JTextField) inputComponent).getText().trim();
            }
            
            if (!newValue.isEmpty()) {
                updateFieldValue(component, newValue);
                JOptionPane.showMessageDialog(editDialog, 
                    fieldName + " updated successfully!", 
                    "Update Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                editDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(editDialog, 
                    "Please enter a valid value.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(200, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setPreferredSize(new Dimension(80, 35));
        cancelButton.addActionListener(e -> editDialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel);

        editDialog.add(contentPanel, BorderLayout.CENTER);
        editDialog.setVisible(true);
    }

    private void updateFieldValue(Component component, String newValue) {
        if (component instanceof JTextField) {
            ((JTextField) component).setText(newValue);
        } else if (component instanceof JComboBox) {
            ((JComboBox<String>) component).setSelectedItem(newValue);
        }
    }
// Generic Content

    private void showGenericContent(String menuItem) {
        JPanel genericPanel = new JPanel(new BorderLayout());
        genericPanel.setBorder(BorderFactory.createTitledBorder(menuItem));

        JTextArea contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setText("This is the " + menuItem + " section.\n\n" +
                "Content for this section is currently under development.\n" +
                "Please check back later for updates.");
        contentArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(contentArea);
        genericPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(genericPanel);
    }

    /**
     * Retrieves student name using DataManager
     * @param studentID The student ID to look up
     * @return Formatted student name (FirstName LastName) or default if not found
     */
    private String getStudentNameFromDatabase(String studentID) {
        StudentInfo studentInfo = DataManager.getStudentInfo(studentID);
        if (studentInfo != null) {
            return studentInfo.getFullName();
        }
        return "STUDENT NAME NOT FOUND";
    }

    /**
     * Creates the Statement of Accounts panel matching the UI design
     */
    private JPanel createStatementOfAccountsPanel(MySinglyLinkedList<String> subItems) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left panel - Statement of Accounts
        JPanel leftPanel = createStatementLeftPanel(subItems);
        leftPanel.setPreferredSize(new Dimension(600, 0));

        // Right panel - Online Payment Channels
        JPanel rightPanel = createPaymentChannelsPanel(subItems);
        rightPanel.setPreferredSize(new Dimension(300, 0));

        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        return mainPanel;
    }

    private JPanel createStatementLeftPanel(MySinglyLinkedList<String> subItems) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setPreferredSize(new Dimension(0, 50));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel headerLabel = new JLabel(subItems.getFirst());
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        // Profile icon on the right
        JLabel profileIcon = new JLabel("👤");
        profileIcon.setForeground(Color.WHITE);
        profileIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        profileIcon.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(profileIcon, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Student Info
        JPanel studentInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        studentInfoPanel.setBackground(Color.WHITE);
        studentInfoPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        studentInfoPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel studentIcon = new JLabel("👤");
        studentIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        studentInfoPanel.add(studentIcon);
        
        JPanel studentTextPanel = new JPanel();
        studentTextPanel.setLayout(new BoxLayout(studentTextPanel, BoxLayout.Y_AXIS));
        studentTextPanel.setBackground(Color.WHITE);
        
        JLabel studentIDLabel = new JLabel(studentID);
        studentIDLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentTextPanel.add(studentIDLabel);
        
        JLabel studentNameLabel = new JLabel(studentName);
        studentNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        studentTextPanel.add(studentNameLabel);
        
        studentInfoPanel.add(studentTextPanel);
        contentPanel.add(studentInfoPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Amount Due Section
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        amountPanel.setBackground(Color.WHITE);
        
        JLabel amountDueLabel = new JLabel("Your amount due for Prelims is:");
        amountDueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amountPanel.add(amountDueLabel);
        
        JLabel amountDueValue = new JLabel("P " + String.format("%.2f", amountDue));
        amountDueValue.setFont(new Font("Arial", Font.BOLD, 24));
        amountDueValue.setForeground(Color.BLACK);
        amountDueValueLabel = amountDueValue; // Store reference for updates
        amountPanel.add(amountDueValue);
        
        contentPanel.add(amountPanel);
        contentPanel.add(Box.createVerticalStrut(10));

        // Overpayment Section
        JPanel overpaymentPanel = new JPanel();
        overpaymentPanel.setLayout(new BoxLayout(overpaymentPanel, BoxLayout.Y_AXIS));
        overpaymentPanel.setBackground(Color.WHITE);
        
        JLabel overpaymentLabel = new JLabel("Your remaining balance as of September 17, 2025 is: ");
        overpaymentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        overpaymentPanel.add(overpaymentLabel);
        
        JLabel overpaymentValue = new JLabel("P (" + String.format("%.2f", currentBalance) + ")");
        overpaymentValue.setFont(new Font("Arial", Font.BOLD, 24));
        overpaymentValue.setForeground(Color.BLACK);
        overpaymentValueLabel = overpaymentValue; // Store reference for updates
        overpaymentPanel.add(overpaymentValue);
        
        contentPanel.add(overpaymentPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Prelim Status
        String prelimStatusText = amountDue <= 0 ? 
            "PRELIM STATUS: PAID. Permitted to take the exams." : 
            "PRELIM STATUS: NOT PAID. Please pay before prelim exams. Ignore if you're SLU Dependent or Full TOF Scholar.";
        Color prelimStatusColor = amountDue <= 0 ? 
            new Color(0, 150, 0) : // Green for paid
            new Color(200, 0, 0);   // Red for unpaid
        
        JLabel prelimStatusLabel = new JLabel(prelimStatusText);
        prelimStatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        prelimStatusLabel.setForeground(prelimStatusColor);
        prelimStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.prelimStatusLabel = prelimStatusLabel; // Store reference for updates
        contentPanel.add(prelimStatusLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        // Finals Status
        String statusText = amountDue <= 0 ? 
            "FINALS STATUS: PAID. Permitted to take the exams." : 
            "FINALS STATUS: UNPAID. Payment required to take exams.";
        Color statusColor = amountDue <= 0 ? 
            new Color(0, 150, 0) : // Green for paid
            new Color(200, 0, 0);   // Red for unpaid
        
        JLabel statusLabel = new JLabel(statusText);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(statusColor);
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Breakdown of Fees
        JPanel breakdownPanel = createBreakdownPanel();
        contentPanel.add(breakdownPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Online Payment Transactions
        JPanel transactionsPanel = createTransactionsPanel();
        contentPanel.add(transactionsPanel);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBreakdownPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setPreferredSize(new Dimension(0, 40));
        
        JLabel headerLabel = new JLabel("Breakdown of fees as of September 08, 2025");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(headerLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Date", "Description", "Amount"};
        Object[][] data = {
            {"", "BEGINNING BALANCE", "21177"},
            {"08/12/2025", "BPI ONLINE- 2025-08-10 (JV100106)", "(21,177.00)"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setPreferredSize(new Dimension(0, 40));
        
        JLabel headerLabel = new JLabel("Online Payment Transactions");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(headerLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Date", "Channel", "Reference", "Amount"};
        
        // Load existing payment data from file
        Object[][] data = loadPaymentTransactions();

        paymentTableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(paymentTableModel);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the Transcript of Records panel matching the UI design
     */
    private JPanel createTranscriptOfRecordsPanel(MySinglyLinkedList<String> subItems) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Header - Transcript of Records
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setPreferredSize(new Dimension(0, 50));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel headerLabel = new JLabel(subItems.toString());
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Create transcript table with all semesters
        String[] columnNames = {"Course Number", "Descriptive Title", "Grade", "Units"};
        
        // Generate random grades (76-99)
        Object[][] transcriptData = generateTranscriptData();
        
        DefaultTableModel transcriptModel = new DefaultTableModel(transcriptData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        JTable transcriptTable = new JTable(transcriptModel);
        transcriptTable.setRowHeight(30);
        transcriptTable.getTableHeader().setReorderingAllowed(false);
        transcriptTable.setAutoCreateRowSorter(false);
        transcriptTable.setShowGrid(true);
        transcriptTable.setGridColor(new Color(200, 200, 200));
        transcriptTable.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Style the table
        transcriptTable.getTableHeader().setBackground(new Color(240, 240, 240));
        transcriptTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set column alignments
        transcriptTable.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });
        
        transcriptTable.getColumnModel().getColumn(1).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });
        
        transcriptTable.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                return this;
            }
        });
        
        transcriptTable.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(transcriptTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createPaymentChannelsPanel(MySinglyLinkedList<String> subItems) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setPreferredSize(new Dimension(0, 50));
        
        JLabel headerLabel = new JLabel(subItems.get(1));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel instructionLabel = new JLabel("Tuition fees can be paid via the available online payment channels.");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setForeground(Color.DARK_GRAY);
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Payment channel buttons
        String[] channels = {
            "UnionBank UPay Online",
            "Dragonpay Payment Gateway", 
            "BPI BPI Online",
            "BDO BDO Online",
            "BDO Bills Payment",
            "Bukas Tuition Installment Plans"
        };

        Color[] colors = {
            new Color(255, 140, 0), // Orange
            new Color(255, 69, 0),  // Red-Orange
            new Color(220, 20, 60), // Crimson
            new Color(0, 100, 200), // Blue
            new Color(0, 100, 200), // Blue
            new Color(135, 206, 235) // Light Blue
        };

        for (int i = 0; i < channels.length; i++) {
            JButton channelButton = new JButton(channels[i]);
            channelButton.setBackground(colors[i]);
            channelButton.setForeground(Color.WHITE);
            channelButton.setFont(new Font("Arial", Font.BOLD, 12));
            channelButton.setPreferredSize(new Dimension(250, 40));
            channelButton.setFocusPainted(false);
            channelButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            // Add action listener for payment processing
            final String channelName = channels[i];
            channelButton.addActionListener(e -> showPaymentDialog(channelName));
            
            contentPanel.add(channelButton);
            contentPanel.add(Box.createVerticalStrut(10));
        }

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    // Fields to track payment information
    private double currentBalance; // Current overpayment amount (randomized)
    private double amountDue; // Current amount due (randomized)
    private JLabel overpaymentValueLabel; // Reference to update the display
    private JLabel amountDueValueLabel; // Reference to update the amount due display
    private JLabel prelimStatusLabel; // Reference to update the PRELIM STATUS display
    private DefaultTableModel paymentTableModel; // Reference to payment transactions table model

    /**
     * Shows payment dialog to collect card information and process payment
     */
    private void showPaymentDialog(String channelName) {
        JDialog paymentDialog = new JDialog(this, "Payment - " + channelName, true);
        paymentDialog.setSize(500, 400);
        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel headerLabel = new JLabel("Payment Information - " + channelName);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        
        paymentDialog.add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Card Number
        JPanel cardNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        cardNumberPanel.setBackground(Color.WHITE);
        JLabel cardNumberLabel = new JLabel("Card Number (16 digits):");
        cardNumberLabel.setPreferredSize(new Dimension(150, 25));
        cardNumberPanel.add(cardNumberLabel);
        
        JTextField cardNumberField = new JTextField();
        cardNumberField.setPreferredSize(new Dimension(200, 25));
        cardNumberField.setDocument(new CardNumberDocument()); // Custom document for formatting
        cardNumberPanel.add(cardNumberField);
        contentPanel.add(cardNumberPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // CVV
        JPanel cvvPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        cvvPanel.setBackground(Color.WHITE);
        JLabel cvvLabel = new JLabel("CVV (3 digits):");
        cvvLabel.setPreferredSize(new Dimension(150, 25));
        cvvPanel.add(cvvLabel);
        
        JTextField cvvField = new JTextField();
        cvvField.setPreferredSize(new Dimension(100, 25));
        cvvField.setDocument(new CVVDocument()); // Custom document for 3 digits only
        cvvPanel.add(cvvField);
        contentPanel.add(cvvPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Expiration Date
        JPanel expDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        expDatePanel.setBackground(Color.WHITE);
        JLabel expDateLabel = new JLabel("Expiration Date (MM/YY):");
        expDateLabel.setPreferredSize(new Dimension(150, 25));
        expDatePanel.add(expDateLabel);
        
        JTextField expDateField = new JTextField();
        expDateField.setPreferredSize(new Dimension(100, 25));
        expDateField.setDocument(new ExpirationDateDocument()); // Custom document for MM/YY format
        expDatePanel.add(expDateField);
        contentPanel.add(expDatePanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Card Holder Name
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        namePanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("Card Holder Name:");
        nameLabel.setPreferredSize(new Dimension(150, 25));
        namePanel.add(nameLabel);
        
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 25));
        namePanel.add(nameField);
        contentPanel.add(namePanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Amount to Pay
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        amountPanel.setBackground(Color.WHITE);
        JLabel amountLabel = new JLabel("Amount to Pay:");
        amountLabel.setPreferredSize(new Dimension(150, 25));
        amountPanel.add(amountLabel);
        
        JTextField amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(150, 25));
        amountPanel.add(amountField);
        contentPanel.add(amountPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton submitButton = new JButton("Submit Payment");
        submitButton.setBackground(new Color(0, 150, 0));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 12));
        submitButton.setPreferredSize(new Dimension(120, 35));
        submitButton.addActionListener(e -> {
            if (processPayment(cardNumberField.getText(), cvvField.getText(), 
                             expDateField.getText(), nameField.getText(), 
                             amountField.getText(), channelName)) {
                paymentDialog.dispose();
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(200, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setPreferredSize(new Dimension(80, 35));
        cancelButton.addActionListener(e -> paymentDialog.dispose());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel);

        paymentDialog.add(contentPanel, BorderLayout.CENTER);
        paymentDialog.setVisible(true);
    }

    /**
     * Processes the payment and updates the balance
     */
    private boolean processPayment(String cardNumber, String cvv, String expDate, 
                                 String cardHolderName, String amountStr, String channelName) {
        // Validate inputs
        if (cardNumber.replaceAll("\\s", "").length() != 16) {
            JOptionPane.showMessageDialog(this, "Card number must be 16 digits", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (cvv.length() != 3) {
            JOptionPane.showMessageDialog(this, "CVV must be 3 digits", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!expDate.matches("\\d{2}/\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Expiration date must be in MM/YY format", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (cardHolderName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Card holder name is required", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Process payment - handle overpayment if amount exceeds amount due
        if (amount >= amountDue) {
            // Calculate overpayment amount
            double overpaymentAmount = amount - amountDue;
            
            // Clear the amount due and add excess to overpayment
            amountDue = 0.0;
            currentBalance += overpaymentAmount;
            
            // Log the payment transaction
            logPaymentTransaction(channelName, amount);
            
            // Add payment to table
            addPaymentToTable(channelName, amount);
            
            // Show success message with overpayment details
            String message = "Payment successful!\n" +
                "Channel: " + channelName + "\n" +
                "Amount Paid: P " + String.format("%.2f", amount) + "\n" +
                "Amount Due: P 0.00 (FULLY PAID)\n" +
                "Overpayment: P " + String.format("%.2f", overpaymentAmount) + "\n" +
                "Total Overpayment Balance: P " + String.format("%.2f", currentBalance);
            
            JOptionPane.showMessageDialog(this, message, "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Partial payment - just reduce amount due
            amountDue -= amount;
            
            // Log the payment transaction
            logPaymentTransaction(channelName, amount);
            
            // Add payment to table
            addPaymentToTable(channelName, amount);
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Payment successful!\n" +
                "Channel: " + channelName + "\n" +
                "Amount Paid: P " + String.format("%.2f", amount) + "\n" +
                "Remaining Amount Due: P " + String.format("%.2f", amountDue),
                "Payment Successful", 
                JOptionPane.INFORMATION_MESSAGE);
        }

        // Update the display (you would need to refresh the Statement of Accounts panel)
        refreshStatementOfAccounts();
        
        return true;
    }

    /**
     * Loads payment transactions using DataManager
     */
    private Object[][] loadPaymentTransactions() {
        java.util.List<PaymentTransaction> transactions = DataManager.loadPaymentTransactions(studentID);
        
        if (transactions.isEmpty()) {
            return new Object[0][4];
        }
        
        // Convert to 2D array
        Object[][] result = new Object[transactions.size()][4];
        for (int i = 0; i < transactions.size(); i++) {
            result[i] = transactions.get(i).toTableRow();
        }
        
        return result;
    }

    /**
     * Logs payment transaction using DataManager
     */
    private void logPaymentTransaction(String channelName, double amount) {
        DataManager.logPaymentTransaction(channelName, amount, studentID);
    }

    /**
     * Adds a new payment transaction to the table
     */
    private void addPaymentToTable(String channelName, double amount) {
        if (paymentTableModel != null) {
            // Generate current date and time
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
            String currentDateTime = dateFormat.format(new java.util.Date());
            
            // Create reference text
            String reference = "FIRST SEMESTER 2025-2026 Enrollme.";
            
            // Format amount with comma separator
            String formattedAmount = String.format("P %,.2f", amount);
            
            // Add new row to table
            paymentTableModel.addRow(new Object[]{currentDateTime, channelName, reference, formattedAmount});
        }
    }

    /**
     * Refreshes the Statement of Accounts display with updated balance
     */
    private void refreshStatementOfAccounts() {
        // Update the amount due value label if it exists
        if (amountDueValueLabel != null) {
            amountDueValueLabel.setText("P " + String.format("%.2f", amountDue));
        }
        
        // Update the overpayment value label if it exists
        if (overpaymentValueLabel != null) {
            overpaymentValueLabel.setText("P (" + String.format("%.2f", currentBalance) + ")");
        }
        
        // Update the PRELIM STATUS label if it exists
        if (prelimStatusLabel != null) {
            String prelimStatusText = amountDue <= 0 ? 
                "PRELIM STATUS: PAID. Permitted to take the exams." : 
                "PRELIM STATUS: NOT PAID. Please pay before prelim exams. Ignore if you're SLU Dependent or Full TOF Scholar.";
            Color prelimStatusColor = amountDue <= 0 ? 
                new Color(0, 150, 0) : // Green for paid
                new Color(200, 0, 0);   // Red for unpaid
            
            prelimStatusLabel.setText(prelimStatusText);
            prelimStatusLabel.setForeground(prelimStatusColor);
        }
        
        // Show success message with both balances
        String message = "Statement of Accounts has been updated.\n" +
            "Amount Due: P " + String.format("%.2f", amountDue) + "\n" +
            "Overpayment Balance: P " + String.format("%.2f", currentBalance);
        
        JOptionPane.showMessageDialog(this, message, "Balance Updated", JOptionPane.INFORMATION_MESSAGE);
    }

    // Custom Document classes for input formatting
    private static class CardNumberDocument extends javax.swing.text.PlainDocument {
        @Override
        public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
            if (str == null) return;
            
            String currentText = getText(0, getLength());
            String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
            newText = newText.replaceAll("\\D", ""); // Remove non-digits
            
            if (newText.length() <= 16) {
                // Format with spaces every 4 digits
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < newText.length(); i++) {
                    if (i > 0 && i % 4 == 0) {
                        formatted.append(" ");
                    }
                    formatted.append(newText.charAt(i));
                }
                super.remove(0, getLength());
                super.insertString(0, formatted.toString(), a);
            }
        }
    }

    private static class CVVDocument extends javax.swing.text.PlainDocument {
        @Override
        public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
            if (str == null) return;
            
            String currentText = getText(0, getLength());
            String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
            newText = newText.replaceAll("\\D", ""); // Remove non-digits
            
            if (newText.length() <= 3) {
                super.remove(0, getLength());
                super.insertString(0, newText, a);
            }
        }
    }

    private static class ExpirationDateDocument extends javax.swing.text.PlainDocument {
        @Override
        public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
            if (str == null) return;
            
            String currentText = getText(0, getLength());
            String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
            newText = newText.replaceAll("\\D", ""); // Remove non-digits
            
            if (newText.length() <= 4) {
                // Format as MM/YY
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < newText.length(); i++) {
                    if (i == 2) {
                        formatted.append("/");
                    }
                    formatted.append(newText.charAt(i));
                }
                super.remove(0, getLength());
                super.insertString(0, formatted.toString(), a);
            }
        }
    }

    /**
     * Creates the About iSLU panel with downloadables and overview sections
     */
    private JPanel createAboutISLUPanel(MySinglyLinkedList<String> subItems) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create scroll pane for the content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Add Downloadables section
        JPanel downloadablesPanel = createDownloadablesSection(subItems);
        contentPanel.add(downloadablesPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Add About iSLU section
        JPanel aboutPanel = createAboutISLUSection(subItems);
        contentPanel.add(aboutPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    /**
     * Creates the downloadables section with all categories
     */
    private JPanel createDownloadablesSection(MySinglyLinkedList<String> subItems) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(0, 50));
        
        JLabel headerIcon = new JLabel("📁");
        headerIcon.setForeground(Color.WHITE);
        headerIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        headerPanel.add(headerIcon);
        
        JLabel headerLabel = new JLabel(subItems.getFirst());
        headerLabel.setForeground(new Color(255, 204, 102));
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Content with downloadable items
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Add all downloadable categories
        addDownloadableCategory(contentPanel, "General", createGeneralDownloadables());
        addDownloadableCategory(contentPanel, "Center for Counseling and Wellness", createCounselingDownloadables());
        addDownloadableCategory(contentPanel, "Other Student Services", createOtherServicesDownloadables());
        addDownloadableCategory(contentPanel, "SAMCIS", createSAMCISDownloadables());
        addDownloadableCategory(contentPanel, "SAS", createSASDownloadables());
        addDownloadableCategory(contentPanel, "SEA", createSEADownloadables());
        addDownloadableCategory(contentPanel, "SONAHBS", createSONAHBSDownloadables());
        addDownloadableCategory(contentPanel, "SOM", createSOMDownloadables());
        addDownloadableCategory(contentPanel, "STELA", createSTELADownloadables());
        addDownloadableCategory(contentPanel, "Student Services Orientation", createStudentServicesDownloadables());

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates the About iSLU section
     */
    private JPanel createAboutISLUSection(MySinglyLinkedList<String> subItems) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(0, 50));
        
        JLabel headerIcon = new JLabel("ℹ️");
        headerIcon.setForeground(Color.WHITE);
        headerIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        headerPanel.add(headerIcon);
        
        JLabel headerLabel = new JLabel(subItems.get(1));
        headerLabel.setForeground(new Color(255, 204, 102));
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // OVERVIEW section
        JLabel overviewTitle = new JLabel("OVERVIEW");
        overviewTitle.setFont(new Font("Arial", Font.BOLD, 18));
        overviewTitle.setForeground(Color.BLACK);
        contentPanel.add(overviewTitle);
        contentPanel.add(Box.createVerticalStrut(15));

        // ABOUT ISLU PORTAL section
        JLabel aboutPortalTitle = new JLabel("ABOUT ISLU PORTAL");
        aboutPortalTitle.setFont(new Font("Arial", Font.BOLD, 14));
        aboutPortalTitle.setForeground(Color.BLACK);
        contentPanel.add(aboutPortalTitle);
        contentPanel.add(Box.createVerticalStrut(8));

        JTextArea aboutPortalText = new JTextArea();
        aboutPortalText.setText("iSLU Portal is for Saint Louis University students and parents, that serves as a personal assistant in carrying out university-related tasks.\n\n" +
                               "Students can view their personal details, schedules, current grades, accountabilities, curriculum checklist and more. Parents can also view these modules of their " +
                               "children as long they register.");
        aboutPortalText.setFont(new Font("Arial", Font.PLAIN, 12));
        aboutPortalText.setLineWrap(true);
        aboutPortalText.setWrapStyleWord(true);
        aboutPortalText.setEditable(false);
        aboutPortalText.setBackground(Color.WHITE);
        aboutPortalText.setBorder(null);
        contentPanel.add(aboutPortalText);
        contentPanel.add(Box.createVerticalStrut(20));

        // FEATURES AVAILABLE section
        JLabel featuresTitle = new JLabel("FEATURES AVAILABLE");
        featuresTitle.setFont(new Font("Arial", Font.BOLD, 14));
        featuresTitle.setForeground(Color.BLACK);
        contentPanel.add(featuresTitle);
        contentPanel.add(Box.createVerticalStrut(8));

        String[] features = {
            "Personal Details: where a student can view his/her personal information.",
            "Class Schedule: where a student can view his/her enrolled subjects of the current term.",
            "Class Attendance: displays the absences of the student of the current term.",
            "Current Grades: shows the Prelim, Mid-term and Tentative final grades of a student.",
            "Transcript of Records: shows the final grades in the previous term of a student.",
            "Statements of Accounts: shows if a student still has account balance from the accounting office.",
            "Curriculum Checklist: shows the student's checklist of a curriculum chosen.",
            "School Calendar: where a student can view the school events and no classes within an academic year."
        };

        for (String feature : features) {
            JTextArea featureText = new JTextArea(feature);
            featureText.setFont(new Font("Arial", Font.PLAIN, 12));
            featureText.setLineWrap(true);
            featureText.setWrapStyleWord(true);
            featureText.setEditable(false);
            featureText.setBackground(Color.WHITE);
            featureText.setBorder(null);
            contentPanel.add(featureText);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        contentPanel.add(Box.createVerticalStrut(15));

        // HOW TO REGISTER section
        JLabel registerTitle = new JLabel("HOW TO REGISTER? Or FORGOT USER ID Number and PASSWORD!");
        registerTitle.setFont(new Font("Arial", Font.BOLD, 14));
        registerTitle.setForeground(Color.BLACK);
        contentPanel.add(registerTitle);
        contentPanel.add(Box.createVerticalStrut(8));

        JTextArea studentsText = new JTextArea();
        studentsText.setText("STUDENTS: proceed to IT CENTER SOFTWARE DEVELOPMENT (MIS), 2nd floor, Burgos Administrative Center, Saint Louis University.");
        studentsText.setFont(new Font("Arial", Font.PLAIN, 12));
        studentsText.setLineWrap(true);
        studentsText.setWrapStyleWord(true);
        studentsText.setEditable(false);
        studentsText.setBackground(Color.WHITE);
        studentsText.setBorder(null);
        contentPanel.add(studentsText);
        contentPanel.add(Box.createVerticalStrut(8));

        JTextArea parentsText = new JTextArea();
        parentsText.setText("PARENTS/GUARDIANS: register at Registrar's office, Diego Silang building, Saint Louis University.");
        parentsText.setFont(new Font("Arial", Font.PLAIN, 12));
        parentsText.setLineWrap(true);
        parentsText.setWrapStyleWord(true);
        parentsText.setEditable(false);
        parentsText.setBackground(Color.WHITE);
        parentsText.setBorder(null);
        contentPanel.add(parentsText);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Helper method to add downloadable categories
     */
    private void addDownloadableCategory(JPanel parent, String categoryName, String[] items) {
        // Category header
        JLabel categoryLabel = new JLabel("• " + categoryName);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 13));
        categoryLabel.setForeground(Color.BLACK);
        parent.add(categoryLabel);
        parent.add(Box.createVerticalStrut(8));

        // Category items
        for (String item : items) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 2));
            itemPanel.setBackground(new Color(245, 245, 245));
            
            JLabel itemLabel = new JLabel("○ " + item.split("\\|")[0]);
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            itemLabel.setForeground(Color.BLACK);
            itemPanel.add(itemLabel);
            
            JLabel downloadLink = new JLabel("[download]");
            downloadLink.setFont(new Font("Arial", Font.PLAIN, 12));
            downloadLink.setForeground(Color.RED);
            downloadLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
            downloadLink.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane.showMessageDialog(null, "Download functionality would be implemented here for: " + item.split("\\|")[0], 
                                                "Download", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            itemPanel.add(downloadLink);
            
            parent.add(itemPanel);
        }
        parent.add(Box.createVerticalStrut(15));
    }

    // Helper methods to create downloadable items for each category
    private String[] createGeneralDownloadables() {
        return new String[]{
            "Student Handbook",
            "Safety Orientation Manual",
            "Ordinance 021-2018 - Harassment",
            "GGuide for Education",
            "The Mission and Identity Cluster",
            "Official List of Student Organizations",
            "UNIVERSITY LIBRARIES",
            "OSA Student Services Orientation",
            "KASAMA SSC Orientation",
            "Feeling at Home in SLU",
            "Health and Safety Protocols re COVID 19 Prevention and Control",
            "Policies and Guidelines on Student Behavior during Online Correspondence Based Learning",
            "SLU Policy on COVID-19 Prevention and Control Measures in the Workplace",
            "SLU Privacy Policy",
            "SLU Quality Policy",
            "SLU UNIVERSITY PRAYER",
            "Special Guidelines for Recognition or Renewal of Recognition 2021 to 2022",
            "Baguio City Anti Discrimination Ordinance",
            "IRR of RA 11313 Safe Spaces Act",
            "Safe Spaces Act 20190417 RA 11313 R&D",
            "Emails of SLU Student Groups"
        };
    }

    private String[] createCounselingDownloadables() {
        return new String[]{
            "Handouts for Parents on their Children's Independent Living in College",
            "Freshie Career Booklet",
            "Referral Guide Poster",
            "SULN Brochure",
            "SULN Form",
            "The Louisian Journal 2023",
            "CCV Referral Form"
        };
    }

    private String[] createOtherServicesDownloadables() {
        return new String[]{
            "MEDICAL CLINIC CONTINUITY PLAN",
            "Online Schedule of Medical Clinic",
            "SLU Residence Halls"
        };
    }

    private String[] createSAMCISDownloadables() {
        return new String[]{
            "SAMCIS Online Helpdesk",
            "SAMCIS Program Offerings Overview"
        };
    }

    private String[] createSASDownloadables() {
        return new String[]{
            "SAS HELPDESK"
        };
    }

    private String[] createSEADownloadables() {
        return new String[]{
            "SEA Online Helpdesk",
            "SEA Program Offerings Overview"
        };
    }

    private String[] createSONAHBSDownloadables() {
        return new String[]{
            "SONAHBS Online Helpdesk",
            "SOL Online Helpdesk"
        };
    }

    private String[] createSOMDownloadables() {
        return new String[]{
            "SOM Online Helpdesk"
        };
    }

    private String[] createSTELADownloadables() {
        return new String[]{
            "STELA Online Helpdesk",
            "STELA Program Offerings Overview"
        };
    }

    private String[] createStudentServicesDownloadables() {
        return new String[]{
            "Guidance Center",
            "Office of Student Affairs",
            "University Registrar's Office",
            "University Libraries",
            "Office of External Relations, Media & Communications and Alumni Affairs",
            "Office of the Vice President for Mission and Identity",
            "Campus Planning, Maintenance, and Security Department",
            "Dental Clinic",
            "Medical Clinic",
            "Technology Management and Development Department"
        };
    }

    /**
     * Creates the Curriculum Checklist panel matching the UI design from the image
     */
    private JPanel createCurriculumChecklistPanel(MySinglyLinkedList<String> subItems) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Header - Curriculum Checklist
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header content
        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.setBackground(new Color(10, 45, 90));
        
        JLabel headerLabel = new JLabel(subItems.toString());
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerContent.add(headerLabel);
        
        // Note text
        JLabel noteLabel = new JLabel("NOTE: For inquiries regarding your checklist please proceed to your respective Dean's offices.");
        noteLabel.setForeground(new Color(255, 204, 102));
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        headerContent.add(Box.createVerticalStrut(5));
        headerContent.add(noteLabel);
        
        headerPanel.add(headerContent, BorderLayout.WEST);
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Create curriculum table
        String[] columnNames = {"", "Course Number", "Course Description", "Units"};
        
        // Generate curriculum data matching the image
        Object[][] curriculumData = generateCurriculumData();
        
        DefaultTableModel curriculumModel = new DefaultTableModel(curriculumData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // For checkboxes
                }
                return String.class;
            }
        };

        JTable curriculumTable = new JTable(curriculumModel);
        curriculumTable.setRowHeight(25);
        curriculumTable.getTableHeader().setReorderingAllowed(false);
        curriculumTable.setAutoCreateRowSorter(false);
        curriculumTable.setShowGrid(true);
        curriculumTable.setGridColor(new Color(200, 200, 200));
        curriculumTable.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Set column widths
        curriculumTable.getColumnModel().getColumn(0).setPreferredWidth(30);  // Checkbox
        curriculumTable.getColumnModel().getColumn(0).setMaxWidth(30);
        curriculumTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Course Number
        curriculumTable.getColumnModel().getColumn(2).setPreferredWidth(500); // Description
        curriculumTable.getColumnModel().getColumn(3).setPreferredWidth(50);  // Units
        
        // Style the table header
        curriculumTable.getTableHeader().setBackground(new Color(220, 220, 220));
        curriculumTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Custom renderer for semester headers (bold rows with no checkbox)
        curriculumTable.setDefaultRenderer(String.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Check if this is a semester header row (no course number)
                Object courseNum = table.getValueAt(row, 1);
                if (courseNum == null || courseNum.toString().trim().isEmpty()) {
                    setFont(new Font("Arial", Font.BOLD, 12));
                    setBackground(new Color(240, 240, 240));
                    setHorizontalAlignment(SwingConstants.LEFT);
                } else {
                    setFont(new Font("Arial", Font.PLAIN, 11));
                    setBackground(Color.WHITE);
                    if (column == 3) { // Units column
                        setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }
                }
                
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(curriculumTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    /**
     * Generates curriculum data matching the BSIT curriculum from the image
     */
    private Object[][] generateCurriculumData() {
        java.util.List<Object[]> data = new java.util.ArrayList<>();
        
        // First Year, First Semester
        data.add(new Object[]{null, "", "First Year, First Semester", ""});
        data.add(new Object[]{true, "CFE 101", "GOD'S JOURNEY WITH HIS PEOPLE", "3"});
        data.add(new Object[]{true, "FIT HW", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (HEALTH AND WELLNESS)", "2"});
        data.add(new Object[]{true, "GART", "ART APPRECIATION", "3"});
        data.add(new Object[]{true, "GHIST", "READINGS IN PHILIPPINE HISTORY", "3"});
        data.add(new Object[]{true, "GSELF", "UNDERSTANDING THE SELF", "3"});
        data.add(new Object[]{true, "IT 111", "INTRODUCTION TO COMPUTING (LEC)", "2"});
        data.add(new Object[]{true, "IT 111L", "INTRODUCTION TO COMPUTING (LAB)", "1"});
        data.add(new Object[]{true, "IT 112", "COMPUTER PROGRAMMING 1 (LEC)", "2"});
        data.add(new Object[]{true, "IT 112L", "COMPUTER PROGRAMMING 1 (LAB)", "1"});
        data.add(new Object[]{true, "IT 113", "DISCRETE MATHEMATICS", "3"});
        
        // First Year, Second Semester
        data.add(new Object[]{null, "", "First Year, Second Semester", ""});
        data.add(new Object[]{true, "CFE 102", "CHRISTIAN MORALITY IN OUR TIMES", "3"});
        data.add(new Object[]{true, "FIT CS", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (COMBATIVE SPORTS)", "2"});
        data.add(new Object[]{true, "GCWORLD", "THE CONTEMPORARY WORLD", "3"});
        data.add(new Object[]{true, "GMATH", "MATHEMATICS IN THE MODERN WORLD", "3"});
        data.add(new Object[]{true, "GPCOM", "PURPOSIVE COMMUNICATION", "3"});
        data.add(new Object[]{true, "IT 121", "INFORMATION SYSTEM FUNDAMENTALS", "3"});
        data.add(new Object[]{true, "IT 122", "COMPUTER PROGRAMMING 2", "2"});
        data.add(new Object[]{true, "IT 122L", "COMPUTER PROGRAMMING 2 (LAB)", "1"});
        data.add(new Object[]{true, "IT 123", "PLATFORM TECHNOLOGIES", "2"});
        data.add(new Object[]{true, "IT 123L", "PLATFORM TECHNOLOGIES (LAB)", "1"});
        
        // First Year, Short Term
        data.add(new Object[]{null, "", "First Year, Short Term", ""});
        data.add(new Object[]{true, "GRIZAL", "THE LIFE AND WORKS OF RIZAL", "3"});
        data.add(new Object[]{true, "IT 131", "COMPUTER ARCHITECTURE", "2"});
        data.add(new Object[]{true, "IT 131L", "COMPUTER ARCHITECTURE (LAB)", "1"});
        
        // Second Year, First Semester
        data.add(new Object[]{null, "", "Second Year, First Semester", ""});
        data.add(new Object[]{false, "CFE 103", "CATHOLIC FOUNDATION OF MISSION", "3"});
        data.add(new Object[]{false, "FIT OA", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (OUTDOOR AND ADVENTURE ACTIVITIES)", "2"});
        data.add(new Object[]{false, "GENVI", "ENVIRONMENTAL SCIENCE", "3"});
        data.add(new Object[]{false, "GSTS", "SCIENCE, TECHNOLOGY, AND SOCIETY", "3"});
        data.add(new Object[]{false, "IT 211", "REQUIREMENTS ANALYSIS AND MODELING", "3"});
        data.add(new Object[]{false, "IT 212", "DATA STRUCTURES (LEC)", "2"});
        data.add(new Object[]{false, "IT 212L", "DATA STRUCTURES (LAB)", "1"});
        data.add(new Object[]{false, "IT 213", "NETWORK FUNDAMENTALS (LEC)", "2"});
        data.add(new Object[]{false, "IT 213L", "NETWORK FUNDAMENTALS (LAB)", "1"});
        data.add(new Object[]{false, "NSTP-CWTS 1", "FOUNDATIONS OF SERVICE", "3"});
        
        // Second Year, Second Semester
        data.add(new Object[]{null, "", "Second Year, Second Semester", ""});
        data.add(new Object[]{false, "CFE 104", "CICM MISSIONARY IDENTITY", "3"});
        data.add(new Object[]{false, "FIT AQ", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (AQUATICS)", "2"});
        data.add(new Object[]{false, "GENTREP", "THE ENTREPRENEURIAL MIND", "3"});
        data.add(new Object[]{false, "GRVA", "READING VISUAL ART", "3"});
        data.add(new Object[]{false, "IT 221", "INFORMATION MANAGEMENT (LEC)", "2"});
        data.add(new Object[]{false, "IT 221L", "INFORMATION MANAGEMENT (LAB)", "1"});
        data.add(new Object[]{false, "IT 222", "INTEGRATIVE TECHNOLOGIES (LEC)", "2"});
        data.add(new Object[]{false, "IT 222L", "INTEGRATIVE TECHNOLOGIES (LAB)", "1"});
        data.add(new Object[]{false, "IT 223", "HUMAN COMPUTER INTERACTION", "3"});
        data.add(new Object[]{false, "NSTP-CWTS 2", "SOCIAL AWARENESS AND EMPOWERMENT FOR SERVICE", "3"});
        
        // Second Year, Short Term
        data.add(new Object[]{null, "", "Second Year, Short Term", ""});
        data.add(new Object[]{false, "CS 314", "SOCIAL AND PERSONAL DEVELOPMENT IN THE ICT WORKPLACE", "3"});
        data.add(new Object[]{false, "CS 315", "TECHNOLOGY-ASSISTED PRESENTATION AND COMMUNICATION", "3"});
        data.add(new Object[]{false, "GETHICS", "ETHICS", "3"});
        
        // Third Year, First Semester
        data.add(new Object[]{null, "", "Third Year, First Semester", ""});
        data.add(new Object[]{false, "CFE 105A", "CICM IN ACTION: JUSTICE, PEACE, INTEGRITY OF CREATION, INDIGENOUS PEOPLES & INTERRELIGIOUS DIALOGUE", "1.5"});
        data.add(new Object[]{false, "IT 311", "APPLICATIONS DEVELOPMENT (LEC)", "2"});
        data.add(new Object[]{false, "IT 311L", "APPLICATIONS DEVELOPMENT (LAB)", "1"});
        data.add(new Object[]{false, "IT 312", "WEB TECHNOLOGIES (LEC)", "2"});
        data.add(new Object[]{false, "IT 312L", "WEB TECHNOLOGIES (LAB)", "1"});
        data.add(new Object[]{false, "IT 313", "SOFTWARE ENGINEERING", "3"});
        data.add(new Object[]{false, "IT 314", "SOCIAL AND PROFESSIONAL ISSUES IN INFORMATION TECHNOLOGY", "3"});
        data.add(new Object[]{false, "IT 315", "TECHNOPRENEURSHIP", "3"});
        data.add(new Object[]{false, "ITE 15", "IT SECURITY MANAGEMENT (Elective)", "3"});
        data.add(new Object[]{false, "ITE 23", "ELECTRONIC COMMERCE (Elective)", "3"});
        data.add(new Object[]{false, "ITE 30", "INFORMATION TECHNOLOGY CERTIFICATION REVIEW (Elective)", "3"});
        
        // Third Year, Second Semester
        data.add(new Object[]{null, "", "Third Year, Second Semester", ""});
        data.add(new Object[]{false, "CFE 105B", "CICM IN ACTION: ENVIRONMENTAL PLANNING & MANAGEMENT, AND DISASTER RISK REDUCTION MANAGEMENT", "1.5"});
        data.add(new Object[]{false, "IT 321", "IT PROJECT 1", "3"});
        data.add(new Object[]{false, "IT 322", "DATA ANALYTICS (LEC)", "2"});
        data.add(new Object[]{false, "IT 322L", "DATA ANALYTICS (LAB)", "1"});
        data.add(new Object[]{false, "IT 323", "SYSTEM ADMINISTRATION AND MAINTENANCE (LEC)", "2"});
        data.add(new Object[]{false, "IT 323L", "SYSTEM ADMINISTRATION AND MAINTENANCE (LAB)", "1"});
        data.add(new Object[]{false, "IT 324", "SYSTEM INTEGRATION AND ARCHITECTURE", "3"});
        data.add(new Object[]{false, "IT 325", "FIELD TRIPS AND SEMINARS", "3"});
        data.add(new Object[]{false, "ITE 16", "CURRENT TRENDS 1 (Elective)", "3"});
        data.add(new Object[]{false, "ITE 27", "CURRENT TRENDS 2 (Elective)", "3"});
        data.add(new Object[]{false, "ITE 29", "SPECIAL TOPICS 2 (Elective)", "3"});
        
        // Third Year, Short Term
        data.add(new Object[]{null, "", "Third Year, Short Term", ""});
        data.add(new Object[]{false, "IT 331", "INFORMATION ASSURANCE AND SECURITY", "3"});
        data.add(new Object[]{false, "ITE 17", "DATA MINING (LEC)", "2"});
        data.add(new Object[]{false, "ITE 17L", "DATA MINING (LAB)", "1"});
        
        // Fourth Year, First Semester
        data.add(new Object[]{null, "", "Fourth Year, First Semester", ""});
        data.add(new Object[]{false, "CFE 106A", "EMBRACING THE CICM MISSION", "1.5"});
        data.add(new Object[]{false, "FOR LANG 1", "FOREIGN LANGUAGE 1", "3"});
        data.add(new Object[]{false, "IT 411", "IT PROJECT 2", "3"});
        data.add(new Object[]{false, "IT 412", "IT RESOURCE MANAGEMENT", "3"});
        data.add(new Object[]{false, "ITE 14", "UX CONCEPTS AND DESIGN (Elective)", "3"});
        data.add(new Object[]{false, "ITE 28", "SPECIAL TOPICS 1 (Elective)", "3"});
        
        // Fourth Year, Second Semester
        data.add(new Object[]{null, "", "Fourth Year, Second Semester", ""});
        data.add(new Object[]{false, "CFE 106B", "EMBRACING THE CICM MISSION", "1.5"});
        data.add(new Object[]{false, "IT 421", "PRACTICUM", "9"});
        
        return data.toArray(new Object[data.size()][4]);
    }
    
    /**
     * Parses profile data string from Database.txt into ProfileData object
     */
    private ProfileData parseProfileData(String profileData) {
        ProfileData profile = new ProfileData();
        
        if (profileData == null || profileData.trim().isEmpty()) {
            System.out.println("DEBUG: No profile data found, using defaults");
            return profile; // Return default values
        }
        
        System.out.println("DEBUG: Parsing profile data: " + profileData);
        
        try {
            String[] pairs = profileData.split(";");
            System.out.println("DEBUG: Found " + pairs.length + " data pairs");
            for (String pair : pairs) {
                if (pair.trim().isEmpty()) continue;
                
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    System.out.println("DEBUG: Parsing " + key + " = " + value);
                    
                    switch (key) {
                        case "Gender": profile.gender = value; break;
                        case "Citizenship": profile.citizenship = value; break;
                        case "Religion": profile.religion = value; break;
                        case "CivilStatus": profile.civilStatus = value; break;
                        case "Birthplace": profile.birthplace = value; break;
                        case "Nationality": profile.nationality = value; break;
                        case "HomeAddress": profile.homeAddress = value; break;
                        case "HomeTel": profile.homeTel = value; break;
                        case "BaguioAddress": profile.baguioAddress = value; break;
                        case "BaguioTel": profile.baguioTel = value; break;
                        case "Cellphone": profile.cellphone = value; break;
                        case "FatherName": profile.fatherName = value; break;
                        case "FatherOcc": profile.fatherOcc = value; break;
                        case "MotherName": profile.motherName = value; break;
                        case "MotherOcc": profile.motherOcc = value; break;
                        case "GuardianName": profile.guardianName = value; break;
                        case "GuardianAddress": profile.guardianAddress = value; break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing profile data: " + e.getMessage());
        }
        
        return profile;
    }
}

/**
 * Data class to hold parsed profile information
 */
class ProfileData {
    String gender = "Male";
    String citizenship = "Filipino";
    String religion = "Roman Catholic";
    String civilStatus = "Single";
    String birthplace = "None";
    String nationality = "Filipino";
    String homeAddress = "None";
    String homeTel = "None";
    String baguioAddress = "None";
    String baguioTel = "None";
    String cellphone = "None";
    String fatherName = "None";
    String fatherOcc = "None";
    String motherName = "None";
    String motherOcc = "None";
    String guardianName = "None";
    String guardianAddress = "None";
    
    // Getter methods
    public String getGender() { return gender; }
    public String getCitizenship() { return citizenship; }
    public String getReligion() { return religion; }
    public String getCivilStatus() { return civilStatus; }
    public String getBirthplace() { return birthplace; }
    public String getNationality() { return nationality; }
    public String getHomeAddress() { return homeAddress; }
    public String getHomeTel() { return homeTel; }
    public String getBaguioAddress() { return baguioAddress; }
    public String getBaguioTel() { return baguioTel; }
    public String getCellphone() { return cellphone; }
    public String getFatherName() { return fatherName; }
    public String getFatherOcc() { return fatherOcc; }
    public String getMotherName() { return motherName; }
    public String getMotherOcc() { return motherOcc; }
    public String getGuardianName() { return guardianName; }
    public String getGuardianAddress() { return guardianAddress; }
}