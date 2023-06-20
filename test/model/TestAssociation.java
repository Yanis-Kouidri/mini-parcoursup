package model;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

/**
 * A class to test Association
 * @author Yanis Kouidri
 * @author Cédric Abdelbaki
 * @version 0.1
 */
public class TestAssociation {
    public static void main(String[] args) {
        Set<Student> studentsList = null;
        Set<School> schoolsList = null;
        try {
            schoolsList = Utils.getSchoolsFromFile("schools.json");
            studentsList = Utils.getStudentsFromFile(schoolsList, "students.json");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        if (studentsList != null) {
            Utils.setStudentPrefForEachSchool(schoolsList, studentsList);

            Map<Student, School> result1 = Association.matchWithSchoolBidding(studentsList, schoolsList);
            Map<Student, School> result2 = Association.matchWithStudentBidding(studentsList, schoolsList);


            System.out.println("------------------------------");
            System.out.println("When schools are bidding : ");
            Utils.printResults(result1);
            System.out.println("------------------------------");

            System.out.println("When students are bidding : ");
            Utils.printResults(result2);

        }
    }
}
