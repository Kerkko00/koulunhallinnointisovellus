package model.dao;

import model.Group;

/**
 * DAO interface for group related CRUD database operations.
 * 
 * @author Kerkko, Ville
 */
public interface IGroupDAO {

	/**
	 * Stores the given group into the database.<br>
	 * If a group with the same id exists, updates the existing group.
	 * 
	 * @param group The group to store into the database
	 * @return True, if saving or updating the group succeeded
	 */
	public boolean createGroup(Group group);

	/**
	 * Retrieves a group from the database by the given id.
	 * 
	 * @param id The id of the group to retrieve
	 * @return The found group, or null if not found
	 */
	public Group getGroup(long id);

	/**
	 * Retrieves a group from the database by the given group name.<br>
	 * If multiple groups have the same name, only one is returned.
	 * 
	 * @param name The name of the group to retrieve
	 * @return The found group, or null if not found
	 */
	public Group getGroup(String name);

	/**
	 * Retrieves all the groups stored in the database.
	 * 
	 * @return An array of groups
	 */
	public Group[] getGroups();

	/**
	 * Deletes a group with the specified id from the database, if one is found.
	 * 
	 * @param id The id of the group to delete
	 * @return True, if deletion was successful
	 */
	public boolean deleteGroup(long id);
}
