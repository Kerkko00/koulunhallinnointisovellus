package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Parent")
/**
 * Class for handling parent data
 * 
 * @author Ville, Saku
 */
public class Parent {
	@Id
	@GeneratedValue
	private long parentID;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "person_id", referencedColumnName = "person_ID", unique = true)
	private Person person;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username", unique = true)
	private User user;

	@ManyToMany(mappedBy = "studentGuardians", fetch = FetchType.EAGER)
	private Set<Student> dependants = new HashSet<Student>();

	public Parent(Person person, User user) {
		super();
		this.person = person;
		this.user = user;
	}

	public Parent() {
	}

	public long getParentID() {
		return parentID;
	}

	public Person getPerson() {
		return person;
	}

	public User getUser() {
		return user;
	}

	public Set<Student> getDependants() {
		return dependants;
	}

	public void addDependant(Student dependant) {
		dependants.add(dependant);
		dependant.getGuardians().add(this);
	}
}
