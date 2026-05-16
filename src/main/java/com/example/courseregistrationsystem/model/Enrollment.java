package com.example.courseregistrationsystem.model;

    public class Enrollment {
        private String studentId;
        private String courseCode;
        private String grade;
        private String id;
        private String studentName;
        private int marks;
        private String semester;
        private String year;


        public Enrollment() {

        }

        public Enrollment(String studentId, String courseCode, String grade) {
            this.studentId = studentId;
            this.courseCode = courseCode;
            this.grade = grade;
        }

        // Getters and Setters
        public String getStudentId() {
            return studentId;
        }
        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getCourseCode() {
            return courseCode;
        }
        public void setCourseCode(String courseCode) {
            this.courseCode = courseCode;
        }

        public String getGrade() {
            return grade;
        }
        public void setGrade(String grade) {
            this.grade = grade;
        }
    }


