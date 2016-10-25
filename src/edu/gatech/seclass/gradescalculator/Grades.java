package edu.gatech.seclass.gradescalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Grades {

	private static final int CELL_TYPE_NUMERIC = 0;
	private FileInputStream file;
	private XSSFWorkbook workbook;
	String gradesDb;
	String formula;
	
	public Grades(String gradesDb) {
		this.gradesDb = gradesDb;
		formula = "AT * 0.2 + AA * 0.4 + AP * 0.4";
	
	}
//Returns the number of assignmetns columns in the excel.
	public int getNumAssignments() {
		openGradesDB();
		XSSFSheet assignmentsSheet = this.workbook.getSheetAt(1);
		
		Iterator<Row> rowIterator = assignmentsSheet.iterator();
		
		int cellCount = 0;
		int column;
		
		int assignmentCount=0;
		while (rowIterator.hasNext()){
			Row row = rowIterator.next();
			
			//rowCount +=1;
			column = 0;
			
			Iterator<Cell> cellIterator = row.cellIterator();
			while(cellIterator.hasNext()) {
	            Cell cell = cellIterator.next();
	           /// System.out.println("The cell iterator had at least " + cellCount + " next");
				if (column == 0){
					        
				}
				else {
					assignmentCount +=1;
				}
	             
	            column+=1;
	        }
			//breaks after firs titeration through each column; should be fine for purposes of getting number of collumns
			break;
			}
		try {
			file.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return assignmentCount;

	}
private void openGradesDB() {
	try {
		this.file = new FileInputStream(new File(gradesDb));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	
	try {
		this.workbook = new XSSFWorkbook (file);
	} catch (IOException e) {
		e.printStackTrace();
	}
}
	
//Returns the number of project columns in the excel.
	public int getNumProjects() throws IOException {
		this.file = new FileInputStream(new File(gradesDb));
		
		this.workbook = new XSSFWorkbook (file);
		XSSFSheet projectSheet = this.workbook.getSheetAt(2);
		
		Iterator<Row> rowIterator = projectSheet.iterator();
		
		int cellCount = 0;
		int column;
		
		int projectCount=0;
		while (rowIterator.hasNext()){
			Row row = rowIterator.next();
			
			//rowCount +=1;
			column = 0;
			
			Iterator<Cell> cellIterator = row.cellIterator();
			while(cellIterator.hasNext()) {
	            Cell cell = cellIterator.next();
	           /// System.out.println("The cell iterator had at least " + cellCount + " next");
				if (column == 0){
					        
				}
				else {
					projectCount +=1;
				}
	             
	            column+=1;
	        }
			//breaks after firs titeration through each column; should be fine for purposes of getting number of collumns
			break;
			}
		file.close();
		return projectCount;		
	}
	
	//Sets student attendance for all students in the hashset. Does this by iterating through 
	//the list of students in studentsHash, and when the student in studenthash is in the cell
	//that the method is searching for, it sets the students attendance.
	public void setStudentAttendance(HashSet<Student> studentsHash) throws IOException{
		//Students students = new Students(CourseTest.STUDENTS_DB);
		
		this.openGradesDB();

		XSSFSheet attendanceSheet = this.workbook.getSheetAt(0);
		//System.out.println("bout to go through Studentshas");
		int studentCountofStudentsHash = 0 ;
		for (Student s : studentsHash){
			//System.out.println("There's at least one student in studentshash");
			boolean studentFound = false;

			Iterator<Row> rowIterator = attendanceSheet.iterator();
			
			int cellCount = 0;
			int column;
			int attendanceRowCount = 0;
			while (rowIterator.hasNext()){
				Row row = rowIterator.next();
				if (attendanceRowCount == 0){
					attendanceRowCount +=1;
					continue;
				}
				//rowCount +=1;
				column = 0;
				
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
		            Cell cell = cellIterator.next();
		           /// System.out.println("The cell iterator had at least " + cellCount + " next");
					if (column == 0){
						//System.out.println(cell.getNumericCellValue() + "couldn't be found");
						int gtID =  (int) cell.getNumericCellValue();
						if (!(String.valueOf(gtID).trim()).equals( s.gtID.trim())){
							//System.out.println("excel gtid " + (String.valueOf(gtID)) + " <> " + s.gtID );
							
							continue;
						}	
					}
						else {
						
							//System.out.println("Setting "+ s.getName() + "attendance to " + (int)cell.getNumericCellValue());
							s.setAttendance((int)cell.getNumericCellValue());
							studentFound = true;
							break;
						}		             
		            column+=1;
		        }
				if (studentFound == true ){
					break;
				}
				
				}
			studentCountofStudentsHash+=1;
			//System.out.println(studentCountofStudentsHash);
			}
		
	file.close();
	}
	
	//open up the assignments tab.  find the first open assignemtn cell. write to cell.
	public void addAssignment(String assignmentName) {
		int lastCell = this.getNumAssignments();
		openGradesDB();
		XSSFSheet assignmentsSheet = this.workbook.getSheetAt(1);
		Cell cell = null;
		Row row = assignmentsSheet.getRow(0);
		row.createCell(lastCell+1).setCellValue(assignmentName);
		try {
			this.file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//updateGrades(); commented this out since test file calls updateGrades for me with necessary param
	}
	
	void updateGrades(Grades localGradesDb) {
		OutputStream outFile = null;

		try {
			 outFile  = new FileOutputStream(localGradesDb.gradesDb);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		try {
			this.workbook.write(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.workbook = new XSSFWorkbook(new FileInputStream(localGradesDb.gradesDb));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Open the assignemnts excel sheet.
	//Iterate through excel and upon finding student in students hashmap update the exel
	public void addGradesForAssignment(String assignmentName, HashMap<Student, Integer> grades2) {
		XSSFSheet assignmentSheet = this.workbook.getSheetAt(1);
		Iterator it = grades2.entrySet().iterator();
		int columnToUpDate = this.getColumntoUpdate(assignmentName, assignmentSheet);
		//System.out.println("bout to go through Studentshas");
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			Student s = (Student) pair.getKey();
			//sSystem.out.println("Iterating thru hashmap just got the student or key");
			Iterator<Row> rowIterator = assignmentSheet.iterator();
			
			int cellCount = 0;
			int column;
			int assignmentGradeRowCount = 0;
			while (rowIterator.hasNext()){
				Row row = rowIterator.next();
				if (assignmentGradeRowCount == 0){
					assignmentGradeRowCount +=1;
					continue;
				}
				//rowCount +=1;
				int gtID =  (int) row.getCell(0).getNumericCellValue();
				String studentID = s.getGtid().trim();
				String gtIDString = String.valueOf(gtID);
				//System.out.println("gtID from excel is " + gtID);
				
				if (gtID == Integer.parseInt(s.getGtid().trim())){
					//System.out.println("found one ! " + s.getGtid() );
					if (row.getCell(columnToUpDate)==null){
						Cell cell = row.createCell(columnToUpDate);
						cell.setCellType(CELL_TYPE_NUMERIC);
						cell.setCellValue( (int)pair.getValue());
						//System.out.println("Columnoupdate -1 was null, updated indicontrib");

					}
					else{
						Cell cell = row.getCell(columnToUpDate);
						cell.setCellValue((int)pair.getValue());
						//System.out.println("Columnoupdate -1 was not null, updated indicontrib");

					}
					break;
				}
				else{
				//	System.out.println("Excel gtID " + String.valueOf(gtID).trim() + " != student ID "
					//		+ s.getGtid().trim());
				}
			}
		}
	try {
		file.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
		
	}
		
	int getColumntoUpdate(String assignmentName, XSSFSheet assignmentSheet){
		
		
		Iterator<Row> rowIterator = assignmentSheet.iterator();
		
		int cellCount = 0;
		int column=0;
		
		int assignmentCount=0;
		while (rowIterator.hasNext()){
			Row row = rowIterator.next();
			
			//rowCount +=1;
			column = 0;
			
			Iterator<Cell> cellIterator = row.cellIterator();
			while(cellIterator.hasNext()) {
	            Cell cell = cellIterator.next();
	           /// System.out.println("The cell iterator had at least " + cellCount + " next");
	            String cellValue = cell.getStringCellValue();
				if (cellValue.equals( assignmentName)){
					     return column;
				}
//				else{
//					
//				}
	             
	            column+=1;
	        }
			//breaks after firs titeration through each column; should be fine for purposes of getting number of collumns
			break;
			}
		return column;
		
	}
	
	//iterate through sheet of students assignemnts grades until student s is found.  then iterate through
	// each cell, getting its values, and then average the values, rounding to 0 decimal places.
	public int getAverageAssignmentGrade(Student student) {
		this.openGradesDB();
		int averageAssignmentGrade = 0;
		int assignmentsSum = 0;
		//this is the number of assignments ultimately.
		int column = 0;
		XSSFSheet assignmentSheet = this.workbook.getSheetAt(1);
		Iterator<Row> rowIterator = assignmentSheet.iterator();
		int assignmentGradeRowCount = 0;

		while (rowIterator.hasNext()){
			Row row = rowIterator.next();
			if (assignmentGradeRowCount == 0){
				assignmentGradeRowCount +=1;
				continue;
			}
			//rowCount +=1;
			int gtID =  (int) row.getCell(0).getNumericCellValue();
			//String studentID = student.getGtid().trim();
			//String gtIDString = String.valueOf(gtID);
			//System.out.println("gtID from excel is " + gtID);
			
			if (gtID == Integer.parseInt(student.getGtid().trim())){
					//Iterate through cells and compute average
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()) {
						if (column == 0){
				            Cell cell = cellIterator.next();
							column+=1;
							continue;
						}
			            Cell cell = cellIterator.next();
			           /// System.out.println("The cell iterator had at least " + cellCount + " next");
						assignmentsSum +=cell.getNumericCellValue();
						//System.out.println("Assignment Sum is " + assignmentsSum);
			             
			            column+=1;
			        }
					break;
				}
						             
		     }
				
			
	try {
		file.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	//averageAssignmentGrade = (int) roundUp(assignmentsSum, (column-1));
	double averageAssignmentGrade2 = ((double)assignmentsSum / (double)(column -1));
	averageAssignmentGrade2 = round(averageAssignmentGrade2, 0);
	
	return (int)averageAssignmentGrade2;
	}
	
	public static double round(double dubToRound, int roundToPlaces){
		BigDecimal bigD = new BigDecimal(dubToRound);
		bigD = bigD.setScale(roundToPlaces, RoundingMode.HALF_UP);
		
		return bigD.doubleValue();
	}
	
	//run student . get team and then search for team in grades db.  iterate through each cell once team/row
	// is found and sum up each grade.  divide by num columns minus 1.
	public int getAverageProjectsGrade(Student student) {
		String teamName = student.getTeam();
		this.openGradesDB();
		int averageProjectGrade = 0;
		double projectsSum = 0;
		//this is the number of assignments ultimately.
		int column = 0;
		XSSFSheet projectSheet = this.workbook.getSheetAt(3);
		Iterator<Row> rowIterator = projectSheet.iterator();
		int projectGradeRowCount = 0;

		while (rowIterator.hasNext()){
			Row row = rowIterator.next();
			if (projectGradeRowCount == 0){
				projectGradeRowCount +=1;
				continue;
			}
			//rowCount +=1;
			String excelTeam =  row.getCell(0).getStringCellValue();
			//String studentID = student.getGtid().trim();
			//String gtIDString = String.valueOf(gtID);
			//System.out.println("teamName from excel is " + excelTeam);
			
			if (excelTeam.equals(teamName)){
					//Iterate through cells and compute average
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()) {
						if (column == 0){
				            Cell cell = cellIterator.next();
							column+=1;
							continue;
						}
			            Cell cell = cellIterator.next();
			           /// System.out.println("The cell iterator had at least " + cellCount + " next");
			            double projectGrade = cell.getNumericCellValue();
			            double indiContrib = getIndividualContribution(student.gtID,column);
			            projectGrade = projectGrade/100.00;
			            indiContrib = indiContrib/100.00;
			            //System.out.println("indi contrib is " + indiContrib + "and projectGrade is " + projectGrade);
						projectsSum += (projectGrade * indiContrib);
						projectsSum = round(projectsSum, 2);
						//System.out.println("Project Sum is " + projectsSum);
			             
			            column+=1;
			        }
					break;
				}
			else{
				//System.out.println("excel team " + excelTeam +" is not equal to "+ teamName);
			}
			
		}
		     
				
			
	try {
		file.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	//averageAssignmentGrade = (int) roundUp(assignmentsSum, (column-1));
	
	double averageProjectGrade2 = (projectsSum / (double)(column -1));
	//System.out.println("aveageproejctgrade2 is "+ averageProjectGrade2);
	averageProjectGrade2 = averageProjectGrade2 * 100.00;
	//System.out.println("aveageproejctgrade2 * 100.00 is "+ averageProjectGrade2);

	averageProjectGrade2 = round(averageProjectGrade2, 0);
	
	return (int)averageProjectGrade2;
	}
	
	
	private double getIndividualContribution(String gtID, int project) {

		XSSFSheet indiSheet = this.workbook.getSheetAt(2);
		
	
			Iterator<Row> rowIterator = indiSheet.iterator();
			
			int cellCount = 0;
			int column;
			Student student = null;
			int indiRowCount = 0;
			while (rowIterator.hasNext()){
				Row row = rowIterator.next();
				if (indiRowCount == 0){
					indiRowCount +=1;
					continue;
				}
				//rowCount +=1;
				
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
		            Cell cell = cellIterator.next();
		           /// System.out.println("The cell iterator had at least " + cellCount + " next");
						//System.out.println(cell.getNumericCellValue() + "couldn't be found");
						int excelGTID =  (int) cell.getNumericCellValue();
						if (excelGTID == Integer.parseInt(gtID)){
							return row.getCell(project).getNumericCellValue();

						}	
					
						else {
						
							//System.out.println("excel gtid " + excelGTID + " <> " + Integer.parseInt(gtID) );
						continue;
						}		             
		        }
				}
			return 0;
			}
	
	
	//Find the project; this will be the column to update.  Then iterate through the GT ID's until the student
	// is found.  Create a cell, update the value of the cell at the column to update.
	public void addIndividualContributions(String projectName,
			HashMap<Student, Integer> contributions1) {
		this.openGradesDB();
		XSSFSheet indiContribsSheet = this.workbook.getSheetAt(2);
		Iterator it = contributions1.entrySet().iterator();
		int columnToUpDate = this.getColumntoUpdate(projectName, indiContribsSheet);
		
		//System.out.println("columnToUpDate  is this int " + columnToUpDate + " and projName is "
		//+ projectName);
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			Student s = (Student) pair.getKey();
			//System.out.println("Iterating thru hashmap just got the student or key for student named " + 
			//s.getName());
			Iterator<Row> rowIterator = indiContribsSheet.iterator();
			
			int cellCount = 0;
			int column;
			int indiGradeRowCount = 0;
			while (rowIterator.hasNext()){
				Row row = rowIterator.next();
				if (indiGradeRowCount == 0){
					indiGradeRowCount +=1;
					continue;
				}
				//rowCount +=1;
				if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC){
				//	System.out.println("this cell's numeric! ");
				}
				int gtID =  (int) row.getCell(0).getNumericCellValue();
				String studentID = s.getGtid().trim();
				String gtIDString = String.valueOf(gtID);
				//System.out.println("gtID from excel is " + gtID);
				
				if (gtID == Integer.parseInt(s.getGtid().trim())){
					//System.out.println("found one ! " + s.getGtid() );
					if (row.getCell(columnToUpDate)==null){
						Cell cell = row.createCell(columnToUpDate);
						cell.setCellType(CELL_TYPE_NUMERIC);
						cell.setCellValue( (int)pair.getValue());
						//System.out.println("Columnoupdate -1 was null, updated indicontrib");

					}
					else{
						Cell cell = row.getCell(columnToUpDate);
						cell.setCellValue((int)pair.getValue());
						//System.out.println("Columnoupdate -1 was not null, updated indicontrib");

					}
					break;
				}
				else{
					//System.out.println("Excel gtID " + String.valueOf(gtID).trim() + " != student ID "
					//		+ s.getGtid().trim());
				}
						             
		     }
				}
			
	try {
		file.close();
	} catch (IOException e) {
		e.printStackTrace();
	}		
	}
	
	public void setFormula(String string) {
		this.formula = string;
		
	}
	
	//Get avg proj grade, attendance, and average assignemtns grade.  Put those values into the 
	// script engine.  do i need to handle lower case?
	public int getOverallGrade(Student student) throws GradeFormulaException {
		ScriptEngineManager sce = new ScriptEngineManager();
		ScriptEngine se = sce.getEngineByName("js");
		int attendance = student.getAttendance(); //AT
		int avgProject = this.getAverageProjectsGrade(student); //AP
		int avgAssignment = this.getAverageAssignmentGrade(student); //AA
		
		se.put("AA", avgAssignment);
		se.put("AP",avgProject);
		se.put("AT", attendance);
		
		Object result = null;
		try {
			 result = se.eval("grade = "+ this.formula);
		} catch (ScriptException e) {
			GradeFormulaException gfe = new GradeFormulaException("Formula " + this.formula + " was no good.");
			e.printStackTrace();
		}
		
		if (result == null){
			throw new GradeFormulaException("Grade formula is invalid.");

		}
		
		result = round((Double) result, 0);
		Integer integerResult = ((Double) result).intValue();
		int i = (int) integerResult;
		return i;
	}
	public String getFormula() {
		return this.formula;
	}
	}

