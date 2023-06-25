package model;

import java.util.*;

/**
 * The static class make the match between Students and School according to the stable marriage algorithm
 * @author Yanis Kouidri
 * @author Cédric Abdelbaki
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
     * In another term, schools are bidding.
     * @param students The set of all students
     * @param schools The set of all schools
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

        boolean finished = false;

        while (!finished) {

            if (DEBUG) {
                System.out.println("We are at round number " + roundNumber);

                System.out.println("Student without school : ");
                printAStudentSet(studentWithoutSchool);
                System.out.println();

                System.out.println("Waiting list : ");
                printAMap(waitingStudents);
                System.out.println();
            }


            // Step 1:
            // Place students on the school's waiting list according to the i th choice.
            for (Student aStudent: studentWithoutSchool) {
                School firstChoice = aStudent.getSchoolPreferences().get(0); // Get the i th choice of the student
                waitingStudents.get(firstChoice).add(aStudent); // Add the student on the waiting list of the school
            }

            // Step 2: Re-calculation of students not on a school waiting list

            if (isSchoolAdmissionListsAreGood(schools, waitingStudents)) {
                finished = true;
            } else {

                // Step 3:
                // Decide who can stay in the waiting list
                // and who get out
                for (School aSchool : schools) {
                    // Get students in waiting list for "aSchool"
                    Set<Student> studentInWaitingList = waitingStudents.get(aSchool);

                    // If there are more applicants than the school can accept :
                    if (studentInWaitingList.size() > aSchool.getSchoolCapacity()) {

                        // Get school maximal capacity
                        int remainingCapacity = aSchool.getSchoolCapacity();
                        Set<Student> notRejectedStudents = new HashSet<>();

                        for (Student aStudent : aSchool.getStudentPreferences()) {
                            if (remainingCapacity > 0) { // While it remains capacity

                                if (studentInWaitingList.contains(aStudent)) { // if the first desired student is in the waiting list,
                                    // accept him,
                                    // else go next
                                    notRejectedStudents.add(aStudent);
                                    remainingCapacity--;
                                }
                            } else {
                                break;
                            }
                        }

                        studentInWaitingList.removeAll(notRejectedStudents);

                        for (Student aStudent : studentInWaitingList) {
                            aStudent.getSchoolPreferences().remove(aSchool);
                        }
                    }

                    // Reset the waiting list
                    waitingStudents.get(aSchool).clear();
                }
            }
            roundNumber++;
        }
        if (DEBUG) {
            System.out.println("Waiting list: ");
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

        System.out.println("Number of round to converge: " + roundNumber);
        return matchResult;
    }

    private static boolean isSchoolAdmissionListsAreGood(Set<School> listOfAllSchool, Map<School, Set<Student>> waitingStudents ) {

        boolean processFinished = true;

        for (School aSchool : listOfAllSchool) {
            if (aSchool.getSchoolCapacity() != waitingStudents.get(aSchool).size()) {
                processFinished = false;
                break;
            }

        }
        return processFinished;
    }

    /**
     * The hearth of the program,
     * the algorithm who matches students to schools according to their respective choices.
     * In this method,
     * schools send proposition to their favorite students and each round,
     * students cherry-pick the best one and say no to each other.
     * In another term, students are bidding.
     * @param students The set of all students
     * @param schools The set of all schools
     * @return The result of the match, for each student the school where he will go
     */
    public static Map<Student, School> matchWithStudentBidding(Set<Student> students, Set<School> schools) {
        Set<School> schoolsList = new HashSet<>(schools); // Set of all schools

        Map<Student, Set<School>> studentsBidding = initializeStudentBidding(students); // For each student,
        // Schools on his waiting list
        boolean endOfBidding = false;


        int roundNumber = 0; // initialization of the round counter

        while (!endOfBidding) {

            if (DEBUG) {
                System.out.println("We are at round number " + roundNumber);

                System.out.println("Student without school: ");
                //printAStudentSet(studentWithoutSchool);
                System.out.println();

                System.out.println("Waiting list: ");
                //printAMap(waitingStudents);
                System.out.println();
            }

            // Step 1: Schools send propositions to students according to their capacity

            for (School aSchool : schoolsList) {

                // Get the x preferred students according to the school capacity
                for (int i = 0 ; i < aSchool.getSchoolCapacity() ; i++) {
                    // Get the ith student
                    Student ithChoiceStudent = aSchool.getStudentPreferences().get(i);

                    // Add this school to the student available choice of school
                    studentsBidding.get(ithChoiceStudent).add(aSchool);
                }
            }

            // Step 2: Checks if the bidding process is ended
            boolean ended = true;
            for (Student aStudent : studentsBidding.keySet()) {
                if (studentsBidding.get(aStudent).size() != 1) {
                    ended = false;
                    break;
                }
            }
            if (ended) {
                endOfBidding = true;
            } else {

                // Step 3: Students keep only the best proposition

                // For all students
                for (Student aStudent : studentsBidding.keySet()) {

                    // If the student has at least 2 proposition, he chooses the best one.
                    if (studentsBidding.get(aStudent).size() > 1) {

                        // Get the best proposition according to the student choices
                        School schoolRetain = getBestProposition(aStudent, studentsBidding.get(aStudent));

                        // notify schools that the student refused it
                        for (School aRefusedSchool : studentsBidding.get(aStudent)) {
                            if (aRefusedSchool != schoolRetain) {
                                aRefusedSchool.getStudentPreferences().remove(aStudent);
                            }
                        }

                    }
                    // Reset the set because the school will resent propositions
                    studentsBidding.get(aStudent).clear();
                }
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

        System.out.println("Number of round to converge: " + roundNumber);
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
                break;
            }

        }
        return studentChoice;

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
