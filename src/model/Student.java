package model; 
import java.util.concurrent.atomic.*;
import java.util.ArrayList;
import java.util.Random;

/** Allows to create a student object
 * @author Cédric ABDELBAKI
 */

public class Student {

	// A counter that increments
	private static final AtomicInteger studentsCounter = new AtomicInteger(0);

	// The student identifier
	private final int studentId;

	// The student last name
	private final String lastName;

	// The student first name
	private final String firstName;

	// The student average grade
	private final float grade;

	// The list of the students favorites schools, sorted by preferences
	private final ArrayList<School> schoolPreferences;

	/** Instantiates a student object
	 * @param lastName The student last name
	 * @param firstName The student first name
	 */
	public Student(String lastName, String firstName) {	
		this.studentId = studentsCounter.incrementAndGet();
		this.lastName = lastName;
		this.firstName = firstName;
		this.grade = new Random().nextFloat(0, 20);
		this.schoolPreferences = new ArrayList<School>();
	}

	/** Gets the student identifier
	 * @return studentId The student identifier
	 */
	public int getStudentId() {
		return studentId;
	}

	/** Gets the student last name
	 * @return lastName the student last name
	 */
	public String getLastName() {
		return lastName;
	}

	/** Gets the student first name
	 * @return firstName the student firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/** Gets the student average grade
	 * @return grade The student average grade
	 */
	public float getGrade() {
		return grade;
	}

	/** Gets the student list of favorites schools
	 * @return schoolPreferences The student list of favorites schools
	 */
	public ArrayList<School> getSchoolPreferences() {
		return schoolPreferences;
	}

	/** Adds a school to the student list of favorite schools
	 * @param schoolToAdd The school to add to the list
	 */
	public void addSchoolToPref(School schoolToAdd) {
		if (schoolToAdd == null) {
			throw new IllegalArgumentException();
		}
		schoolPreferences.add(schoolToAdd);
		//TODO check is schoolToAdd is not already in schoolPreferences
	}
}
