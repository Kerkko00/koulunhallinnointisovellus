/**
 * 
 */
package controller;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.util.Pair;
import model.Absence;
import model.Course;
import model.Group;
import model.Parent;
import model.Person;
import model.PersonType;
import model.Student;
import model.Teacher;
import model.User;
import model.dao.AbsenceDAO;
import model.dao.CourseDAO;
import model.dao.GroupDAO;
import model.dao.IAbsenceDAO;
import model.dao.ICourseDAO;
import model.dao.IGroupDAO;
import model.dao.IPersonDAO;
import model.dao.PersonDAO;
import view.GUI;

/**
 * Test cases for controller
 * 
 * @author Ville
 *
 */
class ControllerTest {

	static IController controller = null;
	static IController controllerGUI = null;
	static IPersonDAO personDAO = null;
	static IGroupDAO daoGroup = null;
	static ICourseDAO daoCourse = null;
	static IAbsenceDAO absenceDAO = null;

	@BeforeAll
	static void init() {
		controllerGUI = new Controller(new GUI());
		controller = new Controller();
		personDAO = new PersonDAO();
		daoGroup = new GroupDAO();
		daoCourse = new CourseDAO();
		absenceDAO = new AbsenceDAO();
	}

	@AfterEach
	public void tearDown() {
		if (personDAO.getUsers() != null)
			for (User user : personDAO.getUsers()) {
				personDAO.deleteUser(user.getUsername());
			}
		if (personDAO.getPersons() != null)
			for (Person person : personDAO.getPersons()) {
				personDAO.deletePerson(person.getID());
			}
		if (personDAO.getTeachers() != null)
			for (Teacher teacher : personDAO.getTeachers()) {
				personDAO.deleteTeacher(teacher.getTeacherID());
			}
		if (personDAO.getParents() != null)
			for (Parent parent : personDAO.getParents()) {
				personDAO.deleteParent(parent.getParentID());
			}
		if (personDAO.getStudents() != null)
			for (Student student : personDAO.getStudents()) {
				personDAO.deleteStudent(student.getStudentID());
			}
		if (daoGroup.getGroups() != null)
			for (Group group : daoGroup.getGroups())
				daoGroup.deleteGroup(group.getID());
		if (daoCourse.getCourses() != null)
			for (Course course : daoCourse.getCourses())
				daoCourse.deleteCourse(course.getCourseID());

	}

	/**
	 * Test method for creating user.
	 */
	@Test
	void testCreateUser() {
		assertTrue(controller.createUser(PersonType.PARENT, "Janne", "Mäkinen", "0401234567", "Janne@test.com",
				"JanneM", "Salasana123"));
	}

	/**
	 * Test method for username already taken.
	 */
	@Test
	void testCreateUserUsernameTaken() {
		controller.createUser(PersonType.PARENT, "Janne", "Mäkinen", "0401234567", "Janne@test.com", "JanneM",
				"Salasana123");
		assertFalse(controller.createUser(PersonType.TEACHER, "Antti", "Pitkänen", "0401234567", "Janne@test.com",
				"JanneM", "123"));
	}

	/**
	 * Test method for user login.
	 */
	@Test
	@DisplayName("Test login/logout")
	void testLogin() {
		assertTrue(controller.createUser(PersonType.PARENT, "Janne", "Mäkinen", "0401234567", "Janne@test.com",
				"JanneM", "Salasana123"));
		assertTrue(controller.createUser(PersonType.TEACHER, "Antti", "Pekka", "04432323232", "antti@test.com"));
		assertFalse(controller.login("JanneM", "Salasana1232"));
		assertTrue(controller.login("JanneM", "Salasana123"));

		assertTrue(controller.logout());
		assertTrue(controller.login("Antti.pekka", "123"));
		assertTrue(controller.logout());
	}

	/**
	 * Test method for getting current loggedin user
	 */
	@Test
	@DisplayName("Test current loggedin user")
	void testGetCurrentUser() {
		assertNull(CurrentUser.getUser());
		controller.createUser(PersonType.PARENT, "Janne", "Mäkinen", "0401234567", "Janne@test.com", "JanneM",
				"Salasana123");
		controller.login("JanneM", "Salasana123");
		assertEquals(CurrentUser.getUser().getParent().getPerson().getEmail(), "Janne@test.com");
		assertEquals(CurrentUser.getPerson().getFirstName(), "Janne");
		assertEquals(CurrentUser.getUserType(), PersonType.PARENT);
		assertNull(CurrentUser.getStudent());
		Person currentPerson = CurrentUser.getUser().getParent().getPerson();
		currentPerson.setFirstName("Antti");
		assertTrue(controller.updatePerson(currentPerson));
		assertTrue(controller.updateUser(CurrentUser.getUser(), "Salasana123"));
		assertTrue(controller.logout());
	}

	/**
	 * Test method for creating students in controller
	 */
	@Test
	@DisplayName("Test creating student via controller ")
	void testStudent() {
		assertTrue(controller.createUser(PersonType.PARENT, "Janne", "Mäkinen", "0401234567", "Janne@test.com",
				"JanneM", "Salasana123"));
		List<Pair<String, String>> temp = new ArrayList<Pair<String, String>>();
		temp.add(new Pair<>("Testi", "Mäkinen"));
		assertFalse(controller.createStudent(temp, "Janne@test.com23"));
		assertTrue(controller.createStudent(temp, "Janne@test.com"));

		assertTrue(controller.getStudent("Testi").size() == 1);
		assertTrue(controller.getStudent("Testi").size() == 1);
	}

	/**
	 * Test method for groups
	 */
	@Test
	@DisplayName("Test group creating/deleting via controller")
	void testGroups() {
		Set<Student> students = new HashSet<Student>();
		assertTrue(controller.createUser(PersonType.TEACHER, "Antti", "Pekka", "04432323232", "antti@test.com"));
		assertTrue(controller.login("Antti.pekka", "123"));
		students.add(new Student());
		assertNull(controller.getGroup("Ei ole"));
		assertTrue(controller.createGroup("Testi", students));
		Group group = controller.getGroup("Testi");
		assertNotNull(group);
		assertTrue(controller.getGroups().length > 0);
		assertTrue(controller.deleteGroup(group));
		assertTrue(controller.getGroups().length == 0);
	}

	@Test
	@DisplayName("Test course and absence creating/deleting via controller")
	void testCourses() {
		Person person = new Person("Student", "Lapsi");
		Student test = new Student(person);
		assertTrue(personDAO.createStudent(test));

		Course course = new Course();
		assertTrue(controller.createCourse(course));
		assertNotNull(controller.getCourses());

		assertTrue(controller.createAbsence("Testi", Date.valueOf("2022-04-09"), course.getCourseID(),
				test.getStudentID()));

		assertTrue(controller.getAbsencesByStudent(test.getStudentID()).size() == 1);
		assertTrue(controller.getAbsencesByCourse(course.getCourseID()).size() == 1);

		for (Absence absence : absenceDAO.getAbsences()) {
			assertTrue(absenceDAO.deleteAbsence(absence.getAbsenceID()));
		}

		assertTrue(controller.deleteCourse(course.getCourseID()));
	}
}
