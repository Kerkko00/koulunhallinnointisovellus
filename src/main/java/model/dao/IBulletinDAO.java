package model.dao;

import java.util.List;

import model.Bulletin;
import model.Group;
import model.User;

/**
 * DAO interface for bulletin related CRUD database operations.
 * 
 * @author Saku
 */
public interface IBulletinDAO {

	/**
	 * Stores the given bulletin into the database.<br>
	 * If a bulletin with the same id exists, updates the existing bulletin.
	 * 
	 * @param bulletin The bulletin to store into the database
	 * @return True, if saving or updating the bulletin succeeded
	 */
	public boolean createBulletin(Bulletin bulletin);

	/**
	 * Retrieves a bulletin from the database with the given id.
	 * 
	 * @param id The id of the bulletin to retrieve
	 * @return The found bulletin, or null if not found
	 */
	public Bulletin getBulletin(long id);

	/**
	 * Returns a list of bulletins from the database related to the specified
	 * user.<br>
	 * If the user is a teacher, returns all bulletins sent by that teacher. If the
	 * user is a parent, returns all bulletins received by the {@link Group} in
	 * which the currently selected student belongs in.
	 * 
	 * @param user The user who's bulletins to retrieve
	 * @return A list of bulletins related to the specified user
	 */
	public List<Bulletin> getBulletins(User user);

	/**
	 * Deletes a bulletin with the specified id from the database, if one is found.
	 * 
	 * @param id The id of the bulletin to delete
	 * @return True, if deletion was successful
	 */
	public boolean deleteBulletin(long id);
}
