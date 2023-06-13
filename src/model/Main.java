package model;


import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		ArrayList<Student> list = null;
		try {
			list = FileManager.getStudentsFromFile();
		} catch (FileNotFoundException e) {
			
		}
		
		if (list != null) {
			for (Student stud : list) {
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