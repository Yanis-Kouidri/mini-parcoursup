package model;

import java.util.*;

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
     * the algorithm who matches students to schools according to their respective choices.
     * In this method,
     * students go in the garden of their favorite school and school decide who can stay and who take a hike.
     * In other term, schools are bidding.
     * //TODO make this method adaptable for student biding and school biding
     * @return The result of the match, for each student the school where he will go
     */
    public static Map<Student, School> matchWithSchoolBidding(Set<Student> students, Set<School> schools) {
        Set<Student> studentWithoutSchool = new HashSet<>(students); //Copying students
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

    public static Map<Student, School> matchWithStudentBidding(Set<Student> students, Set<School> schools) {
        Set<School> schoolsList = new HashSet<>(schools); // school without a complete list of students
        Map<School, Integer> schoolsRemainingCapacity = initializeSchoolCapacity(schools);

        Map<Student, Set<School>> studentsBidding = initializeStudentBidding(students); // For each student,
        // schools on his waiting list
        boolean endOfBidding = false;


        int roundNumber = 0; // initialization of the round counter

        while (!endOfBidding) {

            if (DEBUG) {
                System.out.println("We are at round number " + roundNumber);

                System.out.println("Student without school : ");
                //printAStudentSet(studentWithoutSchool);
                System.out.println();

                System.out.println("Waiting list : ");
                //printAMap(waitingStudents);
                System.out.println();
            }

            // Step 1: Schools send propositions to the student if it remains places

            for (School aSchool : schoolsList) {
                if (schoolsRemainingCapacity.get(aSchool) > 0) {
                    //Get the first choice (in terms of student) of the school
                    Student firstChoiceStudent = aSchool.getStudentPreferences().get(0);

                    //Add the current school to the waiting list of the student
                    studentsBidding.get(firstChoiceStudent).add(aSchool);

                    //Remove this student of the school choices
                    aSchool.getStudentPreferences().remove(0);
                }
            }

            // Step 2: Students keep only the best proposition

            // For all students
            for (Student aStudent : studentsBidding.keySet()) {

                // If the student has at least 2 proposition, he chooses the best one.
                if (studentsBidding.get(aStudent).size() > 1) {

                    // Get the best proposition according to the student choices
                    School schoolRetain = getBestProposition(aStudent, studentsBidding.get(aStudent));
                    Set<School> updatedChoice = new HashSet<>();
                    updatedChoice.add(schoolRetain);
                    studentsBidding.put(aStudent, updatedChoice);
                }
            }

            // Step 3: Recompute the school remaining capacity.
            schoolsRemainingCapacity = computeSchoolCapacity(schools, studentsBidding);

            // Step 4: Checks if the bidding process is ended
            boolean ended = true;
            for (Student aStudent : studentsBidding.keySet()) {
                if (studentsBidding.get(aStudent).size() != 1) {
                    ended = false;
                    break;
                }
            }
            if (ended) {
                endOfBidding = true;
            }

            roundNumber++;
        }

        // Return result

        Map<Student, School> results = new HashMap<>();

        for (Student aStudent : studentsBidding.keySet()) {
            for (School aSchool : schoolsList) {
                if (studentsBidding.get(aStudent).contains(aSchool)) {
                    results.put(aStudent, aSchool);
                    break;
                }
            }
        }

        return results;
        
    }

    /**
     * For a student who has a list of propositions made by school.
     * Return the best one according to their criteria
     */
    private static School getBestProposition(Student aStudent, Set<School> proposition) {
        List<School> studentPreferences = aStudent.getSchoolPreferences();

        School studentChoice = null; //The school that the student decides to retain

        for (School aPreference : studentPreferences) {
            if (proposition.contains(aPreference)) {
                studentChoice = aPreference;
            }
        }
        return studentChoice;

    }
    
    private static Map<School, Integer> initializeSchoolCapacity(Set<School> allSchools) {
        Map<School, Integer> mapOfSchoolsCapacity = new HashMap<>();
        for(School aSchool : allSchools) {
            mapOfSchoolsCapacity.put(aSchool, aSchool.getSchoolCapacity());
        }
        return mapOfSchoolsCapacity;
    }

    private static Map<School, Integer> computeSchoolCapacity(Set<School> allSchools, Map<Student, Set<School>> studentsBidding) {
        Map<School, Integer> mapOfSchoolsCapacity = initializeSchoolCapacity(allSchools);

        // For each school
        for (School aSchool : allSchools) {

            // browse each student
            for (Student aStudent : studentsBidding.keySet()) {

                // If the student has this school on him choice, removing 1 at school remaining capacity
                if (studentsBidding.get(aStudent).contains(aSchool)) {
                    int previousValue = mapOfSchoolsCapacity.get(aSchool);
                    mapOfSchoolsCapacity.put(aSchool, previousValue - 1);
                }
            }
        }
        return mapOfSchoolsCapacity;
    }

    private static Map<Student, Set<School>> initializeStudentBidding(Set<Student> allStudents) {
        Map<Student, Set<School>> mapOfStudentReceivedProposition = new HashMap<>();
        for(Student aStudent : allStudents) {
            mapOfStudentReceivedProposition.put(aStudent, new HashSet<>());
        }
        return mapOfStudentReceivedProposition;
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
