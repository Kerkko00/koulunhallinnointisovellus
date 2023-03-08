package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Teacher")
/**
 * Class for handling Teacher data
 * 
 * @author Ville, Saku
 */
public class Teacher {
	@Id
	@GeneratedValue
	private long teacherID;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "person_id", referencedColumnName = "person_ID", unique = true)
	private Person person;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username", unique = true)
	private User user;

	@OneToMany(mappedBy = "bulletinSender")
	private Set<Bulletin> teacherBulletins = new HashSet<>();

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "teacherGroup", referencedColumnName = "groupID")
	private Group teacherGroup;

	public Teacher(Person person, User user) {
		super();
		this.person = person;
		this.user = user;
	}

	public Group getTeacherGroup() {
		return teacherGroup;
	}

	public void setTeacherGroup(Group teacherGroup) {
		this.teacherGroup = teacherGroup;
	}

	public Teacher() {
	}

	public long getTeacherID() {
		return teacherID;
	}

	public Person getPerson() {
		return person;
	}

	public User getUser() {
		return user;
	}

	public Set<Bulletin> getBulletins() {
		return teacherBulletins;
	}

	public void setBulletins(Set<Bulletin> bulletins) {
		teacherBulletins = bulletins;
	}
}
