package edu.slu.accounts.dao;

import edu.slu.accounts.model.Grade;
import edu.slu.accounts.util.DatabaseConnection;
import edu.slu.accounts.util.IdGenerator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Grade operations
 */
public class GradeDAO {

    /**
     * Create a new grade
     */
    public boolean createGrade(Grade grade) {
        String sql = """
            INSERT INTO grades (id, student_id, subject_code, subject_name, units, prelim_grade, 
                              midterm_grade, final_grade, semester_grade, remarks, semester, 
                              academic_year, created_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (grade.getId() == null) {
                grade.setId(IdGenerator.generateId());
            }

            pstmt.setString(1, grade.getId());
            pstmt.setString(2, grade.getStudentId());
            pstmt.setString(3, grade.getSubjectCode());
            pstmt.setString(4, grade.getSubjectName());
            pstmt.setDouble(5, grade.getUnits());
            
            if (grade.getPrelimGrade() != null) {
                pstmt.setDouble(6, grade.getPrelimGrade());
            } else {
                pstmt.setNull(6, Types.DOUBLE);
            }
            
            if (grade.getMidtermGrade() != null) {
                pstmt.setDouble(7, grade.getMidtermGrade());
            } else {
                pstmt.setNull(7, Types.DOUBLE);
            }
            
            if (grade.getFinalGrade() != null) {
                pstmt.setDouble(8, grade.getFinalGrade());
            } else {
                pstmt.setNull(8, Types.DOUBLE);
            }
            
            if (grade.getSemesterGrade() != null) {
                pstmt.setDouble(9, grade.getSemesterGrade());
            } else {
                pstmt.setNull(9, Types.DOUBLE);
            }
            
            pstmt.setString(10, grade.getRemarks());
            pstmt.setString(11, grade.getSemester());
            pstmt.setString(12, grade.getAcademicYear());
            pstmt.setString(13, grade.getCreatedAt().toString());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error creating grade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find grades by student ID
     */
    public List<Grade> findByStudentId(String studentId) {
        return findByStudentIdAndSemester(studentId, "FIRST SEMESTER", "2025-2026");
    }

    /**
     * Find grades by student ID and semester
     */
    public List<Grade> findByStudentIdAndSemester(String studentId, String semester, String academicYear) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE student_id = ? AND semester = ? AND academic_year = ? ORDER BY subject_code";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, semester);
            pstmt.setString(3, academicYear);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                grades.add(mapResultSetToGrade(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding grades by student ID: " + e.getMessage());
        }

        return grades;
    }

    /**
     * Find grade by ID
     */
    public Optional<Grade> findById(String id) {
        String sql = "SELECT * FROM grades WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToGrade(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding grade by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Update grade
     */
    public boolean updateGrade(Grade grade) {
        String sql = """
            UPDATE grades SET prelim_grade = ?, midterm_grade = ?, final_grade = ?, 
                            semester_grade = ?, remarks = ? 
            WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (grade.getPrelimGrade() != null) {
                pstmt.setDouble(1, grade.getPrelimGrade());
            } else {
                pstmt.setNull(1, Types.DOUBLE);
            }
            
            if (grade.getMidtermGrade() != null) {
                pstmt.setDouble(2, grade.getMidtermGrade());
            } else {
                pstmt.setNull(2, Types.DOUBLE);
            }
            
            if (grade.getFinalGrade() != null) {
                pstmt.setDouble(3, grade.getFinalGrade());
            } else {
                pstmt.setNull(3, Types.DOUBLE);
            }
            
            if (grade.getSemesterGrade() != null) {
                pstmt.setDouble(4, grade.getSemesterGrade());
            } else {
                pstmt.setNull(4, Types.DOUBLE);
            }
            
            pstmt.setString(5, grade.getRemarks());
            pstmt.setString(6, grade.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating grade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update prelim grade
     */
    public boolean updatePrelimGrade(String gradeId, double prelimGrade) {
        String sql = "UPDATE grades SET prelim_grade = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, prelimGrade);
            pstmt.setString(2, gradeId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating prelim grade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get student GPA
     */
    public double getStudentGPA(String studentId, String semester, String academicYear) {
        String sql = """
            SELECT AVG(semester_grade) as gpa 
            FROM grades 
            WHERE student_id = ? AND semester = ? AND academic_year = ? 
            AND semester_grade IS NOT NULL
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, semester);
            pstmt.setString(3, academicYear);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("gpa");
            }

        } catch (SQLException e) {
            System.err.println("Error calculating GPA: " + e.getMessage());
        }

        return 0.0;
    }

    /**
     * Map ResultSet to Grade object
     */
    private Grade mapResultSetToGrade(ResultSet rs) throws SQLException {
        Grade grade = new Grade();
        grade.setId(rs.getString("id"));
        grade.setStudentId(rs.getString("student_id"));
        grade.setSubjectCode(rs.getString("subject_code"));
        grade.setSubjectName(rs.getString("subject_name"));
        grade.setUnits(rs.getDouble("units"));
        
        double prelimGrade = rs.getDouble("prelim_grade");
        if (!rs.wasNull()) {
            grade.setPrelimGrade(prelimGrade);
        }
        
        double midtermGrade = rs.getDouble("midterm_grade");
        if (!rs.wasNull()) {
            grade.setMidtermGrade(midtermGrade);
        }
        
        double finalGrade = rs.getDouble("final_grade");
        if (!rs.wasNull()) {
            grade.setFinalGrade(finalGrade);
        }
        
        double semesterGrade = rs.getDouble("semester_grade");
        if (!rs.wasNull()) {
            grade.setSemesterGrade(semesterGrade);
        }
        
        grade.setRemarks(rs.getString("remarks"));
        grade.setSemester(rs.getString("semester"));
        grade.setAcademicYear(rs.getString("academic_year"));
        
        String createdAtStr = rs.getString("created_at");
        if (createdAtStr != null) {
            grade.setCreatedAt(LocalDateTime.parse(createdAtStr));
        }

        return grade;
    }
}