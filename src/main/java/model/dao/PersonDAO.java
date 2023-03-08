package model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import model.HibernateUtil;
import model.Parent;
import model.Person;
import model.PersonType;
import model.Student;
import model.Teacher;
import model.User;

/**
 * DAO class for handling CRUD database operations for persons and their related
 * classes: users, teachers, parents and students.
 * 
 * @author Ville
 */
public class PersonDAO implements IPersonDAO {
	private SessionFactory sessionFactory = null;
	private Transaction transaction = null;

	/**
	 * Initializes a new PersonDAO instance.
	 */
	public PersonDAO() {
		try {
			sessionFactory = HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			System.err.println("SessionFactoryn creation was not successful " + e.getMessage());
			System.exit(-1);
		}
	}

	public void finalize() {
		try {
			if (sessionFactory != null) {
				// sessionFactory.close();
			}
		} catch (Exception e) {
			System.err.println("SessionFactoryn closure failed");
		}
	}

	@Override
	public boolean createPerson(Person person) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(person);
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
				throw e;
			}
		}

		return false;
	}

	@Override
	public boolean createUser(User user) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(user);
			transaction.commit();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean createUserForPerson(PersonType type, Person person, User user) {
		createPerson(person);

		switch (type) {
		case TEACHER:
			Teacher teacher = new Teacher(person, user);
			user.setTeacher(teacher);
			createUser(user);
			createTeacher(teacher);
			return true;
		case PARENT:
			Parent parent = new Parent(person, user);
			user.setParent(parent);
			createUser(user);
			createParent(parent);
			return true;
		case STUDENT:
			Student student = new Student(person);
			createStudent(student);
			return true;
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

	@Override
	public boolean createTeacher(Teacher teacher) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(teacher);
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
				throw e;
			}
		}

		return false;
	}

	@Override
	public boolean createParent(Parent parent) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(parent);
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
				throw e;
			}
		}

		return false;
	}

	@Override
	public boolean createStudent(Student student) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(student);
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
				throw e;
			}
		}

		return false;
	}

	@Override
	public Person getPerson(long id) {
		try (Session session = sessionFactory.openSession()) {
			Person person = new Person();
			session.load(person, id);

			return person;
		} catch (ObjectNotFoundException e) {
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	public Person[] getPersons() {
		try (Session session = sessionFactory.openSession()) {
			List<Person> personsList = session.createQuery("FROM Person", Person.class).getResultList();
			Person[] persons = new Person[personsList.size()];

			for (int i = 0; i < persons.length; i++) {
				persons[i] = personsList.get(i);
			}

			return persons;
		} catch (ObjectNotFoundException e) {
			return null;
		} catch (Exception e) {
			System.err.println("Database search was not seccessful");
		}

		return null;
	}

	@Override
	public User getUser(String username, String password) {
		try (Session session = sessionFactory.openSession()) {
			Query query = session.createQuery("FROM User WHERE username=:user AND password=:pass");
			query.setParameter("user", username);
			query.setParameter("pass", password);
			User user = (User) query.getSingleResult();

			return user;
		} catch (NoResultException exp) {
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	public User[] getUsers() {
		try (Session session = sessionFactory.openSession()) {
			String q = "FROM User";
			List<User> results = session.createQuery(q, User.class).getResultList();

			return results.toArray(new User[0]);
		} catch (NoResultException exp) {
			return null;
		} catch (Exception e) {
			System.out.println("User search failed : " + e.getMessage());
		}

		return null;
	}

	@Override
	public Teacher[] getTeachers() {
		try (Session session = sessionFactory.openSession()) {
			List<Teacher> teachersList = session.createQuery("FROM Teacher", Teacher.class).getResultList();
			Teacher[] teachers = new Teacher[teachersList.size()];

			for (int i = 0; i < teachers.length; i++) {
				teachers[i] = teachersList.get(i);
			}

			return teachers;
		} catch (ObjectNotFoundException e) {
			return null;
		} catch (Exception e) {
			System.err.println("Database search was not seccessful:");
		}

		return null;
	}

	@Override
	public Parent getParentByEmail(String email) {
		try (Session session = sessionFactory.openSession()) {
			Query query = session.createSQLQuery(
					"SELECT Parent.* FROM Parent INNER JOIN Person ON Parent.person_id=Person.person_ID WHERE Person.email=:mail");
			((NativeQuery<?>) query).addEntity(Parent.class);
			query.setParameter("mail", email);
			Parent parent = (Parent) query.getSingleResult();

			return parent;
		} catch (NoResultException exp) {
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	public Parent[] getParents() {
		try (Session session = sessionFactory.openSession()) {
			List<Parent> parentsList = session.createQuery("FROM Parent", Parent.class).getResultList();
			Parent[] parents = new Parent[parentsList.size()];

			for (int i = 0; i < parents.length; i++) {
				parents[i] = parentsList.get(i);
			}

			return parents;
		} catch (ObjectNotFoundException e) {
			System.err.println("Database search was not seccessful");
		} catch (Exception e) {
			System.err.println("Database search was not seccessful");
		}

		return null;
	}

	@Override
	public Student getStudentById(long id) {
		try (Session session = sessionFactory.openSession()) {
			Student student = new Student();
			session.load(student, id);

			return student;
		} catch (ObjectNotFoundException e) {
			System.out.println("No student found with " + id + " ID");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> getStudent(String name) {
		List<Student> students = new ArrayList<>();

		try (Session session = sessionFactory.openSession()) {
			Query query = session.createSQLQuery(
					"SELECT Student.* FROM Student INNER JOIN Person ON Person.person_id = Student.person_id WHERE CONCAT(LOWER(Person.first_name), ' ' , LOWER(Person.last_name)) LIKE :name");
			((NativeQuery<?>) query).addEntity(Student.class);
			query.setParameter("name", "%" + name.toLowerCase() + "%");
			students = query.getResultList();

			return students;
		} catch (NoResultException exp) {
			return students;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return students;
	}

	@Override
	public Student[] getStudents() {
		List<Student> studentsList = new ArrayList<>();
		try (Session session = sessionFactory.openSession()) {
			studentsList = session.createQuery("FROM Student", Student.class).getResultList();
			Student[] students = new Student[studentsList.size()];

			for (int i = 0; i < students.length; i++) {
				students[i] = studentsList.get(i);
			}

			return students;
		} catch (ObjectNotFoundException e) {
			return new Student[0];
		} catch (Exception e) {
			System.err.println("Couldn't find students on database: " + e.getMessage());
		}

		return new Student[0];
	}

	@Override
	public boolean updatePerson(Person person) {
		if (getPerson(person.getID()) != null) {
			try {
				createPerson(person);
				return true;
			} catch (Exception e) {
				System.err.println("db entryn update was not succsesfull ");
			}
		} else {
			System.err.println("Could not find person on db");
		}

		return false;
	}

	@Override
	public boolean updateUser(User user, String oldPassword) {
		if (getUser(user.getUsername(), oldPassword) != null) {
			try {
				createUser(user);
				return true;
			} catch (Exception e) {
				System.err.println("db entryn update was not succsesfull ");
			}
		} else {
			System.err.println("Could not find user on db");
		}

		return false;
	}

	@Override
	public Parent updateParent(Parent parent) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(parent);
			transaction.commit();

			return parent;
		} catch (ObjectNotFoundException e) {
			return null;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	public boolean deletePerson(long id) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(Person.class, id));
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}

	@Override
	public boolean deleteUser(String username) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(User.class, username));
			transaction.commit();

			return true;
		} catch (ObjectNotFoundException e) {
			return false;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println(e.getMessage());
		}

		return false;
	}

	@Override
	public boolean deleteTeacher(long id) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(Teacher.class, id));
			transaction.commit();

			return true;
		} catch (ObjectNotFoundException e) {
			return false;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println(e.getMessage());
		}

		return false;
	}

	@Override
	public boolean deleteParent(long id) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(Parent.class, id));
			transaction.commit();

			return true;
		} catch (ObjectNotFoundException e) {
			return false;
		} catch (Exception e) {
			if (transaction != null) {
				System.out.println(e.getMessage());
			}
		}

		return false;
	}

	@Override
	public boolean deleteStudent(long id) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(Student.class, id));
			transaction.commit();

			return true;
		} catch (ObjectNotFoundException e) {
			return false;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println(e.getMessage());
		}

		return false;
	}
}
