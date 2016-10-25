package edu.gatech.seclass.gradescalculator;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyCourseTest {

    Students students = null;
    Grades grades = null;
    Course course = null;
    static final String GRADES_DB = "DB" + File.separator + "GradesDatabase6300-grades.xlsx";
    static final String GRADES_DB_GOLDEN = "DB" + File.separator + "GradesDatabase6300-grades-goldenversion.xlsx";
    static final String STUDENTS_DB = "DB" + File.separator + "GradesDatabase6300-students.xlsx";
    static final String STUDENTS_DB_GOLDEN = "DB" + File.separator + "GradesDatabase6300-students-goldenversion.xlsx";
    
    @Before
    public void setUp() throws Exception {
        FileSystem fs = FileSystems.getDefault();
        Path gradesdbfilegolden = fs.getPath(GRADES_DB_GOLDEN);
        Path gradesdbfile = fs.getPath(GRADES_DB);
        Files.copy(gradesdbfilegolden, gradesdbfile, REPLACE_EXISTING);
        Path studentsdbfilegolden = fs.getPath(STUDENTS_DB_GOLDEN);
        Path studentsdbfile = fs.getPath(STUDENTS_DB);
        Files.copy(studentsdbfilegolden, studentsdbfile, REPLACE_EXISTING);    	
    	students = new Students(STUDENTS_DB);
        grades = new Grades(GRADES_DB);
        course = new Course(students, grades);
    }

    @After
    public void tearDown() throws Exception {
        students = null;
        grades = null;
        course = null;
    }
    
    @Test
    public void testAddStudent() {
    	int numStudentsBefore = course.students.getNumStudents();
        course.addStudent("David Vinegar", "901234599");
        course.updateStudents(new Students(STUDENTS_DB));
        int numStudentsAfter = course.students.getNumStudents();
        assertEquals(numStudentsBefore +1, numStudentsAfter);
        numStudentsBefore = numStudentsAfter;
        course.addStudent("David's Clone", "901234599");
        course.updateStudents(new Students(STUDENTS_DB));
        numStudentsAfter = course.students.getNumStudents();
        assertEquals(numStudentsBefore +1, numStudentsAfter);
    }
    
    @Test
    public void testAddProject() {
    	course.addProject("Project Cuatro!");
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(4, course.getNumProjects());
        course.addProject("Project Cinco!");
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(5, course.getNumProjects());
    }
    
    @Test
    public void testAddGradesForProject() {
        String projectName = "Project 4";
        Student student1 = new Student("Josepha Jube", "901234502", course);
        Student student2 = new Student("Christine Schaeffer", "901234508", course);
        Student student3 = new Student("Ernesta Anderson", "901234510", course);
        Integer[] student1ProjAndIndiGrade = {87,98};
        Integer[] student2ProjAndIndiGrade = {47,88};
        Integer[] student3ProjAndIndiGrade = {94,91};
        course.addProject(projectName);
        course.updateGrades(new Grades(GRADES_DB));
        HashMap<Student, Integer[]> grades = new HashMap<Student, Integer[]>();
        grades.put(student1, student1ProjAndIndiGrade);
        grades.put(student2, student2ProjAndIndiGrade);
        grades.put(student3, student3ProjAndIndiGrade);
        course.addGradesForProject(projectName, grades);
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(84, course.getAverageProjectsGrade(student1));
        assertEquals(60, course.getAverageProjectsGrade(student2));
        assertEquals(85, course.getAverageProjectsGrade(student3));
    }
}