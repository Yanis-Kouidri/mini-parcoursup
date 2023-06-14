package model;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

/**
 * @author Yanis Kouidri
 * @version 0.1
 */
public class TestAssociation {
    public static void main(String[] args) {
        Set<Student> studentsList = null;
        Set<School> schoolsList = null;
        try {
            schoolsList = Utils.getSchoolsFromFile();
            studentsList = Utils.getStudentsFromFile(schoolsList);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        if (studentsList != null) {
            Utils.setStudentPrefForEachSchool(schoolsList, studentsList);
            Map<Student, School> result = Association.matchStudentsSchools(studentsList, schoolsList);

            Utils.printResults(result);
        }
    }
}
