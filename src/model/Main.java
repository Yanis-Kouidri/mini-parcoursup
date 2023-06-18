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
			Utils.printProgramUsage();
			throw new IllegalArgumentException("Too many arguments. Please provide the correct number of arguments.");
		}
		boolean schoolsBidding;

		//Pass args
		switch (args[0]) {
			case "schoolsBidding" -> schoolsBidding = true;
			case "studentsBidding" -> schoolsBidding = false;
			default -> {
				Utils.printProgramUsage();
				throw new IllegalArgumentException("You can only choose between schoolsBidding or studentsBidding.");
			}
		}

		String schoolsFileName = args[1];
		String studentsFileName = args[2];

		// Get the set of all students and the set of all schools
		try {
			// Read the JSON file :
			Set<School> schoolsList = Utils.getSchoolsFromFile(schoolsFileName);
			Set<Student> studentsList = Utils.getStudentsFromFile(schoolsList, studentsFileName);

			// For each school, convert a student id to a student object
			Utils.setStudentPrefForEachSchool(schoolsList, studentsList);

			System.out.println(Utils.DELIMITER);
			System.out.println("Info :");
			System.out.println(Utils.DELIMITER);

			Map<Student, School> result;
			if (schoolsBidding) {
				System.out.println("You choose that schools are bidding");
				result = Association.matchWithSchoolBidding(studentsList, schoolsList);
			} else {
				System.out.println("You choose that students are bidding");
				result = Association.matchWithStudentBidding(studentsList, schoolsList);
			}

			Utils.printResults(result);

			//TODO Test with TD1 example

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
	}
}
