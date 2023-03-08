package model.dao;

import model.Course;

/**
 * DAO interface for course related CRUD database operations.
 * 
 * @author Kerkko
 */
public interface ICourseDAO {

	/**
	 * Stores the given course into the database.<br>
	 * If a course with the same id exists, updates the existing course.
	 * 
	 * @param course The course to store into the database
	 * @return True, if saving or updating the course succeeded
	 */
	public boolean createCourse(Course course);

	/**
	 * Retrieves a course from the database by the given id.
	 * 
	 * @param id The id of the course to retrieve
	 * @return The found course, or null if not found
	 */
	public Course getCourse(long id);

	/**
	 * Retrieves all courses from the database.
	 * 
	 * @return An array of found courses
	 */
	public Course[] getCourses();

	/**
	 * Deletes a course with the specified id from the database, if one is found.
	 * 
	 * @param id The id of the course to delete
	 * @return True, if deletion was successful
	 */
	public boolean deleteCourse(long id);
}
