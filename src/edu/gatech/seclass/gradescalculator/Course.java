package edu.gatech.seclass.gradescalculator;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
public class Course {

	 static Students students;
	private Grades grades;

	public Course(Students students, Grades grades) {
		this.grades = grades;
		this.students = students;
		try {
			setStudentAttendance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getNumStudents() {
		return students.getNumStudents();
	}

	public int getNumAssignments() {
		return grades.getNumAssignments();
	}

	public int getNumProjects() {
		int numProjects=0;
		try {
			numProjects =grades.getNumProjects();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numProjects;
	}

	public HashSet<Student> getStudents() {
		return students.getStudents();
	}

	public Student getStudentByName(String string) {
		return students.getStudentByName(string);
	}

	public Student getStudentByID(String string) {
		return students.getStudentByID(string);
	}
	
	//Sets attendance for all students in studentsHash.
	public void setStudentAttendance() throws IOException{
		HashSet<Student> studentsHash = this.students.getStudents();
		
		grades.setStudentAttendance(studentsHash);
			
		}

	public void addAssignment(String assignmentName) {
		this.grades.addAssignment(assignmentName);
		
	}

	public void updateGrades(Grades grades2) {
		this.grades.updateGrades(grades2);
		
	}

	public void addGradesForAssignment(String assignmentName,
			HashMap<Student, Integer> grades2) {
		this.grades.addGradesForAssignment(assignmentName, grades2);
		
	}

	public int getAverageAssignmentsGrade(Student student) {
		return this.grades.getAverageAssignmentGrade(student);
	}

	public Object getAverageProjectsGrade(Student student) {
		return this.grades.getAverageProjectsGrade(student);
	}

	public void addIndividualContributions(String projectName1,
			HashMap<Student, Integer> contributions1) {
		this.grades.addIndividualContributions(projectName1, contributions1);
		
	}

	public static String findTeamforStudent(String name) {
		return students.findTeamforStudent(name);
		
	}

	public int getAttendance(Student student) {
		return student.getAttendance();
	}

	public String getTeam(Student student) {
		return student.getTeam();
	}

	public int getOverallGrade(Student student) {
		// TODO Auto-generated method stub
		return this.grades.getOverallGrade(student);
	}

	public String getFormula() {
		return this.grades.getFormula();
	}

	public String getEmail(Student student) {
		return students.getEmail(student);
	}

	public void setFormula(String newFormula) {
		this.grades.formula = newFormula;
	}
	

}
