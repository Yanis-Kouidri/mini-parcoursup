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
            schoolsList = FileManager.getSchoolsFromFile();
            studentsList = FileManager.getStudentsFromFile(schoolsList);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        if (studentsList != null) {
            for (School aSchool : schoolsList) {
                aSchool.studentIdToStudentObject(studentsList);
            }
            Map<Student, School> result = Association.matchStudentsSchools(studentsList, schoolsList);

            System.out.println("Number of student oriented : " + result.size());

            for (Student eachStudent : result.keySet()) {
                System.out.println(eachStudent.getFullName() + " will go at : " + result.get(eachStudent).getSchoolName());
            }
        }
    }
}
