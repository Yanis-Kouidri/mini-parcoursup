package model; 
import java.util.concurrent.atomic.*;
import java.util.ArrayList;

/** Allows creating a student object
 * @author Cédric ABDELBAKI
 */

public class Student {

	// A counter that increments
	private static final AtomicInteger studentsCounter = new AtomicInteger(0);

	// The student identifier
	private final int studentId;

	// The student lastname
	private final String lastName;

	// The student firstname
	private final String firstName;

	// The list of the students favorites schools, sorted by preferences
	private final ArrayList<School> schoolPreferences;

	/** Instantiates a student object
	 * @param lastName The student lastname
	 * @param firstName The student firstname
	 */
	public Student(String lastName, String firstName) {	
		this.studentId = studentsCounter.incrementAndGet();
		this.lastName = lastName;
		this.firstName = firstName;
		this.schoolPreferences = new ArrayList<>();
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

	/** Gets the student list of favorite schools
	 * @return schoolPreferences The student list of favorite schools
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
	}
}
