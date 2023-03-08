package model;

import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "User")
/**
 * Class for handling User data
 * 
 * @author Ville, Kerkko
 */
public class User {

	@Id
	@Column(name = "username", unique = true)
	private String username;

	@Column(name = "password")
	private String password;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "teacher", referencedColumnName = "teacherID", unique = true)
	private Teacher teacher;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "parent", referencedColumnName = "parentID", unique = true)
	private Parent parent;

	@Column(name = "language")
	private Locale language;

	public User() {
	}

	public User(String username, String password, Teacher teacher) {
		super();
		this.username = username;
		this.password = password;
		this.teacher = teacher;
	}

	public User(String username, String password, Parent parent) {
		super();
		this.username = username;
		this.password = password;
		this.parent = parent;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}

}
