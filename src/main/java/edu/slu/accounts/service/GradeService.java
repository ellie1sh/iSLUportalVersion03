package edu.slu.accounts.service;

import edu.slu.accounts.dao.GradeDAO;
import edu.slu.accounts.model.Grade;
import edu.slu.accounts.util.IdGenerator;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing student grades with access control
 */
public class GradeService {
    private final GradeDAO gradeDAO;
    private final AccountService accountService;

    public GradeService() {
        this.gradeDAO = new GradeDAO();
        this.accountService = new AccountService();
    }

    /**
     * Create initial grades for a student
     */
    public boolean createInitialGrades(String studentId) {
        String[][] subjects = {
            {"IT 101", "Programming Fundamentals", "3.0"},
            {"IT 102", "Data Structures and Algorithms", "3.0"},
            {"IT 103", "Database Management Systems", "3.0"},
            {"IT 104", "Web Development", "3.0"},
            {"GE 101", "Mathematics in the Modern World", "3.0"},
            {"GE 102", "Purposive Communication", "3.0"},
            {"PE 101", "Physical Education", "2.0"}
        };

        boolean allCreated = true;
        
        for (String[] subject : subjects) {
            Grade grade = new Grade(
                studentId,
                subject[0],
                subject[1],
                Double.parseDouble(subject[2]),
                "FIRST SEMESTER",
                "2025-2026"
            );
            
            // Set sample prelim grades (only visible after payment)
            double prelimGrade = 75.0 + (Math.random() * 20); // Random grade between 75-95
            grade.setPrelimGrade(prelimGrade);
            
            if (!gradeDAO.createGrade(grade)) {
                allCreated = false;
                System.err.println("Failed to create grade for subject: " + subject[0]);
            }
        }
        
        if (allCreated) {
            System.out.println("Initial grades created successfully for student: " + studentId);
        }
        
        return allCreated;
    }

    /**
     * Get student grades (with access control)
     */
    public List<Grade> getStudentGrades(String studentId) {
        // Check if student has paid prelim fees
        if (!accountService.isPrelimPaid(studentId)) {
            System.out.println("❌ Access denied. Prelim payment required to view grades.");
            return List.of(); // Return empty list
        }
        
        return gradeDAO.findByStudentId(studentId);
    }

    /**
     * Display grades in formatted table
     */
    public void displayGrades(String studentId) {
        // Check payment status first
        if (!accountService.isPrelimPaid(studentId)) {
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.println("                        GRADES ACCESS                          ");
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.println("❌ ACCESS DENIED");
            System.out.println();
            System.out.println("You must complete your PRELIM payment before viewing grades.");
            System.out.println("Please settle your payment first and try again.");
            System.out.println("═══════════════════════════════════════════════════════════════");
            return;
        }

        List<Grade> grades = gradeDAO.findByStudentId(studentId);
        
        if (grades.isEmpty()) {
            System.out.println("No grades found for this student.");
            return;
        }

        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                      PRELIM GRADES                            ");
        System.out.println("                   FIRST SEMESTER, 2025-2026                   ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.printf("%-10s %-35s %-6s %-10s\n", "Subject", "Subject Name", "Units", "Prelim");
        System.out.println("───────────────────────────────────────────────────────────────");

        double totalUnits = 0;
        double totalGradePoints = 0;
        
        for (Grade grade : grades) {
            System.out.printf("%-10s %-35s %-6.1f %-10s\n",
                            grade.getSubjectCode(),
                            grade.getSubjectName(),
                            grade.getUnits(),
                            grade.getFormattedPrelimGrade());
            
            totalUnits += grade.getUnits();
            if (grade.getPrelimGrade() != null) {
                totalGradePoints += grade.getPrelimGrade() * grade.getUnits();
            }
        }
        
        System.out.println("───────────────────────────────────────────────────────────────");
        System.out.printf("%-52s %-6.1f\n", "TOTAL UNITS:", totalUnits);
        
        if (totalUnits > 0) {
            double gpa = totalGradePoints / totalUnits;
            System.out.printf("%-52s %-6.2f\n", "PRELIM GPA:", gpa);
            
            // Display GPA status
            if (gpa >= 90) {
                System.out.println("                        🏆 EXCELLENT! 🏆                       ");
            } else if (gpa >= 85) {
                System.out.println("                         ⭐ VERY GOOD ⭐                        ");
            } else if (gpa >= 80) {
                System.out.println("                          ✅ GOOD ✅                           ");
            } else if (gpa >= 75) {
                System.out.println("                        ✔️ SATISFACTORY ✔️                     ");
            } else {
                System.out.println("                      ⚠️ NEEDS IMPROVEMENT ⚠️                  ");
            }
        }
        
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Note: Only PRELIM grades are currently available.");
        System.out.println("Midterm and Final grades will be available after respective exams.");
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    /**
     * Update prelim grade for a subject
     */
    public boolean updatePrelimGrade(String studentId, String subjectCode, double grade) {
        List<Grade> grades = gradeDAO.findByStudentId(studentId);
        
        Optional<Grade> gradeOpt = grades.stream()
                                       .filter(g -> g.getSubjectCode().equals(subjectCode))
                                       .findFirst();
        
        if (gradeOpt.isPresent()) {
            Grade gradeRecord = gradeOpt.get();
            gradeRecord.setPrelimGrade(grade);
            return gradeDAO.updateGrade(gradeRecord);
        }
        
        return false;
    }

    /**
     * Calculate student GPA
     */
    public double calculateGPA(String studentId) {
        return gradeDAO.getStudentGPA(studentId, "FIRST SEMESTER", "2025-2026");
    }

    /**
     * Check if student can view grades
     */
    public boolean canViewGrades(String studentId) {
        return accountService.isPrelimPaid(studentId);
    }

    /**
     * Get grade summary for student
     */
    public String getGradeSummary(String studentId) {
        if (!canViewGrades(studentId)) {
            return "Grades access requires prelim payment completion.";
        }

        List<Grade> grades = gradeDAO.findByStudentId(studentId);
        
        if (grades.isEmpty()) {
            return "No grades available.";
        }

        int totalSubjects = grades.size();
        long subjectsWithGrades = grades.stream()
                                      .filter(g -> g.getPrelimGrade() != null)
                                      .count();
        double gpa = calculateGPA(studentId);

        return String.format("Subjects: %d | With Grades: %d | Prelim GPA: %.2f", 
                           totalSubjects, subjectsWithGrades, gpa);
    }

    /**
     * Get all grades for admin purposes
     */
    public List<Grade> getAllGrades() {
        // This method would typically be restricted to admin users
        return gradeDAO.findByStudentId(""); // This would need proper implementation
    }
}