package model;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public final class FileManager {
	
	private final static String STUD_FILE_PATH = "files/students.json";
	private final static String SCHO_FILE_PATH = "file/schools.json";
	
	public static ArrayList<Student> getStudentsFromFile() throws FileNotFoundException {
		ArrayList<Student> studentsList = new ArrayList<>();
		JsonArray studentsArray = createJsonArray(STUD_FILE_PATH);
		for (JsonElement element : studentsArray) {
			JsonObject student = element.getAsJsonObject();
			String lastName = student.get("lastName").getAsString();
			String firstName = student.get("firstName").getAsString();
			Student currentStudent = new Student(lastName, firstName);
			
			JsonArray preferedSchoolsArray = student.get("preferedSchools").getAsJsonArray();
			for (JsonElement aSchool : preferedSchoolsArray) {
				String schoolName = aSchool.toString();
				currentStudent.addSchoolToPref(new School(schoolName, 0));
				//FIXME : Create 2 method, one for creating a list of student without school pref
				// One to create a list of school
				// Then add school to schoolPreference without duplication
			}
			studentsList.add(currentStudent);
		}
		return studentsList;
	}
	
	public static ArrayList<School> getSchoolsFromFile() throws FileNotFoundException  {
		ArrayList<School> schoolsList = new ArrayList<>();
		JsonArray schoolsArray = createJsonArray(SCHO_FILE_PATH);
		for (JsonElement element : schoolsArray) {
			JsonObject school = element.getAsJsonObject();
			String schoolName = school.get("schoolName").getAsString();
			int schoolCapacity = school.get("capacity").getAsInt();
			School currentSchool = new School(schoolName, schoolCapacity);
			schoolsList.add(currentSchool);
		}
		return schoolsList;	
	}
	
	public static JsonArray createJsonArray(String filePath) throws FileNotFoundException {
		Reader reader = new FileReader(filePath);
		Gson gson = new Gson();
		JsonArray array = gson.fromJson(reader, JsonArray.class);
		return array;
	}
}
