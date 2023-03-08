package controller;

import model.Student;

/**
 * Abstract class for listening to when the selected student is changed from the
 * sidebar (observer pattern).
 * 
 * @author Saku
 */
public abstract class StudentChangedListener {
	/**
	 * Called when the selected student is changed.
	 * 
	 * @param currentStudent The currently selected student
	 */
	public abstract void studentChanged(Student currentStudent);
}
