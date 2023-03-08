package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Bulletin
 * 
 * @author Saku
 */
class BulletinTest {
	Bulletin bulletin;
	Teacher teacher;
	Set<Group> groups;

	@BeforeEach
	void setUp() {
		teacher = new Teacher();

		groups = new HashSet<Group>();
		groups.add(new Group());
		groups.add(new Group());
		groups.add(new Group());

		bulletin = new Bulletin("Testitiedote", "Tiedotteen teksti", teacher, groups);
	}

	@Test
	@DisplayName("Test getTitle method")
	void testGetTitle() {
		assertEquals("Testitiedote", bulletin.getTitle(), "Bulletin title was not correct");
	}

	@Test
	@DisplayName("Test setTitle method")
	void testSetTitle() {
		bulletin.setTitle("Erilainen otsikko");
		assertEquals("Erilainen otsikko", bulletin.getTitle(), "Bulletin title did not change correctly");
	}

	@Test
	@DisplayName("Test getContent method")
	void testGetContent() {
		assertEquals("Tiedotteen teksti", bulletin.getContent(), "Bulletin content was not correct");
	}

	@Test
	@DisplayName("Test setContent method")
	void testSetContent() {
		bulletin.setTitle("Erilainen teksti");
		assertEquals("Erilainen teksti", bulletin.getTitle(), "Bulletin content did not change correctly");
	}

	@Test
	@DisplayName("Test getSender method")
	void testGetSender() {
		assertEquals(teacher, bulletin.getSender(), "Bulletin sender was not correct");
	}

	@Test
	@DisplayName("Test setSender method")
	void testSetSender() {
		Teacher newTeacher = new Teacher();
		bulletin.setSender(newTeacher);

		assertEquals(newTeacher, bulletin.getSender(), "The sender was not changed correctly");
		assertFalse(teacher.getBulletins().contains(bulletin),
				"The old sender still has the bulletin in their bulletins list");
		assertTrue(newTeacher.getBulletins().contains(bulletin),
				"The new sender doesn't have the bulletin in their bulletins list");
	}

	@Test
	@DisplayName("Test getGroups method")
	void testGetGroups() {
		assertEquals(groups, bulletin.getGroups(), "Bulletin groups were not correct");
	}

	@Test
	@DisplayName("Test setGroups method")
	void testSetGroups() {
		Set<Group> newGroups = new HashSet<Group>();
		newGroups.add(new Group());
		bulletin.setGroups(newGroups);

		assertEquals(newGroups, bulletin.getGroups(), "The groups could not be changed");
		assertEquals(1, bulletin.getGroups().size(), "The new groups size was incorrect");
	}

	@Test
	@DisplayName("Test addGroup method")
	void testAddGroup() {
		bulletin.addGroup(new Group());
		assertEquals(4, bulletin.getGroups().size(), "The new group size is incorrect");

		bulletin.setGroups(null);
		bulletin.addGroup(new Group());
		assertEquals(1, bulletin.getGroups().size(), "A group could not be added to empty groups list");

		for (Group group : bulletin.getGroups()) {
			assertTrue(group.getBulletins().contains(bulletin),
					"A group does not have the bulletin in its groups list");
		}
	}

	@Test
	@DisplayName("test sentdate method")
	void testSendDate() {
		Date date = new Date();
		bulletin.setSentDate(date);
		assertTrue(bulletin.getSentDateString().compareTo(date.toString()) == 0);
		assertTrue(bulletin.getSentDate().compareTo(date) == 0);
	}
}
