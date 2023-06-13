package model;

import java.util.ArrayList;
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
    private final ArrayList<Student> studentPreferences;

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
        this.schoolId = schoolCounter.getAndIncrement();
        this.schoolName = schoolName;
        this.schoolCapacity = schoolCapacity;
        this.studentPreferences = new ArrayList<>();
    }

    // Methods

    public int getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public ArrayList<Student> getStudentPreferences() {
        return studentPreferences;
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
}
