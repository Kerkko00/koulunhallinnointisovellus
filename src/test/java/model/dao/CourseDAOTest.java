package model.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Course;
import model.Group;
import model.dao.CourseDAO;
import model.dao.ICourseDAO;

/**
 * @author Kerkko
 *
 *         Class for CourseDAO tests
 */
public class CourseDAOTest {
	ICourseDAO dao = null;
	Course course = null;
	Group group = null;

	@BeforeEach
	void setUp() {
		dao = new CourseDAO();
		course = new Course("LuovaKurssinNimi", group, LocalDate.now(), LocalDate.now());
	}

	@AfterEach
	public void tearDown() {
		for (Course courseToDelete : dao.getCourses()) {
			dao.deleteCourse(courseToDelete.getCourseID());
		}
	}

	/**
	 * Test for createCourse method
	 */
	@Test
	@DisplayName("Test createCourse method")
	public void testCreateCourse() {
		assertTrue(dao.createCourse(course), "createCourse(): Creating new course failed.");
	}

	/**
	 * Test for getCourse method
	 */
	@Test
	@DisplayName("Test getCourse method")
	public void testGetCourse() {
		assertTrue(dao.createCourse(course), "createCourse(): Creating new course failed.");
		assertTrue(dao.getCourse(course.getCourseID()) != null, "getCourse(): Finding course from DB failed.");
	}

	/**
	 * Test for getCourses method
	 */
	@Test
	@DisplayName("Test getCourses method")
	public void testGetCourses() {
		dao.createCourse(course);
		Course course2 = new Course("ToinenLuovaKurssinNimi", group, LocalDate.now(), LocalDate.now());
		dao.createCourse(course2);
		assertTrue(dao.getCourses().length >= 2, "getCourse(): Finding courses from DB failed.");
		dao.deleteCourse(course2.getCourseID());
	}

	/**
	 * Test for deleteCourse method
	 */
	@Test
	@DisplayName("Test deleteCourse method")
	public void testDeleteCourse() {
		assertTrue(dao.createCourse(course), "createCourse(): Creating new course failed.");
		assertTrue(dao.deleteCourse(course.getCourseID()), "deleteCourse(): Deleting course failed.");
	}
}
