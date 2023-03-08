package model.dao;

import java.util.List;

import model.Parent;
import model.Person;
import model.PersonType;
import model.Student;
import model.Teacher;
import model.User;

/**
 * DAO interface for person related CRUD database operations, and their related
 * objects: users, teachers, parents and students.
 * 
 * @author Ville
 */
public interface IPersonDAO {

	/**
	 * Stores the given person into the database.<br>
	 * If a person with the same id exists, updates the existing person.
	 * 
	 * @param person The person to store into the database
	 * @return True, if saving or updating the person succeeded
	 */
	public boolean createPerson(Person person);

	/**
	 * Stores the given user into the database.<br>
	 * If a user with the same id exists, updates the existing user.
	 * 
	 * @param user The user to store into the database
	 * @return True, if saving or updating the user succeeded
	 */
	public boolean createUser(User user);

	/**
	 * Associates the given user with the given person and stores both in the
	 * database.<br>
	 * Also creates a new teacher, parent or student object based on the given
	 * {@link PersonType} and attaches it to the stored person and user. Each person
	 * stored in the database must have one of these person types associated with it
	 * to determine their role in the application.
	 * 
	 * @param type   The person type to associate the created person with
	 * @param person The person for which the user and person type objects are
	 *               associated
	 * @param user   The user which is associated with the specified person
	 * @return True
	 */
	public boolean createUserForPerson(PersonType type, Person person, User user);

	/**
	 * Stores the given teacher into the database.<br>
	 * If a teacher with the same id exists, updates the existing teacher.
	 * 
	 * @param teacher The teacher to store into the database
	 * @return True, if saving or updating the teacher succeeded
	 */
	public boolean createTeacher(Teacher teacher);

	/**
	 * Stores the given parent into the database.<br>
	 * If a parent with the same id exists, updates the existing parent.
	 * 
	 * @param parent The parent to store into the database
	 * @return True, if saving or updating the parent succeeded
	 */
	public boolean createParent(Parent parent);

	/**
	 * Stores the given student into the database.<br>
	 * If a student with the same id exists, updates the existing student.
	 * 
	 * @param student The student to store into the database
	 * @return True, if saving or updating the student succeeded
	 */
	public boolean createStudent(Student student);

	/**
	 * Retrieves a person from the database with the given id.
	 * 
	 * @param id The id of the person to retrieve
	 * @return The found person, or null if not found
	 */
	public Person getPerson(long id);

	/**
	 * Retrieves all persons from the database.
	 * 
	 * @return An array of persons, or null if none are found
	 */
	public Person[] getPersons();

	/**
	 * Retrieves a user from the database with the given username and password.
	 * 
	 * @param username The username of the user
	 * @param password The password of the user
	 * @return The found user, or null if no user is found with the given username +
	 *         password combination
	 */
	public User getUser(String username, String password);

	/**
	 * Retrieves all users from the database.
	 * 
	 * @return An array of users, or null if none are found
	 */
	public User[] getUsers();

	/**
	 * Retrieves all teachers from the database.
	 * 
	 * @return An array of teachers, or null if none are found
	 */
	public Teacher[] getTeachers();

	/**
	 * Retrieves a parent from the database with the given email address.
	 * 
	 * @param email The email address of the parent
	 * @return The found parent, or null if none is found with the given email
	 */
	public Parent getParentByEmail(String email);

	/**
	 * Retrieves all parents from the database.
	 * 
	 * @return An array of parents, or null if none are found
	 */
	public Parent[] getParents();

	/**
	 * Retrieves a student from the database with the given id.
	 * 
	 * @param id The id of the student
	 * @return The found student, or null if none is found
	 */
	public Student getStudentById(long id);

	/**
	 * Retrieves students from the database that have the given name.
	 * 
	 * @param name The name to search with
	 * @return A list of the found students
	 */
	public List<Student> getStudent(String name);

	/**
	 * Retrieves all students from the database.
	 * 
	 * @return An array of found students
	 */
	public Student[] getStudents();

	/**
	 * Updates the given person, if one is found by its id.
	 * 
	 * @param id The person which to update
	 * @return True, if the person was found and updated
	 */
	public boolean updatePerson(Person id);

	/**
	 * Updates the given user, if one is found by its username and password.
	 * 
	 * @param user        The user which to update
	 * @param oldPassword The old password of the user to update
	 * @return True, if the user was found and updated
	 */
	public boolean updateUser(User user, String oldPassword);

	/**
	 * Updates the given parent, if one is found by its id.
	 * 
	 * @param parent The parent which to update
	 * @return True, if the parent was found and updated
	 */
	public Parent updateParent(Parent parent);

	/**
	 * Deletes a person with the specified id from the database, if one is found.
	 * 
	 * @param id The id of the person to delete
	 * @return True, if deletion was successful
	 */
	public boolean deletePerson(long id);

	/**
	 * Deletes a user with the specified username from the database, if one is
	 * found.
	 * 
	 * @param username The username of the user to delete
	 * @return True, if deletion was successful
	 */
	public boolean deleteUser(String username);

	/**
	 * Deletes a teacher with the specified id from the database, if one is found.
	 * 
	 * @param id The id of the teacher to delete
	 * @return True, if deletion was successful
	 */
	public boolean deleteTeacher(long id);

	/**
	 * Deletes a parent with the specified id from the database, if one is found.
	 * 
	 * @param id The id of the parent to delete
	 * @return True, if deletion was successful
	 */
	public boolean deleteParent(long id);

	/**
	 * Deletes a student with the specified id from the database, if one is found.
	 * 
	 * @param id The id of the student to delete
	 * @return True, if deletion was successful
	 */
	public boolean deleteStudent(long id);
}
