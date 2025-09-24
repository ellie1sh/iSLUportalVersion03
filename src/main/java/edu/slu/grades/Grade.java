package edu.slu.grades;

/**
 * Grade data model class
 * Represents a grade record for a specific course and student
 */
public class Grade {
    private String classCode;
    private String courseNumber;
    private Integer units;
    private Double prelimGrade;
    private Double midtermGrade;
    private Double tentativeFinalGrade;
    private String finalGrade;
    private String weights;
    private String studentNumber;
    private String semester;
    private String academicYear;
    
    public Grade() {
    }
    
    public Grade(String classCode, String courseNumber, Integer units, 
                 Double prelimGrade, Double midtermGrade, Double tentativeFinalGrade, 
                 String finalGrade, String weights) {
        this.classCode = classCode;
        this.courseNumber = courseNumber;
        this.units = units;
        this.prelimGrade = prelimGrade;
        this.midtermGrade = midtermGrade;
        this.tentativeFinalGrade = tentativeFinalGrade;
        this.finalGrade = finalGrade;
        this.weights = weights;
    }
    
    public Grade(String classCode, String courseNumber, Integer units, 
                 Double prelimGrade, Double midtermGrade, Double tentativeFinalGrade, 
                 String finalGrade, String weights, String studentNumber) {
        this(classCode, courseNumber, units, prelimGrade, midtermGrade, tentativeFinalGrade, finalGrade, weights);
        this.studentNumber = studentNumber;
    }
    
    // Getters and Setters
    public String getClassCode() {
        return classCode;
    }
    
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
    
    public String getCourseNumber() {
        return courseNumber;
    }
    
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }
    
    public Integer getUnits() {
        return units;
    }
    
    public void setUnits(Integer units) {
        this.units = units;
    }
    
    public Double getPrelimGrade() {
        return prelimGrade;
    }
    
    public void setPrelimGrade(Double prelimGrade) {
        this.prelimGrade = prelimGrade;
    }
    
    public Double getMidtermGrade() {
        return midtermGrade;
    }
    
    public void setMidtermGrade(Double midtermGrade) {
        this.midtermGrade = midtermGrade;
    }
    
    public Double getTentativeFinalGrade() {
        return tentativeFinalGrade;
    }
    
    public void setTentativeFinalGrade(Double tentativeFinalGrade) {
        this.tentativeFinalGrade = tentativeFinalGrade;
    }
    
    public String getFinalGrade() {
        return finalGrade;
    }
    
    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }
    
    public String getWeights() {
        return weights;
    }
    
    public void setWeights(String weights) {
        this.weights = weights;
    }
    
    public String getStudentNumber() {
        return studentNumber;
    }
    
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getAcademicYear() {
        return academicYear;
    }
    
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
    
    // Utility methods
    public boolean isPassed() {
        if (prelimGrade != null && prelimGrade >= 75.0) return true;
        if (midtermGrade != null && midtermGrade >= 75.0) return true;
        if (tentativeFinalGrade != null && tentativeFinalGrade >= 75.0) return true;
        if (finalGrade != null && (finalGrade.equals("P") || finalGrade.equals("HP"))) return true;
        return false;
    }
    
    public boolean isFailed() {
        if (prelimGrade != null && prelimGrade < 75.0) return true;
        if (midtermGrade != null && midtermGrade < 75.0) return true;
        if (tentativeFinalGrade != null && tentativeFinalGrade < 75.0) return true;
        if (finalGrade != null && finalGrade.equals("F")) return true;
        return false;
    }
    
    public boolean isIncomplete() {
        return finalGrade != null && finalGrade.equals("INC");
    }
    
    public boolean isDropped() {
        return finalGrade != null && finalGrade.equals("D");
    }
    
    public boolean hasGrades() {
        return prelimGrade != null || midtermGrade != null || 
               tentativeFinalGrade != null || finalGrade != null;
    }
    
    public double getAverageGrade() {
        double total = 0.0;
        int count = 0;
        
        if (prelimGrade != null) {
            total += prelimGrade;
            count++;
        }
        if (midtermGrade != null) {
            total += midtermGrade;
            count++;
        }
        if (tentativeFinalGrade != null) {
            total += tentativeFinalGrade;
            count++;
        }
        
        return count > 0 ? total / count : 0.0;
    }
    
    @Override
    public String toString() {
        return "Grade{" +
                "classCode='" + classCode + '\'' +
                ", courseNumber='" + courseNumber + '\'' +
                ", units=" + units +
                ", prelimGrade=" + prelimGrade +
                ", midtermGrade=" + midtermGrade +
                ", tentativeFinalGrade=" + tentativeFinalGrade +
                ", finalGrade='" + finalGrade + '\'' +
                ", weights='" + weights + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Grade grade = (Grade) obj;
        return classCode != null ? classCode.equals(grade.classCode) : grade.classCode == null;
    }
    
    @Override
    public int hashCode() {
        return classCode != null ? classCode.hashCode() : 0;
    }
}