package com.example.attendance;

public class Course {



    private String CourseName;

    public Course(String courseName, String courseID) {
        CourseName = courseName;
        CourseID = courseID;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public void setCourseID(String courseID) {
        CourseID = courseID;
    }

    private String CourseID;

    public String getCourseName() {
        return CourseName;
    }

    public String getCourseID() {
        return CourseID;
    }
}
