package com.erp.student_erp.userDTO;
//StudentMarksResponse.java
public class StudentMarksResponse {

 private String course;
 private int internal;
 private int external;
 public String getCourse() {
	return course;
}

public void setCourse(String course) {
	this.course = course;
}

public int getInternal() {
	return internal;
}

public void setInternal(int internal) {
	this.internal = internal;
}

public int getExternal() {
	return external;
}

public void setExternal(int external) {
	this.external = external;
}

public int getTotal() {
	return total;
}

public void setTotal(int total) {
	this.total = total;
}

public String getGrade() {
	return grade;
}

public void setGrade(String grade) {
	this.grade = grade;
}

private int total;
 private String grade;

 public StudentMarksResponse(String course, int internal, int external, int total, String grade) {
     this.course = course;
     this.internal = internal;
     this.external = external;
     this.total = total;
     this.grade = grade;
 }

 // getters
}
