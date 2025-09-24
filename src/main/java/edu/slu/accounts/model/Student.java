package edu.slu.accounts.model;

import java.time.LocalDateTime;

/**
 * Student model class representing a university student
 */
public class Student {
    private String id;
    private String studentNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String course;
    private int yearLevel;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    // Constructors
    public Student() {
        this.createdAt = LocalDateTime.now();
    }

    public Student(String studentNumber, String firstName, String middleName, String lastName, 
                   String course, int yearLevel, String email, String password) {
        this();
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.course = course;
        this.yearLevel = yearLevel;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getYearLevel() { return yearLevel; }
    public void setYearLevel(int yearLevel) { this.yearLevel = yearLevel; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Utility methods
    public String getFullName() {
        StringBuilder fullName = new StringBuilder(firstName);
        if (middleName != null && !middleName.trim().isEmpty()) {
            fullName.append(" ").append(middleName);
        }
        fullName.append(" ").append(lastName);
        return fullName.toString();
    }

    public String getInitials() {
        StringBuilder initials = new StringBuilder();
        initials.append(firstName.charAt(0));
        if (middleName != null && !middleName.trim().isEmpty()) {
            initials.append(middleName.charAt(0));
        }
        initials.append(lastName.charAt(0));
        return initials.toString().toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', studentNumber='%s', name='%s', course='%s %d'}", 
                           id, studentNumber, getFullName(), course, yearLevel);
    }
}