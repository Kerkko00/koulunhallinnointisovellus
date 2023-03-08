package model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Grade")
/**
 * Class for handling Grade data
 * 
 * @author Olli
 */
public class Grade {

	@Id
	@GeneratedValue
	private long gradeID;

	@ManyToOne
	@JoinColumn(name = "StudentID")
	private Student student;

	@Column(name = "Date")
	private Date date;

	@Column(name = "Name")
	private String name;

	@Column(name = "Grade")
	private int gradeNumber;

	public Grade() {
	}

	public Grade(String name, int gradeNumber, Date date, Student student) {
		this.name = name;
		this.gradeNumber = gradeNumber;
		this.date = date;
		this.student = student;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getGrade() {
		return gradeNumber;
	}

	public void setGrade(int grade) {
		this.gradeNumber = grade;
	}

	public long getID() {
		return gradeID;
	}
}
