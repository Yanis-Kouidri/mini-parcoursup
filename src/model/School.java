package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Java class to define schools
 * @author Yanis Kouidri
 * @version 0.1
 */
public class School {

    // Attributes

    /**
     * This counter allows adding a unique id to each school
     * -1 is use to start at 0
     */
    private static final AtomicInteger schoolCounter = new AtomicInteger(0);

    /**
     * The unique id for each school
     */
    private final int schoolId;

    /**
     * The name of the school
     */
    private final String schoolName;
    
    private final int schoolCapacity;

    /**
     * The list of desired students by each school
     */
    private final List<Student> studentPreferences;

    private final List<Integer> studentIdPreferences;

    // Constructor

    /**
     * Create a school by giving its name
     * @param schoolName The name of the school
     */
    public School(String schoolName, int schoolCapacity) {
        if (schoolName == null) {
            throw new IllegalArgumentException("The school name must not be not null");
        }
        if (schoolName.isEmpty()) {
            throw new IllegalArgumentException("The school name can't be empty");
        }
        if (schoolCapacity < 1) {
            throw new IllegalArgumentException("The school capacity can't be null or negative");
        }
        this.schoolId = schoolCounter.getAndIncrement();
        this.schoolName = schoolName;
        this.schoolCapacity = schoolCapacity;
        this.studentPreferences = new ArrayList<>();
        this.studentIdPreferences = new ArrayList<>();
    }

    // Methods

    public int getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public List<Student> getStudentPreferences() {
        return studentPreferences;
    }

    public int getSchoolCapacity() {
        return schoolCapacity;
    }

    /**
     * Add a new student to the list of desired students
     * @param prefStudent The student to add
     * @throws StudentAlreadyPrefException throws when the student that you try to add
     * is already present in the list
     */
    public void addStudentToPref(Student prefStudent) throws StudentAlreadyPrefException {
        if (prefStudent == null) {
            throw new IllegalArgumentException("The student must be not null");
        }
        if (this.studentPreferences.contains(prefStudent)) {
            throw new StudentAlreadyPrefException();
        }

        this.studentPreferences.add(prefStudent);
    }

    public void addStudentIdToPref(Integer idPrefStudent) throws StudentAlreadyPrefException {
        if (idPrefStudent == null) {
            throw new IllegalArgumentException("The student must be not null");
        }
        if (this.studentIdPreferences.contains(idPrefStudent)) {
            throw new StudentAlreadyPrefException();
        }

        this.studentIdPreferences.add(idPrefStudent);
    }

    public void studentIdToStudentObject(Set<Student> allTheStudents) {
        // For the first student id
        for (int aStudentId : studentIdPreferences) {
            // Test all the student to find the matching id
            for (Student aStudent : allTheStudents) {
                // If it is found, add it
                if (aStudent.getStudentId() == aStudentId) {
                    try {
                        addStudentToPref(aStudent);
                    } catch (StudentAlreadyPrefException e) { // That should not happen
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
