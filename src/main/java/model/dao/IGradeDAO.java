package model.dao;

import model.Grade;
import model.Student;

/**
 * DAO interface for absence related CRUD database operations.
 * 
 * @author Olli
 */
public interface IGradeDAO {

	/**
	 * Stores the given grade into the database.<br>
	 * If a grade with the same id exists, updates the existing grade.
	 * 
	 * @param grade The grade to store into the database
	 * @return True, if saving or updating the grade succeeded
	 */
	public boolean createGrade(Grade grade);

	/**
	 * Retrieves a grade from the database by the given id.
	 * 
	 * @param id The id of the grade to retrieve
	 * @return The found grade, or null if not found
	 */
	public Grade getGrade(long id);

	/**
	 * Retrieves the grades of the given student from the database.
	 * 
	 * @param student The student who's grades to retrieve
	 * @return An array of grades belonging to the specified student
	 */
	public Grade[] getGrades(Student student);

	/**
	 * Updates the given grade, if one is found by its id.
	 * 
	 * @param grade The grade which to update
	 * @return True, if the grade was found and updated
	 */
	public boolean updateGrade(Grade grade);

	/**
	 * Deletes the given grade from the database, if it is found.
	 * 
	 * @param grade The grade to delete
	 * @return True, if deletion was successful
	 */
	public boolean deleteGrade(Grade grade);
}
