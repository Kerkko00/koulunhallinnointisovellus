package model.dao;

import model.Message;

/**
 * DAO interface for message related CRUD database operations.
 * 
 * @author Olli
 */
public interface IMessageDAO {

	/**
	 * Stores the given message into the database.<br>
	 * If a message with the same id exists, updates the existing message.
	 * 
	 * @param message The message to store into the database
	 * @return True, if saving or updating the message succeeded
	 */
	public boolean createMessage(Message message);

	/**
	 * Retrieves all messages from the database.
	 * 
	 * @return An array of messages
	 */
	public Message[] getMessages();
}
