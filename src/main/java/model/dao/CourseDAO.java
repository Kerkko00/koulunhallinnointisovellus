/**
 * 
 */
package model.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Course;
import model.HibernateUtil;

/**
 * DAO class for handling CRUD database operations for courses.
 * 
 * @author Kerkko
 */
public class CourseDAO implements ICourseDAO {

	private SessionFactory sessionFactory = null;
	private Transaction transaction = null;

	/**
	 * Initializes a new CourseDAO instance.
	 */
	public CourseDAO() {
		try {
			sessionFactory = HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			System.err.println("SessionFactory creation was unsuccessful: " + e.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public boolean createCourse(Course course) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(course);
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
	public Course getCourse(long id) {
		try (Session session = sessionFactory.openSession()) {
			Course course = new Course();
			session.load(course, id);

			return course;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Course[] getCourses() {
		Course[] courseArray = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			@SuppressWarnings("unchecked")
			List<Course> courseList = session.createQuery("FROM Course").list();
			courseArray = new Course[courseList.size()];
			courseList.toArray(courseArray);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			System.err.println("Database search was unsuccessful: " + e.getMessage());
		}
		return courseArray;
	}

	@Override
	public boolean deleteCourse(long id) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(Course.class, id));
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}
}
