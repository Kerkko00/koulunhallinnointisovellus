package controller;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javafx.util.Pair;
import model.Absence;
import model.Course;
import model.Group;
import model.Person;
import model.PersonType;
import model.Student;
import model.User;

/**
 * Controller interface
 * 
 * @author Ville, Kerkko
 */
public interface IController {

	/**
	 * Creates a new person and a user for that person from the given parameters.
	 * Also associates them with a given person type (teacher, parent or student).
	 * 
	 * @param type        Type of a person to be stored
	 * @param firstName   User's first name
	 * @param lastName    User's last name
	 * @param phonenumber User's phone number
	 * @param email       User's email
	 * @param username    User's username for logging in
	 * @param password    User's password
	 * @return True, if the person and user were created succesfully
	 */
	public boolean createUser(PersonType type, String firstName, String lastName, String phonenumber, String email,
			String username, String password);

	/**
	 * Creates a new person and a user for that person from the given parameters.
	 * Also associates them with a given person type (teacher, parent or student).
	 * The username and password are generated automatically.
	 * 
	 * @param type        Type of a person to be stored
	 * @param firstName   User's first name
	 * @param lastName    User's last name
	 * @param phonenumber User's phone number
	 * @param email       User's email
	 * @return True, if the person and user were created succesfully
	 */
	public boolean createUser(PersonType type, String firstName, String lastName, String phonenumber, String email);

	/**
	 * Logs the user in using the given username and password. If the login is
	 * successful, the logged in user's data is stored in {@link CurrentUser}
	 * together with the user's type and associated person data.
	 * 
	 * @param username The user's username
	 * @param password The user's password
	 * @return True, if login succeeded
	 */
	public boolean login(String username, String password);

	/**
	 * Empties the stored user + person data from {@link CurrentUser}.
	 * 
	 * @return True
	 */
	public boolean logout();

	/**
	 * Stores the given course.
	 * 
	 * @param course
	 * @return boolean true if creation succeeds
	 */
	public boolean createCourse(Course course);

	/**
	 * Returns all the stored courses.
	 * 
	 * @return An array of all the courses
	 */
	public Course[] getCourses();

	/**
	 * Deletes a course with the given id.
	 * 
	 * @param id The id of the course to delete
	 * @return True, if the course was deleted successfully
	 */
	public boolean deleteCourse(long id);

	/**
	 * Creates a group with a name for a given set of students.
	 * 
	 * @param name     Name of the group
	 * @param students A set of students that belong to the group
	 * @return True, if the group was created succesfully
	 */
	public boolean createGroup(String name, Set<Student> students);

	/**
	 * Finds a group that has the given name.
	 * 
	 * @param name The group's name
	 * @return The found group, or null if not found
	 */
	public Group getGroup(String name);

	/**
	 * Returns all sotred groups.
	 * 
	 * @return An array of groups
	 */
	public Group[] getGroups();

	/**
	 * Deletes the given group.
	 * 
	 * @param group The group to delete
	 * @return True, if the group was deleted successful
	 */
	public boolean deleteGroup(Group group);

	/**
	 * Creates any number of new students with the given firstname + lastname pairs
	 * and sets the students as dependants for the parent who has the specified
	 * email address.
	 * 
	 * @param students    List of students' firstnames and lastnames
	 * @param parentEmail The email of the student's parent
	 * @return True, if the student was created successful
	 */
	public boolean createStudent(List<Pair<String, String>> students, String parentEmail);

	/**
	 * Gets all stored students with the given name.
	 * 
	 * @param name The name to search with
	 * @return A list of students with the given name
	 */
	public List<Student> getStudent(String name);

	/**
	 * Updates the given user, if the password is correct.
	 * 
	 * @param user        The user that is being updated
	 * @param oldPassword The old password of the user
	 * @return True, if the update was successful
	 */
	public boolean updateUser(User user, String oldPassword);

	/**
	 * Updates the given person.
	 * 
	 * @param person The person that is being updated
	 * @return True, if the update was successful
	 */
	public boolean updatePerson(Person person);

	/**
	 * Creates and stores a new absence, and associates it to the given student and
	 * course.
	 * 
	 * @param reason    Reason of the absence
	 * @param date      Date when the absence took place
	 * @param courseID  Id of the course
	 * @param studentID Id of the student
	 * @return True, if the absence was created succesfully
	 */
	public boolean createAbsence(String reason, Date date, long courseID, long studentID);

	/**
	 * Gets all absences of a given student.
	 * 
	 * @param studentID Id of the student
	 * @return A list of the student's absences
	 */
	public List<Absence> getAbsencesByStudent(long studentID);

	/**
	 * Gets all absences on the given course.
	 * 
	 * @param courseID Id of the course
	 * @return A list of absences on the course
	 */
	public List<Absence> getAbsencesByCourse(long courseID);

	/**
	 * Updates the given absence.
	 * 
	 * @param absence The absence to update
	 * @return True, if the update was successful
	 */
	public boolean updateAbsence(Absence absence);
}
