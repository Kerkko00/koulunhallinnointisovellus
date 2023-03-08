package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Group
 * 
 * @author Kerkko
 */
class GroupTest {
	Group testGroup = null;
	Teacher testTeacher = null;
	Set<Student> testGroupStudents;
	Set<Bulletin> testGroupBulletins;

	@BeforeEach
	public void setUp() {
		testGroup = new Group();
		testTeacher = new Teacher();
		testGroupStudents = new HashSet<Student>();
		testGroupBulletins = new HashSet<Bulletin>();
	}

	@AfterEach
	public void tearDown() {
		testGroup = null;
		testTeacher = null;
		testGroupStudents = null;
		testGroupBulletins = null;
	}
	
	@Test
	@DisplayName("Test getID method")
	public void testGetID() {
		assertTrue(testGroup.getID() != -1, "getID(): Failed to get group ID.");
	}
	
	@Test
	@DisplayName("Test getName method")
	public void testGetName() {
		assertTrue(testGroup.getName() == null, "getName(): Failed to get Group name.");
	}
	
	@Test
	@DisplayName("Test setName method")
	public void testSetName() {
		testGroup.setName("TestiRyhmä");
		assertTrue(testGroup.getName() == "TestiRyhmä", "getName(): Failed to set Group name.");
	}
	
	@Test
	@DisplayName("Test setTeacher and getTeacher methods")
	public void testSetGetTeacher() {
		assertTrue(testGroup.getTeacher() == null, "getTeacher(): Failed to get Group Teacher");
		testGroup.setTeacher(testTeacher);
		assertTrue(testGroup.getTeacher() != null, "setTeacher(): Failed to set Group Teacher");
	}
	
	@Test
	@DisplayName("Test setBulletins and getBulletins methods")
	public void testSetGetBulletins() {
		assertTrue(testGroup.getBulletins().isEmpty(), "getBulletins(): Failed to get Group Bulletins");
		Bulletin testBulletin = new Bulletin();
		testGroupBulletins.add(testBulletin);
		testGroup.setBulletins(testGroupBulletins);
		assertTrue(testGroup.getBulletins() != null, "setBulletins(): Failed to set Group Bulletins");
		testGroupBulletins = null;
	}
}
