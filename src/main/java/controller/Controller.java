package controller;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javafx.util.Pair;
import model.Absence;
import model.Course;
import model.Group;
import model.Parent;
import model.Person;
import model.PersonType;
import model.Student;
import model.User;
import model.dao.AbsenceDAO;
import model.dao.CourseDAO;
import model.dao.GroupDAO;
import model.dao.IAbsenceDAO;
import model.dao.ICourseDAO;
import model.dao.IGroupDAO;
import model.dao.IPersonDAO;
import model.dao.PersonDAO;
import view.GUI;

/**
 * Controller for linking model and view.
 * 
 * @author Kerkko, Ville
 */
public class Controller implements IController {

	/**
	 * Private instance for GUI
	 */
	private GUI gui;

	/**
	 * Model instance for person DAO
	 */
	private IPersonDAO personDAO;

	/**
	 * Private instance for courseDAO
	 */
	private ICourseDAO courseDAO;

	/**
	 * Private instance for groupDAO
	 */
	private IGroupDAO groupDAO;

	/**
	 * Private instance for absenceDAO
	 */
	private IAbsenceDAO absenceDAO;

	/**
	 * Constructor for controller
	 * 
	 * @param gui from View
	 */
	public Controller(GUI gui) {
		this.gui = gui;
		this.courseDAO = new CourseDAO();
		this.groupDAO = new GroupDAO();
		this.personDAO = new PersonDAO();
		this.absenceDAO = new AbsenceDAO();
	}

	/**
	 * Constructor for test cases
	 */
	public Controller() {
		this.courseDAO = new CourseDAO();
		this.groupDAO = new GroupDAO();
		this.personDAO = new PersonDAO();
		this.absenceDAO = new AbsenceDAO();
	}

	/**
	 * @return The GUI connected to this controller
	 */
	public GUI getGui() {
		return gui;
	}

	@Override
	public boolean createUser(PersonType type, String firstName, String lastName, String phonenumber, String email,
			String username, String password) {
		try {
			Person person = new Person(firstName, lastName, phonenumber, email);
			User user = new User(username.toLowerCase(), password);
			personDAO.createUserForPerson(type, person, user);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean createUser(PersonType type, String firstName, String lastName, String phonenumber, String email) {
		try {
			String username = firstName + "." + lastName;
			// TODO: Random password
			String password = "123";
			Person person = new Person(firstName, lastName, phonenumber, email);
			User user = new User(username.toLowerCase(), password);
			personDAO.createUserForPerson(type, person, user);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean login(String username, String password) {
		try {
			User userLogin = personDAO.getUser(username.toLowerCase(), password);

			if (userLogin == null) {
				return false;
			}

			if (userLogin.getParent() != null) {
				CurrentUser.setUserType(PersonType.PARENT);
				CurrentUser.setPerson(userLogin.getParent().getPerson());
			} else if (userLogin.getTeacher() != null) {
				CurrentUser.setUserType(PersonType.TEACHER);
				CurrentUser.setPerson(userLogin.getTeacher().getPerson());
			} else {
				throw new Error("User has no type");
			}

			CurrentUser.setUser(userLogin);
			return true;

		} catch (Exception e) {
			throw new Error(e.getMessage());
		}
	}

	@Override
	public boolean logout() {
		CurrentUser.clearUser();
		return true;
	}

	@Override
	public boolean createCourse(Course course) {
		return courseDAO.createCourse(course);
	}

	@Override
	public Course[] getCourses() {
		return courseDAO.getCourses();
	}

	@Override
	public boolean deleteCourse(long id) {
		return courseDAO.deleteCourse(id);
	}

	@Override
	public boolean createGroup(String name, Set<Student> students) {
		try {
			Group group = new Group(name, CurrentUser.getUser().getTeacher(), students);

			// Associate the group's students with this group
			for (Student student : students) {
				student.setGroup(group);
			}

			CurrentUser.getUser().getTeacher().setTeacherGroup(group);
			groupDAO.createGroup(group);
			personDAO.createTeacher(CurrentUser.getUser().getTeacher());

			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public Group getGroup(String name) {
		return groupDAO.getGroup(name);
	}

	@Override
	public Group[] getGroups() {
		return groupDAO.getGroups();
	}

	@Override
	public boolean deleteGroup(Group group) {
		group.getTeacher().setTeacherGroup(null);

		// Remove the students from this group
		for (Student student : group.getStudents()) {
			student.setGroup(null);
			personDAO.createStudent(student);
		}

		personDAO.createTeacher(group.getTeacher());
		return groupDAO.deleteGroup(group.getID());
	}

	@Override
	public boolean createStudent(List<Pair<String, String>> students, String parentEmail) {
		try {
			Parent parent = personDAO.getParentByEmail(parentEmail);
			for (Pair<String, String> student : students) {
				Student newStudent = new Student(new Person(student.getKey(), student.getValue()));
				parent.addDependant(newStudent);
				personDAO.createStudent(newStudent);
				personDAO.updateParent(parent);
			}
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Student> getStudent(String name) {
		return personDAO.getStudent(name);
	}

	@Override
	public boolean updateUser(User user, String oldPassword) {
		try {
			return personDAO.updateUser(user, oldPassword);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean updatePerson(Person person) {
		try {
			return personDAO.updatePerson(person);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean createAbsence(String reason, Date date, long courseID, long studentID) {
		try {
			Student student = personDAO.getStudentById(studentID);
			Absence absence = new Absence(reason, date, courseDAO.getCourse(courseID), student);
			absenceDAO.createAbsence(absence);

			// Associate the absence to the student
			student.addAbsences(absence);
			personDAO.createStudent(student);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Absence> getAbsencesByStudent(long studentID) {
		return absenceDAO.getAbsencesByStudent(studentID);
	}

	@Override
	public List<Absence> getAbsencesByCourse(long courseID) {
		return absenceDAO.getAbsencesByCourse(courseID);
	}

	@Override
	public boolean updateAbsence(Absence absence) {
		return absenceDAO.createAbsence(absence);
	}

}
