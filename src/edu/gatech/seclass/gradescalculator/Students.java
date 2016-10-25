package edu.gatech.seclass.gradescalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Students {

	private int rowCount;
	FileInputStream file;
	XSSFWorkbook workbook;
	HashSet<Student>  students= new HashSet();
	String studentsDb;

	//Should make a new Students instance
	public Students(String studentsDb) {
		this.studentsDb = studentsDb;
		
		rowCount = 0;
		
		try {
			studentHashSet();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			addTeamtoStudent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	//Iterates through each team and assigns the team to a student. 
	 void addTeamtoStudent() throws IOException {
		this.file = new FileInputStream(new File(this.studentsDb));
		
		this.workbook = new XSSFWorkbook (file);
		
		XSSFSheet teamSheet = workbook.getSheetAt(1);

		Iterator<Row> rowIterator = teamSheet.iterator();
		
		int column;
		String teamName = "";
		while (rowIterator.hasNext()){
			Row row = rowIterator.next();
			//rowCount +=1;
			column = 0;
			
			Iterator<Cell> cellIterator = row.cellIterator();
			while(cellIterator.hasNext()) {
	            Cell cell = cellIterator.next();

				if (column == 0){
	            	teamName = cell.getStringCellValue();
	            }
				else {
					//System.out.println("Made it into clause where team assignd to stduent");
					if (this.getStudentByName(cell.getStringCellValue())!=null){
						(this.getStudentByName(cell.getStringCellValue())).setTeam(teamName);
					}
				}
	             
	            column+=1;
	        }
			}
			file.close();
	    }		


//Creates a hashset of students.
	private void studentHashSet() throws IOException {
		this.file = new FileInputStream(new File(this.studentsDb));
			
		this.workbook = new XSSFWorkbook (file);
		
		XSSFSheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIterator = sheet.iterator();
				
		int column = 0;
		rowCount = 0;
		while (rowIterator.hasNext()){
			Student student = new Student();
			Row row = rowIterator.next();
			rowCount +=1;
			column = 0;
			Iterator<Cell> cellIterator = row.cellIterator();
			while(cellIterator.hasNext()) {
	            
	            Cell cell = cellIterator.next();
	             
	            handleStudentInfoCell(column, student, cell);
	            column+=1;
	        }
	       // System.out.println("");
			//Don't want to include the first row with column titles
			if (rowCount != 1 && rowCount !=0){
				this.students.add(student);
			}
	    }		
	    file.close();
	}


// refactored out the handling of student info (checking for cell type and setting student id values, etc).
	private void handleStudentInfoCell(int column, Student student, Cell cell) {
		switch(cell.getCellType()) {
		    case Cell.CELL_TYPE_BOOLEAN:
		        //System.out.print(cell.getBooleanCellValue() + "\t\t");
		        break;
		    case Cell.CELL_TYPE_NUMERIC:
		    	if (column ==1){
		    		student.setGtid(String.valueOf((int)cell.getNumericCellValue()));	                			                		
		    	}
		        break;
		    case Cell.CELL_TYPE_STRING:
		      //  System.out.print(cell.getStringCellValue() + "\t\t");
		    	if (column == 0){
		    		student.setName(cell.getStringCellValue());
		    	}
		    	if (column ==2){
		    		//System.out.println("email abou to get set");
		    		student.setEmail(cell.getStringCellValue());
		    	}
		        break;
		}
	}


//Returns the number of students in the excel by accessing the students hashset.
	public int getNumStudents() {
		return students.size();
	}

	//Retruns a hashset of student objects.
	public HashSet<Student> getStudents() {
		
		return students;
	}

	public Student getStudentByName(String string) {
		Student student = null;
		for (Student s : this.students) {
        	//System.out.println(s.getName());
            if (s.getName().compareTo(string) == 0) {
                student = s;
                break;
            }
        }		
		return student;
	}

	public Student getStudentByID(String string) {
		// TODO Auto-generated method stub
		Student student = null;
		for (Student s : this.students) {
        	//System.out.println(s.getName());
            if (s.getGtid().compareTo(string) == 0) {
                student = s;
                break;
            }
        }		
		return student;
		}


	public String findTeamforStudent(String name) {
		this.openStudentsDB();
		int column = 0;
		XSSFSheet teamSheet = this.workbook.getSheetAt(1);
		Iterator<Row> rowIterator = teamSheet.iterator();
		int teamRowCount = 0;
		String excelName = "";
		while (rowIterator.hasNext()){
			Row row = rowIterator.next();
			if (teamRowCount == 0){
				teamRowCount +=1;
				continue;
			}
			String excelTeam =  row.getCell(0).getStringCellValue();
			
			//System.out.println("teamName from excel is " + excelTeam);
			
					//Iterate through cells and compute average
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()) {
						if (column == 0){
				            Cell cell = cellIterator.next();
				            excelTeam = cell.getStringCellValue();
							column+=1;
							continue;
						}
			            Cell cell = cellIterator.next();
			            
			           /// System.out.println("The cell iterator had at least " + cellCount + " next");
						excelName = cell.getStringCellValue();
			             if (excelName.equals(name)){
			            	 return excelTeam;
			             }
			             else{
			            	// System.out.println("excel name " + excelName + " is not equal to " + name);
			             }
			            column+=1;
			        }
				}
	
			
	try {
		file.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	return name;		
	}


	private void openStudentsDB() {
			try {
				this.file = new FileInputStream(new File(studentsDb));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			try {
				this.workbook = new XSSFWorkbook (file);
			} catch (IOException e) {
				e.printStackTrace();
			}
				
	}


	public String getEmail(Student student) {
		// TODO Auto-generated method stub
		return student.getEmail();
	}

}
