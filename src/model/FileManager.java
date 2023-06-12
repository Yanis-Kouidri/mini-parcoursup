package model;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;


public class FileManager {
	
	private ArrayList<Student> studentsList;
	private ArrayList<School> schoolsList;

	public FileManager() {
		String studFilePath = "files/students.csv";
		String schoolFilePath = "files/schools.csv";
		BufferedReader bufferedReader = createReader(studFilePath); 
		
		this.studentsList = readStudentsFile(bufferedReader);
		bufferedReader = createReader(schoolFilePath); 
		
		this.schoolsList = readSchoolsFile(bufferedReader);
	}
	
	public BufferedReader createReader(String filePath) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			return bufferedReader;
		} catch (IOException e) {
			System.err.println("Nique !");
			
		}
		return null;
	}
	
	public ArrayList<Student> readStudentsFile(BufferedReader bufferedReader) {
		String line;
		ArrayList<Student> list = new ArrayList<>();
		try {
			bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(";");
				
				//System.out.println(values[1]);
				
				String lastName = values[0].trim();
				String firstName = values[1].trim();

				Student currentStudent = new Student(lastName, firstName);

				String schoolString = values[2].trim();
				schoolString = schoolString.substring(1, schoolString.length() -1); // Removing  the barket []
				String[] schoolsList = schoolString.split(",");

				for (String school: schoolsList) {
					currentStudent.getSchoolPreferences().add(new School(school));

				}


				list.add(currentStudent);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
	}
	
	public ArrayList<School> readSchoolsFile(BufferedReader bufferedReader) {
		String line;
		ArrayList<School> list = new ArrayList<>();
		try {
			bufferedReader.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(";");
				String schoolName = values[0].trim();
				School currentSchool = new School(schoolName);
				list.add(currentSchool);
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
	}
	
	public void printStudent() {
		for (int i = 0; i < studentsList.size(); i++) {
			String firstName = studentsList.get(i).getFirstName();
			String lastName = studentsList.get(i).getLastName();
			String school1 = studentsList.get(i).getSchoolPreferences().get(1).getSchoolName();
			System.out.println(firstName + " " + lastName + " " + school1);

		}
	}
	
	public void printSchool() {
		for (int i = 0; i < schoolsList.size(); i++) {
			String schoolName = schoolsList.get(i).getSchoolName();
			int idSchool = schoolsList.get(i).getSchoolId();
			System.out.println(schoolName + " " + idSchool);

		}
	}
	
	
}
