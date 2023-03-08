package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Message
 * 
 * @author Olli
 */
class MessageTest {

	Message message;
	Date date;
	
	@BeforeEach
	void setUp() {
		date = new Date(System.currentTimeMillis());
		message = new Message("Sisältö", date, true, "Tero", "Pekka");
	}
	
	@Test
	@DisplayName("Test getContent method")
	void testGetContent() {
		assertEquals("Sisältö", message.getContent(), "Message content was not correct");
	}
	
	@Test
	@DisplayName("Test setContent method")
	void testSetContent() {
		message.setContent("Uusi sisältö");
		assertEquals("Uusi sisältö", message.getContent(), "Message content did not change correctly");
	}
	
	@Test
	@DisplayName("Test getDate method")
	void testGetDate() {
		assertEquals(date, message.getDate(), "Message date was not correct");
	}
	
	@Test
	@DisplayName("Test getState method")
	void testStateDate() {
		assertEquals(true, message.isNew(), "Message state was not correct");
	}
	
	@Test
	@DisplayName("Test setState method")
	void testSetState() {
		message.setNew(false);
		assertEquals(false, message.isNew(), "Message state did not change correctly");
	}
	
	@Test
	@DisplayName("Test getSender method")
	void testGetSender() {
		assertEquals("Pekka", message.getSender(), "Message sender was not correct");
	}
	
	@Test
	@DisplayName("Test getReceiver method")
	void testGetReceiver() {
		assertEquals("Tero", message.getReciver(), "Message reciever was not correct");
	}
}
