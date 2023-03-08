package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Course
 * 
 * @author Kerkko
 */
class CourseTest {
	Course course = null;
	Group testGroup = null;

	@BeforeEach
	public void setUp() {
		course = new Course();
		testGroup = new Group();
	}

	@AfterEach
	public void tearDown() {
		course = null;
		testGroup = null;
	}

	@Test
	@DisplayName("Test getCourseID method")
	public void testGetCourseID() {
		assertTrue(course.getCourseID() != -1, "getCourseID(): Failed to get CourseID.");
	}

	@Test
	@DisplayName("Test getCourseName method")
	public void testGetCourseName() {
		assertTrue(course.getCourseName() == null, "getCourseName(): Failed to get CourseName.");
	}

	@Test
	@DisplayName("Test setCourseName method")
	public void testSetCourseName() {
		course.setCourseName("KurssinNimi");
		assertTrue(course.getCourseName() == "KurssinNimi", "getCourseName(): Failed to set CourseName.");
	}

	@Test
	@DisplayName("Test getGroup method")
	public void testGetGroup() {
		course.setGroup(testGroup);
		assertTrue(course.getGroup() != null, "getGroup(): Failed to get group.");
	}

	@Test
	@DisplayName("Test setGroup method")
	public void testSetGroup() {
		testGroup.setName("TestiRyhmä");
		course.setGroup(testGroup);
		assertTrue(course.getGroup().getName() == "TestiRyhmä", "setGroup(): Failed to set Group.");
	}

	@Test
	@DisplayName("Test getStartDate method")
	public void testGetStartDate() {
		assertTrue(course.getStartDate() == null, "getStartDate(): Failed to get StartDate.");
	}

	@Test
	@DisplayName("Test getEndDate method")
	public void testGetEndDate() {
		assertTrue(course.getEndDate() == null, "getEndDate(): Failed to get EndDate.");
	}

}
