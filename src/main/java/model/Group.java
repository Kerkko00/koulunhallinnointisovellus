package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "StudentGroup")
/**
 * Class for handling student group data
 * 
 * @author Saku, Ville, Kerkko
 */
public class Group {

	@Id
	@GeneratedValue
	private long groupID;

	@Column(name = "name")
	private String groupName;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "teacher", referencedColumnName = "teacherID")
	private Teacher groupTeacher;

	@OneToMany(mappedBy = "studentGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Student> groupStudents = new HashSet<>();

	@ManyToMany(mappedBy = "bulletinGroups")
	private Set<Bulletin> groupBulletins = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Course> groupCourses = new HashSet<>();

	public Group() {
	}

	public Group(String name, Teacher teacher, Set<Student> students) {
		this.groupName = name;
		this.groupTeacher = teacher;
		this.groupStudents = students;

		for (Student student : groupStudents) {
			student.setGroup(this);
		}
	}

	public long getID() {
		return groupID;
	}

	public String getName() {
		return groupName;
	}

	public void setName(String name) {
		groupName = name;
	}

	public Teacher getTeacher() {
		return groupTeacher;
	}

	public void setTeacher(Teacher teacher) {
		groupTeacher = teacher;
	}

	public Set<Student> getStudents() {
		return groupStudents;
	}

	public void setStudents(Set<Student> students) {
		this.groupStudents = students;
	}

	public void addStudent(Student student) {
		groupStudents.add(student);
		student.setGroup(this);
	}

	public Set<Bulletin> getBulletins() {
		return groupBulletins;
	}

	public void setBulletins(Set<Bulletin> bulletins) {
		groupBulletins = bulletins;
	}

	@Override
	public String toString() {
		return groupName;
	}
}
