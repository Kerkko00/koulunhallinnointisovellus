package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Conversation
 * 
 * @author Olli
 */
class ConversationTest {

	Conversation conversation;
	Message message;
	Date date;
	Set<Message> messages;
	
	@BeforeEach
	void setUp() {
		
		messages = new HashSet<Message>();
		date = new Date(System.currentTimeMillis());
		conversation = new Conversation("Kalle", "Timo", date, 1, messages);
	}
	
	@Test
	@DisplayName("Test getUsername method")
	void testGetUsername() {
		assertEquals("Kalle", conversation.getUsername(), "Conversation username was not correct");
	}
	
	@Test
	@DisplayName("Test getReceiver method")
	void testGetReceiver() {
		assertEquals("Timo", conversation.getReciver(), "Conversation username did not change correctly");
	}
	
	@Test
	@DisplayName("Test getDate method")
	void testGetDate() {
		assertEquals(date, conversation.getDate(), "Conversation date was not correct");
	}
	
	@Test
	@DisplayName("Test setDate method")
	void testSetDate() {
		Date date = new Date(System.currentTimeMillis() + 10000);
		conversation.setDate(date);
		assertEquals(date, conversation.getDate(), "Conversation date did not change correctly");
	}
	
	@Test
	@DisplayName("Test getState method")
	void testGetState() {
		assertEquals(1, conversation.getState(), "Conversation state was not correct");
	}
	
	@Test
	@DisplayName("Test setState method")
	void testSetState() {
		conversation.setState(3);
		assertEquals(3, conversation.getState(), "Conversation state did not change correctly");
	}
	
	@Test
	@DisplayName("Test getMessages method")
	void testGetMessages() {
		assertEquals(messages, conversation.getMessages(), "Conversation messages were not correct");
	}
	
	@Test
	@DisplayName("Test setMessages method")
	void testSetMessages() {
		Message message = new Message("content", date, true, "Kalle", "Timo");
		messages.add(message);
		
		assertEquals(messages, conversation.getMessages(), "Conversation messages did not change correctly");
	}
}
