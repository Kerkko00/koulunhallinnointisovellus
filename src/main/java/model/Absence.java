package model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Absence")
/**
 * Class for handling Absence data
 * 
 * @author Ville
 */
public class Absence {

	@Id
	@GeneratedValue
	private long absenceID;

	@Column
	private String reason;

	@Column(nullable = false)
	private Date date;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Course course;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Student student;

	public Absence() {
	}

	public Absence(String reason, Date date, Course course, Student student) {
		super();
		this.reason = reason;
		this.date = date;
		this.course = course;
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public long getAbsenceID() {
		return absenceID;
	}
}
