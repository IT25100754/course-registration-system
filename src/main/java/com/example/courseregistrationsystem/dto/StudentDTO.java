package com.example.courseregistrationsystem.dto;


    public class StudentDTO {

        public static class LoginRequest {
            private String email;
            private String password;

            public String getEmail()                 { return email; }
            public void   setEmail(String email)     { this.email = email; }
            public String getPassword()              { return password; }
            public void   setPassword(String pwd)    { this.password = pwd; }
        }


        public static class RegisterRequest {
            private String name;
            private String email;
            private String password;
            private String studentId;
            private String phone;

            public String getName()                  { return name; }
            public void   setName(String n)          { this.name = n; }
            public String getEmail()                 { return email; }
            public void   setEmail(String e)         { this.email = e; }
            public String getPassword()              { return password; }
            public void   setPassword(String p)      { this.password = p; }
            public String getStudentId()             { return studentId; }
            public void   setStudentId(String sid)   { this.studentId = sid; }
            public String getPhone()                 { return phone; }
            public void   setPhone(String ph)        { this.phone = ph; }
        }


        public static class ProfileResponse {
            private String id;
            private String name;
            private String email;
            private String studentId;
            private String phone;
            private String faculty;
            private String createdAt;
            private boolean success;
            private String message;


            public String  getId()                   { return id; }
            public void    setId(String id)          { this.id = id; }
            public String  getName()                 { return name; }
            public void    setName(String n)         { this.name = n; }
            public String  getEmail()                { return email; }
            public void    setEmail(String e)        { this.email = e; }
            public String  getStudentId()            { return studentId; }
            public void    setStudentId(String s)    { this.studentId = s; }
            public String  getPhone()                { return phone; }
            public void    setPhone(String p)        { this.phone = p; }
            public String  getFaculty()              { return faculty; }
            public void    setFaculty(String f)      { this.faculty = f; }
            public String  getCreatedAt()            { return createdAt; }
            public void    setCreatedAt(String c)    { this.createdAt = c; }
            public boolean isSuccess()               { return success; }
            public void    setSuccess(boolean s)     { this.success = s; }
            public String  getMessage()              { return message; }
            public void    setMessage(String m)      { this.message = m; }
        }

        public static class ApiResponse {
            private boolean success;
            private String  message;
            private Object  data;

            public ApiResponse() {}
            public ApiResponse(boolean success, String message) {
                this.success = success;
                this.message = message;
            }
            public ApiResponse(boolean success, String message, Object data) {
                this.success = success;
                this.message = message;
                this.data    = data;
            }

            public boolean isSuccess()              { return success; }
            public void    setSuccess(boolean s)    { this.success = s; }
            public String  getMessage()             { return message; }
            public void    setMessage(String m)     { this.message = m; }
            public Object  getData()                { return data; }
            public void    setData(Object d)        { this.data = d; }
        }

        public static class UpdatePhoneRequest {
            private String studentId;
            private String phone;

            public String getStudentId()             { return studentId; }
            public void   setStudentId(String s)     { this.studentId = s; }
            public String getPhone()                 { return phone; }
            public void   setPhone(String p)         { this.phone = p; }
        }

        public static class ChangePasswordRequest {
            private String studentId;
            private String currentPassword;
            private String newPassword;
            private String confirmPassword;

            public String getStudentId()                    { return studentId; }
            public void   setStudentId(String s)            { this.studentId = s; }
            public String getCurrentPassword()              { return currentPassword; }
            public void   setCurrentPassword(String c)      { this.currentPassword = c; }
            public String getNewPassword()                  { return newPassword; }
            public void   setNewPassword(String n)          { this.newPassword = n; }
            public String getConfirmPassword()              { return confirmPassword; }
            public void   setConfirmPassword(String cf)     { this.confirmPassword = cf; }
        }

        public static class ResetPasswordRequest {
            private String email;
            private String newPassword;
            private String confirmPassword;

            public String getEmail()                        { return email; }
            public void   setEmail(String e)                { this.email = e; }
            public String getNewPassword()                  { return newPassword; }
            public void   setNewPassword(String n)          { this.newPassword = n; }
            public String getConfirmPassword()              { return confirmPassword; }
            public void   setConfirmPassword(String c)      { this.confirmPassword = c; }
        }

        public static class DashboardResponse {
            private String studentName;
            private String studentId;
            private String faculty;
            private int    enrolledCourseCount;
            private double gpa;
            private int    totalCredits;
            private double averageScore;
            private java.util.List<GradeInfo> grades;

            public String  getStudentName()                            { return studentName; }
            public void    setStudentName(String n)                    { this.studentName = n; }
            public String  getStudentId()                              { return studentId; }
            public void    setStudentId(String s)                      { this.studentId = s; }
            public String  getFaculty()                                { return faculty; }
            public void    setFaculty(String f)                        { this.faculty = f; }
            public int     getEnrolledCourseCount()                    { return enrolledCourseCount; }
            public void    setEnrolledCourseCount(int c)               { this.enrolledCourseCount = c; }
            public double  getGpa()                                    { return gpa; }
            public void    setGpa(double g)                            { this.gpa = g; }
            public int     getTotalCredits()                           { return totalCredits; }
            public void    setTotalCredits(int t)                      { this.totalCredits = t; }
            public double  getAverageScore()                           { return averageScore; }
            public void    setAverageScore(double a)                   { this.averageScore = a; }
            public java.util.List<GradeInfo> getGrades()              { return grades; }
            public void    setGrades(java.util.List<GradeInfo> g)     { this.grades = g; }


            public static class GradeInfo {
                private String courseName;
                private String courseCode;
                private int    marks;
                private String grade;
                private String status;

                public String getCourseName()               { return courseName; }
                public void   setCourseName(String c)       { this.courseName = c; }
                public String getCourseCode()               { return courseCode; }
                public void   setCourseCode(String c)       { this.courseCode = c; }
                public int    getMarks()                    { return marks; }
                public void   setMarks(int m)               { this.marks = m; }
                public String getGrade()                    { return grade; }
                public void   setGrade(String g)            { this.grade = g; }
                public String getStatus()                   { return status; }
                public void   setStatus(String s)           { this.status = s; }
            }
        }
    }
