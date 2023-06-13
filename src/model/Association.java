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
     * @return The result of the match, for each student the school where he will go
     */
    public static Map<Student, School> matchStudentsSchools(HashSet<Student> students, HashSet<School> schools) {
        Set<Student> studentWithoutSchool = new HashSet<>(students); //Copying the students list
        int roundNumber = 0; // initialisation of the round counter
        Map<School, Set<Student>> waitingStudents = new HashMap<>(); // For each school, the student on the waiting list

        for (School aSchool: schools) { // filling waiting lists with schools and empty hashSet
            waitingStudents.put(aSchool, new HashSet<>());
        }


        while (!studentWithoutSchool.isEmpty()) {
            System.out.println("We are at round number " + roundNumber);
            // Place students on the school waiting list according to the i th choice.
            for (Student aStudent: studentWithoutSchool) {
                School ithChoice = aStudent.getSchoolPreferences().get(roundNumber); // Get the i th choice of the student
                waitingStudents.get(ithChoice).add(aStudent); // Add the student on the waiting list of the school
            }

            // Decide who stay in the waiting list and who get out
            for (School aSchool: schools){
                // Get students in waiting list for "aSchool"
                Set<Student> studentInWaitingList = waitingStudents.get(aSchool);

                // Get number of students in waiting list for "aSchool"
                int nbOfStudentInWaiting = studentInWaitingList.size();

                // Get school maximal capacity
                int currentSchoolCapacity = aSchool.getSchoolCapacity();

                //if (nbOfStudentInWaiting > currentSchoolCapacity) { // Case in which there are too many applicants
                    int remainingCapacity = currentSchoolCapacity;
                    Set<Student> updatingWaitingList = new HashSet<>();

                    for (Student aStudent: aSchool.getStudentPreferences()) {

                        if (remainingCapacity > 0) { // While it remain capacity

                            if (studentInWaitingList.contains(aStudent)) { // if the first desired student is in the waiting list, accept him, else go next
                                updatingWaitingList.add(aStudent);
                                studentWithoutSchool.remove(aStudent);
                                remainingCapacity--;
                            }
                        } else {
                            break;
                        }

                    }
                    // Update the waiting list
                    waitingStudents.put(aSchool, updatingWaitingList);
                //}
            }
            roundNumber++;
        }


        for (School keySchool: waitingStudents.keySet()) {
            System.out.println(keySchool.getSchoolName() + " : ");
            for(Student aStudent : waitingStudents.get(keySchool)) {
                System.out.print(aStudent.getFirstName() + " " + aStudent.getLastName() + ", ");
            }

        }




        Map<Student, School> matchResult = new HashMap<>();
        return matchResult;
    }
}
