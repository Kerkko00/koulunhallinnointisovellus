package model.dao;

import model.Conversation;

/**
 * DAO interface for conversation related CRUD database operations.
 * 
 * @author Olli
 */
public interface IConversationDAO {

	/**
	 * Stores the given conversation into the database.<br>
	 * If a conversation with the same id exists, updates the existing conversation.
	 * 
	 * @param conversation The conversation to store into the database
	 * @return True, if saving or updating the conversation succeeded
	 */
	public boolean createConversation(Conversation conversation);

	/**
	 * Retrieves a conversation from the database by the given id.
	 * 
	 * @param id The id of the conversation to retrieve
	 * @return The found conversation, or null if not found
	 */
	public Conversation getConversation(long id);

	/**
	 * Retrieves a conversation from the database between the given user(name) and
	 * receiver.
	 * 
	 * @param username The username to whom the conversation must belong
	 * @param reciver  The receiver to whom the conversation must belong
	 * @return The found conversation, or null if not found
	 */
	public Conversation getConversation(String username, String reciver);

	/**
	 * Retrieves all conversations from the database.
	 * 
	 * @return An array of found conversations, or null if none are found
	 */
	public Conversation[] getConversations();

	/**
	 * Retrieves conversations from the database that belong to the given username.
	 * 
	 * @param username The username who's conversations to retrieve
	 * @return An array of conversations, or null if none were found
	 */
	public Conversation[] getConversationsByUsername(String username);

	/**
	 * Updates the given conversation found in the database.
	 * 
	 * @param conversation The conversation to update
	 * @return True, if the conversation was found and updated
	 */
	public boolean updateConversation(Conversation conversation);

	/**
	 * Deletes a conversation with the specified id from the database, if one is
	 * found.
	 * 
	 * @param id The id of the conversation to delete
	 * @return True, if the deletion was successful
	 */
	boolean deleteConversation(long id);
}
