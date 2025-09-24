package edu.slu.accounts.service;

import edu.slu.accounts.dao.StudentDAO;
import edu.slu.accounts.model.Student;
import edu.slu.accounts.util.IdGenerator;
import edu.slu.accounts.util.PasswordUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Service for initializing sample data in the database
 */
public class DataInitializationService {
    private final StudentDAO studentDAO;
    private final AccountService accountService;
    private final GradeService gradeService;

    public DataInitializationService() {
        this.studentDAO = new StudentDAO();
        this.accountService = new AccountService();
        this.gradeService = new GradeService();
    }

    /**
     * Initialize sample data
     */
    public void initializeSampleData() {
        System.out.println("🔄 Initializing sample data...");
        
        // Create sample students
        List<Student> sampleStudents = createSampleStudents();
        
        for (Student student : sampleStudents) {
            // Check if student already exists
            if (!studentDAO.studentNumberExists(student.getStudentNumber())) {
                student.setId(IdGenerator.generateId());
                student.setPassword(PasswordUtil.hashPassword("password")); // Demo password
                
                boolean studentCreated = studentDAO.createStudent(student);
                
                if (studentCreated) {
                    System.out.println("✅ Created student: " + student.getStudentNumber() + " - " + student.getFullName());
                    
                    // Create account for student
                    boolean accountCreated = accountService.createStudentAccount(student.getId());
                    
                    if (accountCreated) {
                        System.out.println("   📊 Account created with assessment");
                        
                        // Create initial grades
                        boolean gradesCreated = gradeService.createInitialGrades(student.getId());
                        
                        if (gradesCreated) {
                            System.out.println("   📝 Initial grades created");
                        }
                    }
                } else {
                    System.err.println("❌ Failed to create student: " + student.getStudentNumber());
                }
            } else {
                System.out.println("ℹ️  Student already exists: " + student.getStudentNumber());
            }
        }
        
        System.out.println("✅ Sample data initialization completed!");
        System.out.println();
    }

    /**
     * Create sample students
     */
    private List<Student> createSampleStudents() {
        return Arrays.asList(
            new Student("2024001", "Juan Carlos", "Santos", "Dela Cruz", 
                       "BSIT", 2, "juan.delacruz@student.slu.edu.ph", "password"),
            
            new Student("2024002", "Maria Elena", "Garcia", "Santos", 
                       "BSCS", 3, "maria.santos@student.slu.edu.ph", "password"),
            
            new Student("2024003", "Jose Miguel", "Reyes", "Fernandez", 
                       "BSIT", 1, "jose.fernandez@student.slu.edu.ph", "password"),
            
            new Student("2024004", "Ana Sophia", "Cruz", "Martinez", 
                       "BSCS", 2, "ana.martinez@student.slu.edu.ph", "password"),
            
            new Student("2024005", "Luis Antonio", "Mendoza", "Rodriguez", 
                       "BSIT", 4, "luis.rodriguez@student.slu.edu.ph", "password")
        );
    }

    /**
     * Display sample credentials
     */
    public void displaySampleCredentials() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                     DEMO CREDENTIALS                          ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Student Number | Password | Name                    | Course");
        System.out.println("───────────────┼──────────┼─────────────────────────┼───────");
        System.out.println("2024001        | password | Juan Carlos Dela Cruz   | BSIT 2");
        System.out.println("2024002        | password | Maria Elena Santos      | BSCS 3");
        System.out.println("2024003        | password | Jose Miguel Fernandez   | BSIT 1");
        System.out.println("2024004        | password | Ana Sophia Martinez     | BSCS 2");
        System.out.println("2024005        | password | Luis Antonio Rodriguez  | BSIT 4");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Note: All students start with UNPAID prelim status");
        System.out.println("      Use the payment system to unlock grade access");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();
    }

    /**
     * Reset all data (for testing purposes)
     */
    public void resetData() {
        System.out.println("⚠️  This will delete all existing data!");
        System.out.print("Are you sure? Type 'RESET' to confirm: ");
        
        // This would require additional implementation to actually reset the database
        System.out.println("Reset functionality would be implemented here.");
    }

    /**
     * Check if sample data exists
     */
    public boolean sampleDataExists() {
        return studentDAO.studentNumberExists("2024001");
    }
}