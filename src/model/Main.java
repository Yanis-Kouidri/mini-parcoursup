package model;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		Set<Student> studentsList = null;
		Set<School> schoolsList;
		try {
			schoolsList = FileManager.getSchoolsFromFile();
			studentsList = FileManager.getStudentsFromFile(schoolsList);
		} catch (FileNotFoundException e) {
			System.err.println("File " + e.getMessage() + " not found");
		}
		
		if (studentsList != null) {
			for (Student stud : studentsList) {
				System.out.println(stud.getFirstName() + " " + stud.getLastName());
				ArrayList<School> studPrefSchool = stud.getSchoolPreferences();
				for(School aSchool : studPrefSchool) {
					System.out.print(aSchool.getSchoolName() + ", ");
					System.out.println();
				}
				
				System.out.println();
			}
		}
		
	}
}
