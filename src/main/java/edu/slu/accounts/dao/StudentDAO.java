package edu.slu.accounts.dao;

import edu.slu.accounts.model.Student;
import edu.slu.accounts.util.DatabaseConnection;
import edu.slu.accounts.util.IdGenerator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Student operations
 */
public class StudentDAO {

    /**
     * Create a new student
     */
    public boolean createStudent(Student student) {
        String sql = """
            INSERT INTO students (id, student_number, first_name, middle_name, last_name, 
                                course, year_level, email, password, created_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (student.getId() == null) {
                student.setId(IdGenerator.generateId());
            }

            pstmt.setString(1, student.getId());
            pstmt.setString(2, student.getStudentNumber());
            pstmt.setString(3, student.getFirstName());
            pstmt.setString(4, student.getMiddleName());
            pstmt.setString(5, student.getLastName());
            pstmt.setString(6, student.getCourse());
            pstmt.setInt(7, student.getYearLevel());
            pstmt.setString(8, student.getEmail());
            pstmt.setString(9, student.getPassword());
            pstmt.setString(10, student.getCreatedAt().toString());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error creating student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find student by student number
     */
    public Optional<Student> findByStudentNumber(String studentNumber) {
        String sql = "SELECT * FROM students WHERE student_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding student by number: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Find student by ID
     */
    public Optional<Student> findById(String id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding student by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Find all students
     */
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_number";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding all students: " + e.getMessage());
        }

        return students;
    }

    /**
     * Update student
     */
    public boolean updateStudent(Student student) {
        String sql = """
            UPDATE students SET first_name = ?, middle_name = ?, last_name = ?, 
                              course = ?, year_level = ?, email = ? 
            WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getMiddleName());
            pstmt.setString(3, student.getLastName());
            pstmt.setString(4, student.getCourse());
            pstmt.setInt(5, student.getYearLevel());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete student
     */
    public boolean deleteStudent(String id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if student number exists
     */
    public boolean studentNumberExists(String studentNumber) {
        String sql = "SELECT COUNT(*) FROM students WHERE student_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking student number existence: " + e.getMessage());
        }

        return false;
    }

    /**
     * Map ResultSet to Student object
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getString("id"));
        student.setStudentNumber(rs.getString("student_number"));
        student.setFirstName(rs.getString("first_name"));
        student.setMiddleName(rs.getString("middle_name"));
        student.setLastName(rs.getString("last_name"));
        student.setCourse(rs.getString("course"));
        student.setYearLevel(rs.getInt("year_level"));
        student.setEmail(rs.getString("email"));
        student.setPassword(rs.getString("password"));
        
        String createdAtStr = rs.getString("created_at");
        if (createdAtStr != null) {
            student.setCreatedAt(LocalDateTime.parse(createdAtStr));
        }

        return student;
    }
}