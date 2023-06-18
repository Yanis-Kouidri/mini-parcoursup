package model;


import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

/**
 * @author Yanis Kouidri
 * @author Cedric Abdelbaki
 */
public class Main {

	public static void main(String[] args) {

		if (args.length != 3) {
			throw new IllegalArgumentException("Too many arguments. Please provide the correct number of arguments.");
		}

		// Get the set of all students and the set of all schools
		try {
			// Read the JSON file :
			Set<School> schoolsList = Utils.getSchoolsFromFile(args[1]);
			Set<Student> studentsList = Utils.getStudentsFromFile(schoolsList, args[2]);

			// For each school, convert a student id to a student object
			Utils.setStudentPrefForEachSchool(schoolsList, studentsList);

			Map<Student, School> result = Association.matchWithSchoolBidding(studentsList, schoolsList);

			Utils.printResults(result);
			//TODO print the number of rounds needed

			//TODO Test with TD1 example

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
	}
}
