package model.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Grade;
import model.HibernateUtil;
import model.Student;

/**
 * DAO class for handling CRUD database operations for grades.
 * 
 * @author Olli
 */
public class GradeDAO implements IGradeDAO {

	private SessionFactory sessionFactory = null;
	private Transaction transaction = null;

	/**
	 * Initializes a new GradeDAO instance.
	 */
	public GradeDAO() {
		try {
			sessionFactory = HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			System.err.println("SessionFactory creation failed: " + e.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public boolean createGrade(Grade grade) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(grade);
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
	public Grade getGrade(long id) {
		try (Session session = sessionFactory.openSession()) {
			Grade grade = new Grade();
			session.load(grade, id);

			return grade;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Grade[] getGrades(Student student) {
		Grade[] gradeArray = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Grade> gradeList = session.createQuery("FROM Grade WHERE student =: student")
					.setParameter("student", student).list();

			gradeArray = new Grade[gradeList.size()];
			gradeList.toArray(gradeArray);
			transaction.commit();
			return gradeArray;
		} catch (Exception e) {
			if (transaction != null)
				// transaction.rollback();
				System.err.println("Database search was unsuccessful: " + e.getMessage());
		}
		return gradeArray;
	}

	@Override
	public boolean updateGrade(Grade grade) {
		if (getGrade(grade.getID()) != null) {
			try {
				createGrade(grade);
				return true;
			} catch (Exception e) {
				System.err.println("db entry update was not successfull ");
			}
		} else {
			System.err.println("Could not find grade on db");
		}
		return false;
	}

	@Override
	public boolean deleteGrade(Grade grade) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(Grade.class, grade.getID()));
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
