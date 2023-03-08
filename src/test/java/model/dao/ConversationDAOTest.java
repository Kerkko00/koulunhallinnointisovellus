package model.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Conversation;
import model.Message;
import model.dao.ConversationDAO;
import model.dao.IConversationDAO;

/**
 * @author Olli
 *
 *         Class for ConversationDAO tests
 */
public class ConversationDAOTest {
	static IConversationDAO dao = null;
	Conversation conversation = null;
	@BeforeAll
	static void init() {
		dao = new ConversationDAO();
	}
	@BeforeEach
	void setUp() {
		Date date = new Date();
		Set<Message> messages = new HashSet<Message>();
		conversation = new Conversation("Aku", "Roope", date, 1, messages);
	}

	@AfterEach
	public void tearDown() {
		if (dao.getConversation(conversation.getID()) != null) {
			dao.deleteConversation(conversation.getID());
		}
	}

	/**
	 * Test for createConversation method
	 */
	@Test
	@DisplayName("Test createConversation method")
	public void testCreateConversation() {
		assertTrue(dao.createConversation(conversation), "createConversation(): Creating new conversation failed.");
	}

	/**
	 * Test for getConversation method
	 */
	@Test
	@DisplayName("Test getConversation method")
	public void testGetConversation() {
		assertTrue(dao.createConversation(conversation), "createConversation(): Creating new conversation failed.");
		assertTrue(dao.getConversation(conversation.getID()) != null, "getConversation(): Finding conversation from DB failed.");
	}

	/**
	 * Test for getConversations method
	 */
	@Test
	@DisplayName("Test getConversations method")
	public void testGetConversations() {
		dao.createConversation(conversation);
		Date date = new Date();
		Set<Message> messages = new HashSet<Message>();
		Conversation conversation2 = new Conversation("Tupu", "Hupu", date, 3, messages);
		dao.createConversation(conversation2);
		assertTrue(dao.getConversations().length == 2, "getConversation(): Finding conversations from DB failed.");
		dao.deleteConversation(conversation.getID());
		dao.deleteConversation(conversation2.getID());
	}

	/**
	 * Test for deleteConversations method
	 */
	@Test
	@DisplayName("Test deleteConversation method")
	public void testDeleteConversation() {
		assertTrue(dao.createConversation(conversation), "createConversation(): Creating new conversation failed.");
		assertTrue(dao.deleteConversation(conversation.getID()), "deleteConversation(): Deleting conversation failed.");
	}
}
