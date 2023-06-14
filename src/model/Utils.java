package model;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public final class Utils {
	
	private final static String STUD_FILE_PATH = "files/students.json";
	private final static String SCHO_FILE_PATH = "files/schools.json";
	
	public static HashSet<Student> getStudentsFromFile(Set<School> listOfAllSchool) throws FileNotFoundException {

		HashSet<Student> studentsList = new HashSet<>();
		JsonArray studentsArray = createJsonArray(STUD_FILE_PATH);

		for (JsonElement element : studentsArray) {
			JsonObject student = element.getAsJsonObject();
			String lastName = student.get("lastName").getAsString();
			String firstName = student.get("firstName").getAsString();
			int studentId = student.get("studentId").getAsInt();
			Student currentStudent = new Student(lastName, firstName, studentId);
			
			JsonArray preferredSchoolsArray = student.get("preferredSchools").getAsJsonArray();
			for (JsonElement aSchool : preferredSchoolsArray) {
				String schoolName = aSchool.getAsString();
				setSchoolPref(currentStudent, schoolName, listOfAllSchool);
			}
			studentsList.add(currentStudent);
		}
		return studentsList;
	}
	
	public static HashSet<School> getSchoolsFromFile() throws FileNotFoundException  {
		HashSet<School> schoolsList = new HashSet<>();

		JsonArray schoolsArray = createJsonArray(SCHO_FILE_PATH);
		for (JsonElement element : schoolsArray) {

			JsonObject school = element.getAsJsonObject();
			String schoolName = school.get("schoolName").getAsString();

			int schoolCapacity = school.get("capacity").getAsInt();
			School currentSchool = new School(schoolName, schoolCapacity);

			JsonArray preferredSchoolsArray = school.get("preferredStudents").getAsJsonArray();
			for (JsonElement aStudent : preferredSchoolsArray) {
				int studentId = aStudent.getAsInt();
				try {
					currentSchool.addStudentIdToPref(studentId);
				} catch (StudentAlreadyPrefException e) {
					System.err.println("There is an error in student id");
				}
			}

			schoolsList.add(currentSchool);
		}
		return schoolsList;	
	}
	
	public static JsonArray createJsonArray(String filePath) throws FileNotFoundException {
		Reader reader = new FileReader(filePath);
		Gson gson = new Gson();
		return gson.fromJson(reader, JsonArray.class);
	}

	/**
	 * A function to add a school to student preference
	 * @param aStudent The student object that you want to add a school
	 * @param schoolName The name of the school provides on the JSON file
	 * @param listOfAllSchools The complete list of schools
	 */
	public static void setSchoolPref(Student aStudent, String schoolName,
									 Set<School> listOfAllSchools) {
		boolean isFind = false;

		for (School aSchool: listOfAllSchools) {
			if (aSchool.getSchoolName().equals(schoolName)) {
				aStudent.addSchoolToPref(aSchool);
				isFind = true;
				break;
			}
		}
		if (!isFind) {
			throw new MissingSchoolException("The school "
					+ schoolName + " that the student nb : "
					+ aStudent.getStudentId() + " is not on the schools list");
		}

	}

	public static void setStudentPrefForEachSchool(Set<School> setOfAllSchools,
												   Set<Student> setOfAllStudents) {
		for (School aSchool : setOfAllSchools) {
			aSchool.studentIdToStudentObject(setOfAllStudents);
		}

	}

	public static void printResults(Map<Student, School> results) {
		System.out.println("Number of student oriented : " + results.size());

		for (Student eachStudent : results.keySet()) {
			System.out.println(eachStudent.getFullName() + " will go at : "
					+ results.get(eachStudent).getSchoolName());
		}
	}


}
