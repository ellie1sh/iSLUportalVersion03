package edu.slu.grades;

/**
 * Student data model class
 * Represents a student in the grades system
 */
public class Student {
    private String studentNumber;
    private String firstName;
    private String lastName;
    private String course;
    private String yearLevel;
    private String middleName;
    
    public Student() {
    }
    
    public Student(String studentNumber, String firstName, String lastName, String course, String yearLevel) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.course = course;
        this.yearLevel = yearLevel;
    }
    
    public Student(String studentNumber, String firstName, String middleName, String lastName, String course, String yearLevel) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.course = course;
        this.yearLevel = yearLevel;
    }
    
    // Getters and Setters
    public String getStudentNumber() {
        return studentNumber;
    }
    
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public String getCourse() {
        return course;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    public String getYearLevel() {
        return yearLevel;
    }
    
    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }
    
    public String getFullName() {
        if (middleName != null && !middleName.trim().isEmpty()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }
    
    public String getDisplayInfo() {
        return studentNumber + " | " + course + " " + yearLevel;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentNumber='" + studentNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", course='" + course + '\'' +
                ", yearLevel='" + yearLevel + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentNumber != null ? studentNumber.equals(student.studentNumber) : student.studentNumber == null;
    }
    
    @Override
    public int hashCode() {
        return studentNumber != null ? studentNumber.hashCode() : 0;
    }
}