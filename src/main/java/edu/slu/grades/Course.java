package edu.slu.grades;

/**
 * Course data model class
 * Represents a course in the academic system
 */
public class Course {
    private String courseCode;
    private String courseName;
    private String description;
    private Integer units;
    private String department;
    private String prerequisite;
    private String semester;
    private String academicYear;
    private String instructor;
    private String schedule;
    private String room;
    
    public Course() {
    }
    
    public Course(String courseCode, String courseName, Integer units) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.units = units;
    }
    
    public Course(String courseCode, String courseName, String description, Integer units, String department) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.units = units;
        this.department = department;
    }
    
    // Getters and Setters
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getUnits() {
        return units;
    }
    
    public void setUnits(Integer units) {
        this.units = units;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getPrerequisite() {
        return prerequisite;
    }
    
    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
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
    
    public String getInstructor() {
        return instructor;
    }
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    public String getRoom() {
        return room;
    }
    
    public void setRoom(String room) {
        this.room = room;
    }
    
    // Utility methods
    public String getFullCourseInfo() {
        return courseCode + " - " + courseName;
    }
    
    public String getCourseDetails() {
        StringBuilder details = new StringBuilder();
        details.append(courseCode).append(" - ").append(courseName);
        if (units != null) {
            details.append(" (").append(units).append(" units)");
        }
        if (instructor != null && !instructor.trim().isEmpty()) {
            details.append(" - ").append(instructor);
        }
        return details.toString();
    }
    
    public boolean isLaboratory() {
        return courseCode != null && courseCode.toUpperCase().endsWith("L");
    }
    
    public boolean hasPrerequisite() {
        return prerequisite != null && !prerequisite.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", units=" + units +
                ", department='" + department + '\'' +
                ", prerequisite='" + prerequisite + '\'' +
                ", semester='" + semester + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", instructor='" + instructor + '\'' +
                ", schedule='" + schedule + '\'' +
                ", room='" + room + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseCode != null ? courseCode.equals(course.courseCode) : course.courseCode == null;
    }
    
    @Override
    public int hashCode() {
        return courseCode != null ? courseCode.hashCode() : 0;
    }
}