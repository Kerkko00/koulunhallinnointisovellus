package model;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Absence
 * 
 * @author Ville
 */
class AbsenceTest {
	Absence absence;
	Course course;
	Student student;

	@BeforeEach
	void setUp() {
		course = new Course();
		student = new Student();
		absence = new Absence("Testi syy", Date.valueOf("2022-03-05"), course, student);

	}

	@Test
	@DisplayName("Test date get method")
	void testGetCourse() {
		assertNotNull(absence.getCourse());
	}

	@Test
	@DisplayName("Test course set method")
	void testSetCourse() {
		absence.setCourse(null);
		assertNull(absence.getCourse());
	}

	@Test
	@DisplayName("Test date get method")
	void testGetDate() {
		assertTrue(absence.getDate().compareTo(Date.valueOf("2022-03-05")) == 0);
	}

	@Test
	@DisplayName("Test date set method")
	void testSetDate() {
		absence.setDate(Date.valueOf("2022-05-01"));
		assertTrue(absence.getDate().compareTo(Date.valueOf("2022-05-01")) == 0);
	}

	@Test
	@DisplayName("Test reason methods")
	void testReason() {
		assertTrue(absence.getReason() == "Testi syy");
		absence.setReason("Ei ole");
		assertTrue(absence.getReason() == "Ei ole");
	}

	@Test
	@DisplayName("Test student methods")
	void testStudent() {
		assertTrue(absence.getStudent().getStudentID() == student.getStudentID());
		absence.setStudent(null);
		assertNull(absence.getStudent());
	}

	@Test
	@DisplayName("Test ID get method")
	void testGetAbsenceID() {
		assertTrue(absence.getAbsenceID() == absence.getAbsenceID());
	}

}
