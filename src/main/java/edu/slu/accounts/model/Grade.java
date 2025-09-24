package edu.slu.accounts.model;

import java.time.LocalDateTime;

/**
 * Grade model class representing student grades
 */
public class Grade {
    private String id;
    private String studentId;
    private String subjectCode;
    private String subjectName;
    private double units;
    private Double prelimGrade;
    private Double midtermGrade;
    private Double finalGrade;
    private Double semesterGrade;
    private String remarks;
    private String semester;
    private String academicYear;
    private LocalDateTime createdAt;

    // Constructors
    public Grade() {
        this.createdAt = LocalDateTime.now();
    }

    public Grade(String studentId, String subjectCode, String subjectName, double units, 
                String semester, String academicYear) {
        this();
        this.studentId = studentId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.units = units;
        this.semester = semester;
        this.academicYear = academicYear;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public double getUnits() { return units; }
    public void setUnits(double units) { this.units = units; }

    public Double getPrelimGrade() { return prelimGrade; }
    public void setPrelimGrade(Double prelimGrade) { this.prelimGrade = prelimGrade; }

    public Double getMidtermGrade() { return midtermGrade; }
    public void setMidtermGrade(Double midtermGrade) { this.midtermGrade = midtermGrade; }

    public Double getFinalGrade() { return finalGrade; }
    public void setFinalGrade(Double finalGrade) { this.finalGrade = finalGrade; }

    public Double getSemesterGrade() { return semesterGrade; }
    public void setSemesterGrade(Double semesterGrade) { this.semesterGrade = semesterGrade; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Utility methods
    public String getFormattedPrelimGrade() {
        return prelimGrade != null ? String.format("%.1f", prelimGrade) : "TBA";
    }

    public String getFormattedMidtermGrade() {
        return midtermGrade != null ? String.format("%.1f", midtermGrade) : "TBA";
    }

    public String getFormattedFinalGrade() {
        return finalGrade != null ? String.format("%.1f", finalGrade) : "TBA";
    }

    public String getFormattedSemesterGrade() {
        return semesterGrade != null ? String.format("%.1f", semesterGrade) : "TBA";
    }

    public boolean isPassing() {
        return semesterGrade != null && semesterGrade >= 75.0;
    }

    public void calculateSemesterGrade() {
        if (prelimGrade != null && midtermGrade != null && finalGrade != null) {
            // Standard calculation: 30% prelim, 30% midterm, 40% final
            semesterGrade = (prelimGrade * 0.30) + (midtermGrade * 0.30) + (finalGrade * 0.40);
            
            // Set remarks based on grade
            if (semesterGrade >= 75.0) {
                remarks = "PASSED";
            } else {
                remarks = "FAILED";
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Grade{id='%s', subject='%s - %s', units=%.1f, " +
                           "prelim=%s, midterm=%s, final=%s, semester=%s, remarks='%s'}", 
                           id, subjectCode, subjectName, units,
                           getFormattedPrelimGrade(), getFormattedMidtermGrade(), 
                           getFormattedFinalGrade(), getFormattedSemesterGrade(), remarks);
    }
}