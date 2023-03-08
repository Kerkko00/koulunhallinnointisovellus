package model.dao;

import java.util.List;

import model.Absence;

/**
 * DAO interface for absence related CRUD database operations.
 * 
 * @author Ville
 */
public interface IAbsenceDAO {

	/**
	 * Stores the given absence into the database.<br>
	 * If an absence with the same id exists, updates the existing absence.
	 * 
	 * @param absence The absence to store into the database
	 * @return True, if saving or updating the absence succeeded
	 */
	public boolean createAbsence(Absence absence);

	/**
	 * Retrieves all absences stored in the databased.
	 * 
	 * @return A list of the found absences
	 */
	public List<Absence> getAbsences();

	/**
	 * Retrieves all absences of the specified student from the database.
	 * 
	 * @param studentID The student's id whose absences to retrieve
	 * @return A list of absences related to the specified student, or null if none found
	 */
	public List<Absence> getAbsencesByStudent(long studentID);

	/**
	 * Retrieves all absences for the specified course from the database.
	 * 
	 * @param courseID The course's id of which absences to retrieve
	 * @return A list of absences related to the specified course, or null if none found
	 */
	public List<Absence> getAbsencesByCourse(long courseID);

	/**
	 * Deletes an absence with the specified id from the database, if one is found.
	 * 
	 * @param id The id of the absence to delete
	 * @return True, if deletion was successful
	 */
	public boolean deleteAbsence(long id);
}
