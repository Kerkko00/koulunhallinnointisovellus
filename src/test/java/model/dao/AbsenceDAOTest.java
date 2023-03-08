package model.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import controller.Controller;
import controller.IController;
import javafx.util.Pair;
import model.Absence;
import model.Course;
import model.Person;
import model.PersonType;
import model.Student;
import model.dao.AbsenceDAO;
import model.dao.CourseDAO;
import model.dao.IAbsenceDAO;
import model.dao.ICourseDAO;
import model.dao.IPersonDAO;
import model.dao.PersonDAO;

class AbsenceDAOTest {
	static IAbsenceDAO absenceDAO;
	static IPersonDAO personDAO;
	static IController controller;
	static ICourseDAO courseDAO;
	static Course course;
	static Student main;

	@BeforeAll
	static void setUp() {
		absenceDAO = new AbsenceDAO();
		controller = new Controller();
		personDAO = new PersonDAO();
		courseDAO = new CourseDAO();

		controller.createUser(PersonType.PARENT, "Parent", "Testi", "1234567", "Test@gmail.com");

		controller.createUser(PersonType.STUDENT, "Testi", "Ukko", null, null);
		course = new Course("TestiKurssi", null, LocalDate.now(), LocalDate.now());

		Person person = new Person("Student", "Lapsi");
		main = new Student(person);
		personDAO.createStudent(main);
		courseDAO.createCourse(course);
	}

	@AfterEach
	void cleanUp() {
		for (Absence absence : absenceDAO.getAbsences()) {
			absenceDAO.deleteAbsence(absence.getAbsenceID());
		}

	}

	@Test
	@DisplayName("Test creating new absence")
	void testCreateAbsence() {
		Absence testAbsence = new Absence(null, Date.valueOf("2022-03-05"), null, null);
		assertTrue(absenceDAO.createAbsence(testAbsence));
		assertTrue(absenceDAO.getAbsences().size() > 0);
	}

	@Test
	@DisplayName("Test getting absence by course ")
	void testGetAbsencesByCourse() {
		Absence testAbsence = new Absence(null, Date.valueOf("2022-03-05"), course, null);
		assertTrue(absenceDAO.createAbsence(testAbsence));
		assertNotNull(absenceDAO.getAbsencesByCourse(course.getCourseID()));
	}
}
