package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Course")
/**
 * Class for handling Course data
 * 
 * @author Kerkko
 */
public class Course {
	@Id
	@GeneratedValue
	private long courseID;

	@Column(name = "courseName")
	private String courseName;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Group group;

	@Column(name = "startDate")
	private LocalDate startDate;

	@Column(name = "endDate")
	private LocalDate endDate;

	public Course() {
	}

	/**
	 * Primary constructor for Course
	 * 
	 * @param courseName
	 * @param group
	 * @param startDate
	 * @param endDate
	 */
	public Course(String courseName, Group group, LocalDate startDate, LocalDate endDate) {
		super();
		this.courseName = courseName;
		this.group = group;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public long getCourseID() {
		return courseID;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return courseName + " " + group.getName() + " " + startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
				+ " " + endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
}
