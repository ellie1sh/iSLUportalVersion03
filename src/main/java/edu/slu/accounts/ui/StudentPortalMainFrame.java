package edu.slu.accounts.ui;

import edu.slu.accounts.model.Student;
import edu.slu.accounts.service.AccountService;
import edu.slu.accounts.service.AuthenticationService;
import edu.slu.accounts.service.GradeService;
import edu.slu.accounts.service.PaymentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

class StudentPortalMainFrame extends JFrame {
	private final AuthenticationService authenticationService;
	private final AccountService accountService;
	private final PaymentService paymentService;
	private final GradeService gradeService;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel contentPanel = new JPanel(cardLayout);

	StudentPortalMainFrame(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
		this.accountService = new AccountService();
		this.paymentService = new PaymentService();
		this.gradeService = new GradeService();

		setTitle("iSLU Student Portal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setDividerLocation(260);
		split.setContinuousLayout(true);

		JPanel sidebar = createSidebar();
		split.setLeftComponent(sidebar);
		initializeContentCards();
		split.setRightComponent(contentPanel);

		setContentPane(split);
	}

	private JPanel createSidebar() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		Student user = authenticationService.getCurrentUser();
		JTextArea profile = new JTextArea();
		profile.setEditable(false);
		profile.setBackground(new Color(245, 245, 245));
		profile.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		profile.setText(user.getFullName() + "\n" + user.getStudentNumber() + "\n" + user.getCourse() + " Y" + user.getYearLevel());

		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout(0, 1, 0, 4));
		menu.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		addNavButton(menu, "Home", "HOME");
		addNavButton(menu, "Schedule", "SCHEDULE");
		addNavButton(menu, "Attendance", "ATTENDANCE");
		addNavButton(menu, "Statement of Accounts", "SOA");
		addNavButton(menu, "Grades", "GRADES");
		addNavButton(menu, "Transcript of Records", "TOR");
		addNavButton(menu, "Curriculum Checklist", "CHECKLIST");
		addNavButton(menu, "Medical Record", "MEDICAL");
		addNavButton(menu, "Personal Details", "PROFILE");
		addNavButton(menu, "Journal/Periodical", "JOURNAL");
		addNavButton(menu, "Downloadables / About iSLU", "DOWNLOADS");

		JButton logout = new JButton("Logout");
		logout.addActionListener(e -> doLogout());

		panel.add(profile, BorderLayout.NORTH);
		panel.add(new JScrollPane(menu), BorderLayout.CENTER);
		panel.add(logout, BorderLayout.SOUTH);
		return panel;
	}

	private void addNavButton(JPanel menu, String label, String card) {
		JButton b = new JButton(label);
		b.setHorizontalAlignment(SwingConstants.LEFT);
		b.addActionListener(e -> cardLayout.show(contentPanel, card));
		menu.add(b);
	}

	private void initializeContentCards() {
		contentPanel.add(createHomePanel(), "HOME");
		contentPanel.add(createSchedulePanel(), "SCHEDULE");
		contentPanel.add(createAttendancePanel(), "ATTENDANCE");
		contentPanel.add(createSOAPanel(), "SOA");
		contentPanel.add(createGradesPanel(), "GRADES");
		contentPanel.add(createPlaceholderPanel("Transcript of Records"), "TOR");
		contentPanel.add(createPlaceholderPanel("Curriculum Checklist"), "CHECKLIST");
		contentPanel.add(createPlaceholderPanel("Medical Record"), "MEDICAL");
		contentPanel.add(createPlaceholderPanel("Personal Details"), "PROFILE");
		contentPanel.add(createPlaceholderPanel("Journal/Periodical"), "JOURNAL");
		contentPanel.add(createPlaceholderPanel("Downloadables / About iSLU"), "DOWNLOADS");
	}

	private JPanel createHomePanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel("Welcome to the iSLU Student Portal", SwingConstants.CENTER), BorderLayout.CENTER);
		return p;
	}

	private JPanel createSchedulePanel() {
		JPanel p = new JPanel(new BorderLayout());
		String studentId = authenticationService.getCurrentUserId();
		String[][] data = {};
		String[] cols = {"Class Code", "Course #", "Description", "Units", "Time", "Days", "Room", "Instructor"};
		JTable table = new JTable(new DefaultTableModel(data, cols));
		p.add(new JScrollPane(table), BorderLayout.CENTER);
		p.add(new JLabel("Schedule view (data source pending migration)", SwingConstants.CENTER), BorderLayout.SOUTH);
		return p;
	}

	private JPanel createAttendancePanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel("Attendance view (data source pending migration)", SwingConstants.CENTER), BorderLayout.CENTER);
		return p;
	}

	private JPanel createSOAPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		String studentId = authenticationService.getCurrentUserId();
		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setText(new edu.slu.accounts.service.AccountService().getPaymentSummary(studentId));
		JButton pay = new JButton("Make Prelim Payment");
		pay.addActionListener(e -> doPayment());
		p.add(new JScrollPane(area));
		p.add(Box.createVerticalStrut(8));
		p.add(pay);
		return p;
	}

	private JPanel createGradesPanel() {
		JPanel p = new JPanel(new BorderLayout());
		JTextArea area = new JTextArea();
		area.setEditable(false);
		String studentId = authenticationService.getCurrentUserId();
		if (!gradeService.canViewGrades(studentId)) {
			area.setText("Access denied. Complete prelim payment to view grades.");
		} else {
			String summary = gradeService.getGradeSummary(studentId);
			area.setText(summary);
		}
		p.add(new JScrollPane(area), BorderLayout.CENTER);
		return p;
	}

	private JPanel createPlaceholderPanel(String title) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(title + " (to be implemented)", SwingConstants.CENTER), BorderLayout.CENTER);
		return p;
	}

	private void doPayment() {
		String studentId = authenticationService.getCurrentUserId();
		JDialog dialog = new JDialog(this, "Make Payment", true);
		dialog.setLayout(new BorderLayout());
		JTextArea info = new JTextArea(accountService.getPaymentSummary(studentId));
		info.setEditable(false);
		dialog.add(new JScrollPane(info), BorderLayout.CENTER);
		JPanel actions = new JPanel();
		JButton proceed = new JButton("Pay via UNIONBANK (no fees)");
		proceed.addActionListener(e -> {
			var calc = paymentService.calculatePayment("UNIONBANK", accountService.getStudentAccount(studentId).map(a -> a.getPrelimAmountDue()).orElse(0.0));
			boolean success = accountService.processPayment(studentId, calc.getTotalAmount(), "UNIONBANK", edu.slu.accounts.util.IdGenerator.generatePaymentReference());
			if (success) {
				JOptionPane.showMessageDialog(this, "Payment successful. You can now view grades.");
				dialog.dispose();
				cardLayout.show(contentPanel, "GRADES");
			}
		});
		actions.add(proceed);
		dialog.add(actions, BorderLayout.SOUTH);
		dialog.setSize(520, 360);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	private void doLogout() {
		authenticationService.logout();
		dispose();
		SwingUtilities.invokeLater(() -> new StudentPortalLoginFrame(new AuthenticationService()).setVisible(true));
	}
}

