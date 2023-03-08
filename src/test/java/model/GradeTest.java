package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Grade
 * 
 * @author Olli
 */
class GradeTest {

	Grade grade;
	Student student;
	Date date;
	long millis;
	
	@BeforeEach
	void setUp() {
		date = new Date(System.currentTimeMillis());
		student = new Student();
		grade = new Grade("Matikka", 8, date, student);
		
	}
	
	@Test
	@DisplayName("Test getName method")
	void testGetName() {
		assertEquals("Matikka", grade.getName(), "Grade name was not correct");
	}
	
	@Test
	@DisplayName("Test setName method")
	void testSetName() {
		grade.setName("Liikunta");
		assertEquals("Liikunta", grade.getName(), "Grade name did not change correctly");
	}
	
	@Test
	@DisplayName("Test getDate method")
	void testGetDate() {
		assertEquals(date, grade.getDate(), "Grade date was not correct");
	}
	
	@Test
	@DisplayName("Test setDate method")
	void testSetDate() {
		Date date = new Date(System.currentTimeMillis());
		grade.setDate(date);
		assertEquals(date, grade.getDate(), "Grade date did not change correctly");
	}
	
	@Test
	@DisplayName("Test getValue method")
	void testGetValue() {
		assertEquals(8, grade.getGrade(), "Grade value was not correct");
	}
	
	@Test
	@DisplayName("Test setValue method")
	void testSetValue() {
		grade.setGrade(5);
		assertEquals( 5, grade.getGrade(), "Grade value did not change correctly");
	}
	
	@Test
	@DisplayName("Test getStudent method")
	void testGetStudent() {
		assertEquals(student, grade.getStudent(), "Grade student was not correct");
	}
	
	@Test
	@DisplayName("Test setStudent method")
	void testSetStudent() {
		Student student = new Student();
		grade.setStudent(student);
		assertEquals(student, grade.getStudent(), "Grade value was not correct");
	}
	
}
