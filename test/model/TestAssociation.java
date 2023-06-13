package model;

import java.io.FileNotFoundException;
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
            studentsList = FileManager.getStudentsFromFile();
            schoolsList = FileManager.getSchoolsFromFile();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        for (School aSchool: schoolsList) {
            aSchool.studentIdToStudentObject(studentsList);
            System.out.println(aSchool.getStudentPreferences());

        }
    }
}
