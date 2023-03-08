package controller;

import model.Person;
import model.PersonType;
import model.Student;
import model.User;

/**
 * A static class (singleton?) for storing the logged in user's information.
 * Contains the person, person type and selected student that are associated
 * with the user.
 * 
 * @author Ville, Saku
 */
public final class CurrentUser {
	private static User user = null;
	private static Person person = null;
	private static PersonType userType = PersonType.LOGGEDOUT;

	private static Student student = null;
	private static StudentChangedListener studentChangedListener = null;

	private CurrentUser() {
	}

	/**
	 * Returns the logged in user, or null if no user is logged in.
	 * 
	 * @return The logged in user
	 */
	public static User getUser() {
		return user;
	}

	/**
	 * Returns the person associated with the logged in user.
	 * 
	 * @return The user's person data
	 */
	public static Person getPerson() {
		return person;
	}

	/**
	 * Sets the currently logged in person.
	 * 
	 * @param person The person to set as logged in
	 */
	public static void setPerson(Person person) {
		CurrentUser.person = person;
	}

	/**
	 * Sets the currently logged in user.
	 * 
	 * @param user The user to set as logged in
	 */
	public static void setUser(User user) {
		CurrentUser.user = user;
	}

	/**
	 * Returns the currently logged in user's person type: teacher or parent
	 * (students can't log in).
	 * 
	 * @return The user's person type
	 */
	public static PersonType getUserType() {
		return userType;
	}

	/**
	 * Sets the currently logged in user's person type.
	 * 
	 * @param userType The person type to set as logged in
	 */
	public static void setUserType(PersonType userType) {
		CurrentUser.userType = userType;
	}

	/**
	 * Sets the student whose data is currently being viewed.
	 * 
	 * @param student The student to select
	 */
	public static void setStudent(Student student) {
		if (student != CurrentUser.student) {
			CurrentUser.student = student;

			// For updating loaded views when the selected student is changed
			if (studentChangedListener != null) {
				studentChangedListener.studentChanged(CurrentUser.student);
			}
		}
	}

	/**
	 * Gets the student whose data is currently being viewed.
	 * 
	 * @return The selected student
	 */
	public static Student getStudent() {
		return student;
	}

	/**
	 * Sets the currently used {@link StudentChangedListener}. The listener is set from the
	 * view that was last opened, if that view needs to update its components based
	 * on the selected student.
	 * 
	 * @param listener The listener object
	 */
	public static void setStudentChangedListener(StudentChangedListener listener) {
		studentChangedListener = listener;
	}

	/**
	 * Clears the currently logged in user, i.e. logs the user out. 
	 */
	public static void clearUser() {
		setStudent(null);
		user = null;
		userType = PersonType.LOGGEDOUT;
	}
}
