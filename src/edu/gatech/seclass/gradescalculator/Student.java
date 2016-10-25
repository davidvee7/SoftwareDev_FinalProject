package edu.gatech.seclass.gradescalculator;

import java.io.IOException;

public class Student {

	public String name;
	public String gtID;
	private String team;
	private int attendance;
	private Course course;
	private String email;
	
	public Student(String studentName, String gtID, Course course) {
		this.setName(studentName);
		this.setGtid(gtID);
		this.setCourse(course);
		this.setTeam(Course.findTeamforStudent(this.name));
	}

	public Student() {
		// TODO Auto-generated constructor stub
	}

	public Student(String studentName, String studentID) {
		this.setName(studentName);
		this.setGtid(studentID);
		this.setTeam(Course.findTeamforStudent(this.name));

	}

	private void setCourse(Course course2) {
		 course = course2;
	}

	public String getGtid() {
		
		return this.gtID;
	}

	public void setGtid(String gtID){
		this.gtID = gtID;
	}
	

	public String getName() {
		
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getTeam() {
	
		return this.team;
	}
	
	public int getAttendance(){
		return this.attendance;
	}
	
	public void setAttendance(int i){
		
		this.attendance =  i;
	}

	public void setTeam(String teamName) {
		this.team = teamName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

}
