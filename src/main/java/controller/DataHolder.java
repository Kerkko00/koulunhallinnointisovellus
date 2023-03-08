package controller;

import model.Conversation;
import model.User;
import view.MessagesViewController;

/**
 * A class for transferring conversation data between opened views. 
 * 
 * @author Olli
 */
public final class DataHolder {
	private Conversation conversation;
	private User currentUser = null;
	private static final DataHolder INSTANCE = new DataHolder();
	private MessagesViewController controller;
	private String message;

	/**
	 * Returns the data holder instance.
	 * 
	 * @return The data holder instance
	 */
	public static DataHolder getInstance() {
		return INSTANCE;
	}

	/**
	 * Sets the conversation that is stored in the data holder
	 * 
	 * @param conversation The stored conversation
	 */
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	/**
	 * Returns the conversation that is currently stored in the data holder
	 * 
	 * @return The stored conversation
	 */
	public Conversation getConversation() {
		return conversation;
	}

	/**
	 * Method for setting current user
	 * 
	 * @param user currently logged
	 */
	public void setUser(User user) {
		this.currentUser = user;
	}

	/**
	 * Method for getting user
	 * 
	 * @return currently logged user
	 */
	public User getUser() {
		return currentUser;
	}

	/**
	 * Method for setting controller
	 * 
	 * @param controller
	 */
	public void setController(MessagesViewController controller) {
		this.controller = controller;
	}

	/**
	 * Method for getting message
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Method for setting message
	 * 
	 * @param message String
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
