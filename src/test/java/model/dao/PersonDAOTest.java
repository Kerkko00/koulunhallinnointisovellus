package model.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Parent;
import model.Person;
import model.PersonType;
import model.Student;
import model.Teacher;
import model.User;
import model.dao.IPersonDAO;
import model.dao.PersonDAO;

/**
 * Class for testing personDAO
 * 
 * @author Ville
 *
 */
class PersonDAOTest {
	static IPersonDAO dao = null;

	@BeforeAll
	static void setUp() {
		dao = new PersonDAO();
	}

	@AfterEach
	public void tearDown() {
		if (dao.getUsers() != null)
			for (User user : dao.getUsers()) {
				dao.deleteUser(user.getUsername());
			}
		if (dao.getPersons() != null)
			for (Person person : dao.getPersons()) {
				dao.deletePerson(person.getID());
			}
		if (dao.getTeachers() != null)
			for (Teacher teacher : dao.getTeachers()) {
				dao.deleteTeacher(teacher.getTeacherID());
			}
		if (dao.getParents() != null)
			for (Parent parent : dao.getParents()) {
				dao.deleteParent(parent.getParentID());
			}
		if (dao.getStudents() != null)
			for (Student student : dao.getStudents()) {
				dao.deleteStudent(student.getStudentID());
			}
	}

	/**
	 * Test method for creating person
	 */
	@Test
	@DisplayName("Person DB changes")
	void testCreatePerson() {
		Person person = new Person("Parent", "Salminen", "04044444111", "Parent@testi.com");
		assertTrue(dao.createPerson(person));
		assertTrue(dao.deletePerson(person.getID()));
	}

	/**
	 * Test method for getting person
	 */
	@Test
	@DisplayName("Getting person from DB")
	void testGetPerson() {
		Person person = new Person("test", "tester", "04044444111", "tests@testi.com");
		assertTrue(dao.createPerson(person));
		assertNotNull(dao.getPerson(person.getID()));
		person.setFirstName("Toimi");
		assertTrue(dao.updatePerson(person));

		assertTrue(dao.getPerson(person.getID()).getFirstName().equals("Toimi"));

	}

	/**
	 * Test method for creating user for person
	 * 
	 */
	@Test
	@DisplayName("Creating user for person")
	void testCreateUserForPerson() {
		Person person = new Person("Parent", "Salminen", "04044444111", "Parent@testi.com");
		User user = new User("parent123", "123");
		assertTrue(dao.createUserForPerson(PersonType.PARENT, person, user));
	}

	/**
	 * Test method for getting user
	 */
	@Test
	@DisplayName("Getting user from DB")
	void testGetUser() {
		User user = new User("parent123", "123");
		dao.createUser(user);

		assertNull(dao.getUser("parent123", "1234"));
		assertNull(dao.getUser("parent12", "123"));
		assertNotNull(dao.getUser("parent123", "123"));
	}

	/**
	 * Test for creating and deleting parent, teacher and person
	 */
	@Test
	@DisplayName("Creating and deleting objects")
	void testCreateAndDelete() {
		User user = new User("parent123", "123");
		Person person = new Person("Parent", "Salminen", "04044444111", "Parent@testi.com");
		Teacher teacher = new Teacher(person, user);
		Parent parent = new Parent();
		assertTrue(dao.createUser(user));

		assertTrue(dao.createTeacher(teacher));
		assertTrue(dao.deleteTeacher(teacher.getTeacherID()));

		assertTrue(dao.createParent(parent));
		assertTrue(dao.deleteParent(parent.getParentID()));
	}

	/**
	 * Test for student CRUD
	 */
	@Test
	@DisplayName("Student CRUD")
	void studentTests() {
		Person person = new Person("Student", "Lapsi");
		Student test = new Student(person);
		assertTrue(dao.createStudent(test));

		assertTrue(dao.getStudent("Student").size() == 1);
		System.out.println("dadwadwawda: " + dao.getStudents().length);
		// assertTrue(dao.getStudents().length > 0);
		assertNotNull(dao.getStudentById(test.getStudentID()));
		assertNull(dao.getStudentById(-4));
		assertTrue(dao.getStudent("Ei ole").size() == 0);
		assertTrue(dao.deleteStudent(test.getStudentID()));
		assertTrue(dao.getStudents().length == 0);
	}
}
