package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Student")
/**
 * Class for handling Student data
 * 
 * @author Saku, Kerkko, Ville, Olli
 */
public class Student {
	@Id
	@GeneratedValue
	private long studentID;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "person_id", referencedColumnName = "person_ID", unique = true)
	private Person person;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "Guardians", joinColumns = { @JoinColumn(name = "studentID") }, inverseJoinColumns = {
			@JoinColumn(name = "parentID") })
	private Set<Parent> studentGuardians = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "groupID", nullable = true)
	private Group studentGroup;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Set<Absence> absences = new HashSet<>();

	@OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
	private Set<Grade> studentGrades = new HashSet<>();

	public Student(Person person) {
		super();
		this.person = person;
	}

	public Student() {
	}

	public void addAbsences(Absence absence) {
		absences.add(absence);
	}

	public long getStudentID() {
		return studentID;
	}

	public Person getPerson() {
		return person;
	}

	public Set<Parent> getGuardians() {
		return studentGuardians;
	}

	public Group getGroup() {
		return studentGroup;
	}

	public void setGroup(Group group) {
		studentGroup = group;
	}

	public void addGuardian(Parent guardian) {
		studentGuardians.add(guardian);

		guardian.getDependants().add(this);
	}

	public void addToGroup(Group group) {
		group.getStudents().add(this);

		studentGroup = group;
	}

	public Set<Absence> getAbsences() {
		return absences;
	}

	public Set<Grade> getGrades() {
		return studentGrades;
	}

	public void setGrades(Set<Grade> grades) {
		studentGrades = grades;
	}

	public String toString() {
		return getPerson().getFullName();
	}
}
