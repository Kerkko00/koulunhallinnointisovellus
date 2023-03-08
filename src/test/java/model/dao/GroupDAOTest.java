package model.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Group;
import model.dao.GroupDAO;
import model.dao.IGroupDAO;

/**
 * @author Kerkko
 *
 *         Class for GroupDAO tests
 */
class GroupDAOTest {

	static IGroupDAO dao = null;
	Group group = null;

	@BeforeAll
	static void init() {
		dao = new GroupDAO();
	}
	@BeforeEach
	public void setUp() {
		group = new Group();
	}

	@AfterEach
	public void tearDown() {
		if (dao.getGroups().length > 0) {
			dao.deleteGroup(group.getID());
		}
	}

	/**
	 * Test for createGroup method
	 */
	@Test
	@DisplayName("Test createGroup method")
	void testCreateGroup() {
		assertTrue(dao.createGroup(group), "createGroup(): Creating new group failed.");
	}

	/**
	 * Test for getGroup method
	 */
	@Test
	@DisplayName("Test getGroup method")
	void testGetGroup() {
		assertTrue(dao.createGroup(group), "getGroup(): Creating new group failed.");
		assertTrue(dao.getGroup(group.getID()) != null, "getGroup(): Finding group from DB failed.");
	}

	/**
	 * Test for getGroups method
	 */
	@Test
	@DisplayName("Test getGroups method")
	void testGetGroups() {
		dao.createGroup(group);
		Group group2 = new Group();
		dao.createGroup(group2);
//		assertTrue(dao.getGroups().length > 0, "getGroups(): Finding groups from DB failed.");
		dao.deleteGroup(group2.getID());
	}

	/**
	 * Test for deleteGroup method
	 */
	@Test
	@DisplayName("Test deleteGroup method")
	void testDeleteGroup() {
		assertTrue(dao.createGroup(group), "createGroup(): Creating new group failed.");
		assertTrue(dao.deleteGroup(group.getID()), "deleteCourse(): Deleting group failed.");
	}

}
