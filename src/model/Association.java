package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The static class make the match between Students and School according to the stable marriage algorithm
 * @author Yanis Kouidri
 * @version 0.1
 */
public final class Association {

    /**
     * Set true if you want to see iteration
     */
    private final static boolean DEBUG = false;

    /**
     * The hearth of the program,
     * the algorithm who matchs students to schools according to their respective choices.
     * //TODO make this method adaptable for student biding and school biding
     * @return The result of the match, for each student the school where he will go
     */
    public static Map<Student, School> matchStudentsSchools(Set<Student> students, Set<School> schools) {
        Set<Student> studentWithoutSchool = new HashSet<>(students); //Copying students'
        // list
        int roundNumber = 0; // initialization of the round counter
        Map<School, Set<Student>> waitingStudents = new HashMap<>(); // For each school, the student on the waiting list

        for (School aSchool: schools) { // filling waiting lists with schools and empty hashSet
            waitingStudents.put(aSchool, new HashSet<>());
        }


        while (!studentWithoutSchool.isEmpty()) {

            if (DEBUG) {
                System.out.println("We are at round number " + roundNumber);

                System.out.println("Student without school : ");
                printAStudentSet(studentWithoutSchool);
                System.out.println();

                System.out.println("Waiting list : ");
                printAMap(waitingStudents);
                System.out.println();
            }


            // Place students on the school waiting list according to the i th choice.
            for (Student aStudent: studentWithoutSchool) {
                School ithChoice = aStudent.getSchoolPreferences().get(roundNumber); // Get the i th choice of the student
                waitingStudents.get(ithChoice).add(aStudent); // Add the student on the waiting list of the school
            }

            // Decide who stay in the waiting list and who get out
            for (School aSchool: schools){
                // Get students in waiting list for "aSchool"
                Set<Student> studentInWaitingList = waitingStudents.get(aSchool);

                // Get school maximal capacity
                int remainingCapacity = aSchool.getSchoolCapacity();
                Set<Student> updatingWaitingList = new HashSet<>();

                for (Student aStudent: aSchool.getStudentPreferences()) {

                    if (remainingCapacity > 0) { // While it remains capacity

                        if (studentInWaitingList.contains(aStudent)) { // if the first desired student is in the waiting list,
                            // accept him,
                            // else go next
                            updatingWaitingList.add(aStudent);
                            remainingCapacity--;
                        }
                    } else {
                        break;
                    }

                }
                // Update the waiting list
                waitingStudents.put(aSchool, updatingWaitingList);
                // Re-calculation of students not on a school waiting list
                studentWithoutSchool = reComputeUnrankedStudents(students, waitingStudents);

            }
            roundNumber++;
        }
        if (DEBUG) {
            System.out.println("Waiting list : ");
            printAMap(waitingStudents);
            System.out.println();
        }

        // Filling the result in a Map, for each student his matching school
        Map<Student, School> matchResult = new HashMap<>();

        for (School keySchool: waitingStudents.keySet()) {

            for(Student aStudent : waitingStudents.get(keySchool)) {
                matchResult.put(aStudent, keySchool);
            }
        }

        return matchResult;
    }

    /**
     * This method will recalculate the set of students without a school
     * (not in a waiting list)
     * @param listOfAllStudent The list of all students
     * @param waitingStudent The waiting list for each school
     * @return The recalculated set of students not in a waiting list
     */
    private static Set<Student> reComputeUnrankedStudents(Set<Student> listOfAllStudent, Map<School, Set<Student>> waitingStudent) {
        Set<Student> studentWithoutSchool = new HashSet<>(listOfAllStudent);
        for (School keySchool: waitingStudent.keySet()) {
            for(Student aStudent : waitingStudent.get(keySchool)) {
                studentWithoutSchool.remove(aStudent);
            }
        }
        return studentWithoutSchool;
    }

    /**
     * A debug method to print a student set
     * @param setToPrint Student set to print
     */
    private static void printAStudentSet(Set<Student> setToPrint) {
        System.out.println();
        for(Student aStudent : setToPrint) {
            System.out.println(aStudent.getFirstName() + " " + aStudent.getLastName());
        }
        System.out.println();
    }

    /**
     * A debug method to print the waiting list for each school
     * @param mapToPrint map of school -> set of student to print
     */
    private static void printAMap(Map<School, Set<Student>> mapToPrint) {
        for (School keySchool: mapToPrint.keySet()) {
            System.out.println(keySchool.getSchoolName());
            for(Student aStudent : mapToPrint.get(keySchool)) {
                System.out.print(aStudent.getFullName() + ", ");
            }
            System.out.println();
        }

    }
}
